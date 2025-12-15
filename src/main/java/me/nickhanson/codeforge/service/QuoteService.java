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
 * Service to fetch random programming quotes from an external API.
 * Caches the last fetched quote for a configurable duration to reduce API calls.
 * Supports filtering by author and tags via configuration properties.
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

    // HTTP request timeout
    private final Duration requestTimeout;

    // Cache for last fetched quote
    private String lastQuote;
    private long lastFetchTime = 0;

    // User preferences
    private String userAuthor = null;

    // Constructor initializes HttpClient and loads configuration properties
    public QuoteService() {
        logger.info("Java={}, Vendor={}", System.getProperty("java.version"), System.getProperty("java.vendor"));
        Properties props = loadProperties("/application.properties");
        this.apiBaseUrl = props.getProperty("quote.api.url", props.getProperty("quote.fallback.url", ""));
        boolean allowInsecure = Boolean.parseBoolean(props.getProperty("quote.api.allowInsecure","false"));
        if (!allowInsecure && apiBaseUrl.startsWith("http://")) {
            logger.warn("Insecure HTTP configured for quote.api.url. Switch to HTTPS in production.");
        }

        // Safe parse with fallback (avoid NumberFormatException)
        this.CACHE_DURATION = safeParseLong(props.getProperty("quote.cache.duration.ms"), 60000L, "quote.cache.duration.ms");
        this.userAuthor = props.getProperty("quote.author", "").trim();
        this.OR_TAGS  = parseCsv(props.getProperty("quote.tags.or", ""));
        this.AND_TAGS = parseCsv(props.getProperty("quote.tags.and", ""));
        int timeoutSeconds = (int) safeParseLong(props.getProperty("quote.api.timeout.seconds"), 5L, "quote.api.timeout.seconds");
        if (timeoutSeconds <= 0) timeoutSeconds = 5; // guard negative/zero
        this.requestTimeout = Duration.ofSeconds(timeoutSeconds);
        this.http = HttpClient.newBuilder()
                .connectTimeout(requestTimeout)
                .build();
        this.mapper = new ObjectMapper();
    }

    /**
     * Safely parses a string to a long, returning a default value on failure.
     * Logs warnings for invalid or non-positive values.
     *
     * @param value The string value to parse.
     * @param def   The default value to return on failure.
     * @param key   The configuration key for logging context.
     * @return The parsed long value or the default.
     */
    private long safeParseLong(String value, long def, String key) {
        if (value == null || value.isBlank()) return def;
        try {
            long parsed = Long.parseLong(value.trim());
            if (parsed <= 0) {
                logger.warn("Config '{}' non-positive ({}). Using default {}", key, parsed, def);
                return def;
            }
            return parsed;
        } catch (NumberFormatException nfe) {
            logger.warn("Config '{}' invalid value '{}' ({}). Using default {}", key, value, nfe.getMessage(), def);
            return def;
        }
    }

    /**
     * Fetches a random programming quote from the external API.
     * Caches the result for a configurable duration to minimize API calls.
     * Applies user-specified filters for author and tags.
     *
     * @return A formatted quote string in the format: "text — Author"
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

            logger.info("URL used: {}", uri.toString());

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

            // format and cache result via helper
            String formatted = formatQuote(text, author);
            lastQuote = formatted;
            lastFetchTime = now;

            return formatted; // “text” — Author
        } catch (IOException | InterruptedException ie) {
            logger.warn("Quote fetch failed: {}", ie.toString());
            return FALLBACK;
        } catch (Exception e) {
            logger.error("Unexpected error fetching quote", e);
            return FALLBACK;
        }
    }

    /**
     * Builds the URI for the quote API request based on user preferences.
     *
     * @return The constructed URI with appropriate query parameters.
     */
    private URI buildQuoteUri() {
        // Build query parameters based on user preferences
        StringBuilder url = new StringBuilder(apiBaseUrl);
        // Collect query parameters
        List<String> params = new ArrayList<>();

        // author only if specified, blank means any author, randomly chosen
         if (userAuthor != null && !userAuthor.isBlank()) {
            params.add("author=" + URLEncoder.encode(userAuthor, StandardCharsets.UTF_8));
        } else {
            // tags only if no author
            String orPart  = String.join("|", OR_TAGS);
            String andPart = String.join(",", AND_TAGS);

            // Combine the OR & AND parts appropriately
            String tagsValue = null;
            if (!orPart.isBlank() && !andPart.isBlank()) {
                tagsValue = orPart + "," + andPart;       // (OR) AND (AND)
            } else if (!orPart.isBlank()) {
                tagsValue = orPart;                        // OR only
            } else if (!andPart.isBlank()) {
                tagsValue = andPart;                       // AND only
            }

            // Add tags parameter if applicable
            if (tagsValue != null && !tagsValue.isBlank()) {
                params.add("tags=" + URLEncoder.encode(tagsValue, StandardCharsets.UTF_8));
            }
        }

         // Append query parameters to URL
        if (!params.isEmpty()) {
            url.append("?").append(String.join("&", params));
        }
        return URI.create(url.toString());
    }

    /**
     * Parses a comma-separated string into a list of trimmed strings.
     *
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
     * Truncates a string to 200 characters, appending an ellipsis if truncated.
     *
     * @param string The input string.
     * @return The truncated string.
     */
    private static String truncate(String string) {
        if (string == null) return "";
        return string.length() > 200 ? string.substring(0, 200) + "…" : string;
    }

    /**
     * Formats a quote with its author.
     * Minimal for MVP--no trimming or validation beyond null/blank checks.
     *
     * @param text   The quote text.
     * @param author The quote author.
     * @return A formatted string in the format: "text — Author"
     */
    private static String formatQuote(String text, String author) {
        String safeText = (text == null) ? "" : text;
        String safeAuthor = (author == null || author.isBlank()) ? "Unknown" : author;
        return safeText + " — " + safeAuthor;
    }
}