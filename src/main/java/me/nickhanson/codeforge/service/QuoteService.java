package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.external.model.QuoteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nickhanson.codeforge.config.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Service for fetching random inspirational quotes from an external API.
 * @author Nick Hanson
 */
public class QuoteService implements PropertiesLoader {
    private static final Logger logger = LogManager.getLogger(QuoteService.class);
    private static final String FALLBACK = "Keep it simple. — CodeForge";

    private final long CACHE_DURATION;
    private final List<String> OR_TAGS;
    private final List<String> AND_TAGS;

    private final String apiBaseUrl;
    private final HttpClient http;
    private final ObjectMapper mapper;

    //
    private final Duration requestTimeout;

    // simple cache

    private String lastQuote;
    private long lastFetchTime = 0;

    // User preferences
    private String userAuthor = null;


        // Constructor initializes HttpClient and loads configuration properties
    public QuoteService() {
        logger.info("Java={}, Vendor={}", System.getProperty("java.version"), System.getProperty("java.vendor"));
        Properties props = loadProperties("/application.properties");
        this.apiBaseUrl = props.getProperty("quote.api.url", "https://zenquotes.io/api/random");
        boolean allowInsecure = Boolean.parseBoolean(props.getProperty("quote.api.allowInsecure","false"));
        if (!allowInsecure && apiBaseUrl.startsWith("http://")) {
            logger.warn("Insecure HTTP configured for quote.api.url. Switch to HTTPS in production.");
        }
        this.CACHE_DURATION = Long.parseLong(props.getProperty("quote.cache.duration.ms", "60000")); // 1 minute default
        this.userAuthor = props.getProperty("quote.author", "").trim();
        this.OR_TAGS  = parseCsv(props.getProperty("quote.tags.or", ""));
        this.AND_TAGS = parseCsv(props.getProperty("quote.tags.and", ""));
        int timeout = Integer.parseInt(props.getProperty("quote.api.timeout.seconds", "5"));
        this.requestTimeout = Duration.ofSeconds(timeout);
        this.http = HttpClient.newBuilder()
                .connectTimeout(requestTimeout)
                .build();
        this.mapper = new ObjectMapper();
    }

    /**
     * Fetches a random inspirational quote using HttpClient and Jackson, with resilient fallbacks.
     * @return A quote string formatted as: “text” — Author
     */
    public String getRandomQuote() {

        // Return cached quote if within cache duration
        long now = System.currentTimeMillis();

        // If cached quote is still valid, return it
        if (lastQuote != null && (now - lastFetchTime) < CACHE_DURATION) {
            // Log the current time passed since last fetch
            logger.info("Using cached quote, {} ms since last fetch", (now - lastFetchTime));
            return lastQuote;
        }

        String text;
        String author;

        try {
            URI uri = buildQuoteUri();
            logger .debug("Fetching quote from URI: {}", uri);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(requestTimeout)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();

            // Handle non-200 responses gracefully
            if (status != 200) {
                logger.warn("Quote API returned status {}: {}", status, truncate(resp.body()));
                return FALLBACK;
            }

            QuoteResponse[] responseArray = mapper.readValue(resp.body(), QuoteResponse[].class);
            if (responseArray == null || responseArray.length == 0 || responseArray[0] == null) {
                logger.warn("Quote API returned empty payload");
                return FALLBACK;
            }
            if (responseArray.length == 1) {
                text = responseArray[0].getContent();
                author = responseArray[0].getAuthor();
            } else {
                // Pick a random quote from the array if multiple are returned
                int index = (int) (Math.random() * responseArray.length);
                text = responseArray[index].getContent();
                author = responseArray[index].getAuthor();
            }
            if (text == null || text.isBlank()) return FALLBACK;
            if (author == null || author.isBlank()) author = "Unknown";

            // format and cache result
            lastQuote = "“" + text + "” — " + author;
            lastFetchTime = now;

            return "“" + text + "” — " + author; // “text” — Author
        } catch (IOException | InterruptedException ie) {
            logger.warn("Quote fetch failed: {}", ie.toString());
            return FALLBACK;
        } catch (Exception e) {
            logger.error("Unexpected error fetching quote", e);
            return FALLBACK;
        }
    }

    private URI buildQuoteUri() {
        StringBuilder url = new StringBuilder(apiBaseUrl);
        List<String> params = new ArrayList<>();

         if (userAuthor != null && !userAuthor.isBlank()) {
            params.add("author=" + URLEncoder.encode(userAuthor, StandardCharsets.UTF_8));
        } else {
            // tags only if no keyword/author
            String orPart  = String.join("|", OR_TAGS);
            String andPart = String.join(",", AND_TAGS);

            String tagsValue = null;
            if (!orPart.isBlank() && !andPart.isBlank()) {
                tagsValue = orPart + "," + andPart;       // (OR) AND (AND)
            } else if (!orPart.isBlank()) {
                tagsValue = orPart;                        // OR only
            } else if (!andPart.isBlank()) {
                tagsValue = andPart;                       // AND only
            }

            if (tagsValue != null && !tagsValue.isBlank()) {
                params.add("tags=" + URLEncoder.encode(tagsValue, StandardCharsets.UTF_8));
            }
        }

        if (!params.isEmpty()) {
            url.append("?").append(String.join("&", params));
        }
        return URI.create(url.toString());
    }

    /**
     * Parses a comma-separated string into a list of trimmed strings.
     * @param string The comma-separated string.
     * @return A list of trimmed strings.
     */
    private static List<String> parseCsv(String string) {
        if (string == null || string.isBlank()) return List.of();
        return Arrays.stream(string.split(","))
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .toList();
    }

    /**
     * Truncates a string to 200 characters for logging purposes.
     * Makes sure logs aren't flooded with gigantic payloads.
     * @param string The string to truncate.
     * @return The truncated string.
     */
    private static String truncate(String string) {
        if (string == null) return "";
        return string.length() > 200 ? string.substring(0, 200) + "…" : string;
    }
}