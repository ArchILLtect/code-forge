package me.nickhanson.codeforge.controller;

import me.nickhanson.codeforge.auth.*;
import me.nickhanson.codeforge.config.EnvConfig;
import me.nickhanson.codeforge.config.PropertiesLoader;
import me.nickhanson.codeforge.entity.AuthenticatedUser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(
        urlPatterns = {"/auth"}
)
// Servlet to handle the callback from AWS Cognito after user login.
public class Auth extends HttpServlet implements PropertiesLoader {

    Properties properties;
    String CLIENT_ID;
    String CLIENT_SECRET;
    String OAUTH_URL;
    String LOGIN_URL;
    String REDIRECT_URL;
    String REGION;
    String POOL_ID;
    // Use existing error JSP under WEB-INF
    String ERROR_URL = "/WEB-INF/jsp/error/500.jsp";
    Keys jwks;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void init() throws ServletException {
        super.init();
        logger.debug("Initializing Auth servlet and loading properties.");
        properties = loadProperties("/cognito.properties");

        CLIENT_ID = properties.getProperty("client.id");
        // Read secret from environment variable only
        CLIENT_SECRET = System.getenv("COGNITO_CLIENT_SECRET");
        if (CLIENT_SECRET == null || CLIENT_SECRET.isBlank()) {
            logger.error("COGNITO_CLIENT_SECRET is not set. Refusing to start Auth servlet.");
            throw new ServletException("Missing COGNITO_CLIENT_SECRET environment variable");
        }
        OAUTH_URL = properties.getProperty("oAuthURL");
        LOGIN_URL = properties.getProperty("loginURL");
        REDIRECT_URL = EnvConfig.get(logger, properties, "redirectURL");
        REGION = properties.getProperty("region");
        POOL_ID = properties.getProperty("pool.id");

        loadKey();
    }

    /**
     * Gets the auth code from the request and exchanges it for a token containing user info.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authCode = req.getParameter("code");
        AuthenticatedUser user = null;

        if (authCode == null) {
            logger.error("Missing authorization code in callback. Forwarding to error page.");
            req.setAttribute("errorMessage", "Missing or invalid authorization code. Please try logging in again.");
            req.getRequestDispatcher(ERROR_URL).forward(req, resp);
            return;
        } else {
            HttpRequest authRequest = buildAuthRequest(authCode);
            try {
                TokenResponse tokenResponse = getToken(authRequest);
                user = validate(tokenResponse);

                // Persist the authenticated user in the session
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);

            } catch (SecurityException se) {
                req.setAttribute("errorMessage", "Your session could not be verified. Please sign in again.");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                req.getRequestDispatcher(ERROR_URL).forward(req, resp);
                return;
            } catch (IOException | InterruptedException ex) {
                logger.error("Auth exchange failed: {}", ex.getMessage(), ex);
                req.setAttribute("errorMessage", "Authentication service is temporarily unavailable.");
                resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                req.getRequestDispatcher(ERROR_URL).forward(req, resp);
                return;
            }
        }
        // Redirect to /me so the controller can render the view; session holds the user
        resp.sendRedirect(req.getContextPath() + "/me");
    }

    /**
     * Sends the request for a token to Cognito and maps the response
     * @param authRequest the request to the oauth2/token url in cognito
     * @return response from the oauth2/token endpoint which should include id token, access token and refresh token
     * @throws IOException - if an I/O error occurs
     * @throws InterruptedException - if the operation is interrupted
     */
    private TokenResponse getToken(HttpRequest authRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<?> response = null;

        response = client.send(authRequest, HttpResponse.BodyHandlers.ofString());


        logger.debug("Response headers: {}", response.headers().toString());
        logger.debug("Response body: {}", response.body().toString());

        ObjectMapper mapper = new ObjectMapper();
        TokenResponse tokenResponse = mapper.readValue(response.body().toString(), TokenResponse.class);
        logger.debug("Id token: {}", tokenResponse.getIdToken());

        return tokenResponse;

    }

    /**
     * Get values out of the header to verify the token is legit. If it is legit, get the claims from it, such
     * as username.
     * @param tokenResponse the response from the oauth2/token endpoint which includes the id token
     * @return the username from the token claims
     * @throws IOException - if an I/O error occurs
     */
    private AuthenticatedUser validate(TokenResponse tokenResponse) throws IOException {
        String userName = null;
        String email = null;
        String sub = null;

        ObjectMapper mapper = new ObjectMapper();
        CognitoTokenHeader tokenHeader = mapper.readValue(CognitoJWTParser.getHeader(tokenResponse.getIdToken()).toString(), CognitoTokenHeader.class);

        // Header should have kid and alg- https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-the-id-token.html
        String keyId = tokenHeader.getKid();
        String alg = tokenHeader.getAlg();

        // Find the corresponding JWK by key ID
        KeysItem jwk = jwks.getKeys().stream()
                .filter(k -> keyId.equals(k.getKid()))
                .findFirst()
                .orElse(null);

        if (jwk == null) {
            logger.error("JWK with key ID {} not found.", keyId);
            throw new IOException("JWK with key ID " + keyId + " not found.");
        }

        try {
            // Use Key's N and E
            byte[] nBytes = Base64.getUrlDecoder().decode(jwk.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(jwk.getE());
            BigInteger modulus  = new BigInteger(1, nBytes);
            BigInteger exponent = new BigInteger(1, eBytes);

            // Generate Public Key
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));

            // Create RSA256 Algorithm instance with public key
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

            // Verify ISS field of the token to make sure it's from the Cognito source
            String iss = String.format("https://cognito-idp.%s.amazonaws.com/%s", REGION, POOL_ID);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(iss)
                    .withClaim("token_use", "id") // make sure you're verifying id token
                    .build();

            // Verify the token
            DecodedJWT jwt = verifier.verify(tokenResponse.getIdToken());
            userName = jwt.getClaim("cognito:username").asString();
            email = jwt.getClaim("email").asString();
            sub = jwt.getClaim("sub").asString();

            logger.debug("here's the username: {}", userName);
            logger.debug("here are all the available claims: {}", jwt.getClaims());

        } catch (InvalidKeySpecException e) {
            logger.error("Unable to build RSA public key: {}", e.getMessage(), e);
            throw new SecurityException("Invalid verification key", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm Error {}", e.getMessage(), e);
            throw new SecurityException("Algorithm error", e);
        }
        return new AuthenticatedUser(userName, email, sub);
    }

    /** Create the auth url and use it to build the request.
     *
     * @param authCode auth code received from Cognito as part of the login process
     * @return the constructed oauth request
     */
    private HttpRequest buildAuthRequest(String authCode) {
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("client_id", CLIENT_ID);
        parameters.put("code", authCode);
        parameters.put("redirect_uri", REDIRECT_URL);

        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());

        return HttpRequest.newBuilder().uri(URI.create(OAUTH_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
    }

    /**
     * Gets the JSON Web Key Set (JWKS) for the user pool from cognito and loads it
     * into objects for easier use.
     * JSON Web Key Set (JWKS) location: https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json
     * Demo url: https://cognito-idp.us-east-2.amazonaws.com/us-east-2_XaRYHsmKB/.well-known/jwks.json
     *
     * @see Keys
     * @see KeysItem
     */
    private void loadKey() {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream in = new URL(
                String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json",
                        REGION, POOL_ID)).openStream()) {
            jwks = mapper.readValue(in, Keys.class);
            logger.debug("Keys are loaded. Here's e: {}", jwks.getKeys().get(0).getE());
        } catch (IOException ioException) {
            logger.error("Cannot load json... {}", ioException.getMessage(), ioException);
        } catch (Exception e) {
            logger.error("Error loading json {}", e.getMessage(), e);
        }
    }
}
