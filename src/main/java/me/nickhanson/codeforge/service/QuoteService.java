package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.QuoteResponseItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.nickhanson.codeforge.config.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;

/**
 * Service for fetching random inspirational quotes from an external API.
 * @author Nick Hanson
 */
public class QuoteService implements PropertiesLoader {
    private static final Logger log = LogManager.getLogger(QuoteService.class);
    private static final String FALLBACK = "Keep it simple. — CodeForge";
    private static final long CACHE_DURATION_MS = 60_000; // 1 minute cache duration

    private final String apiUrl;
    private final HttpClient http;
    private final ObjectMapper mapper;

    // simple cache
    private String lastQuote;
    private long lastFetchTime = 0;

    // Constructor initializes HttpClient and loads configuration properties
    public QuoteService() {
        Properties props = loadProperties("/application.properties");
        this.apiUrl = props.getProperty("quote.api.url", "https://zenquotes.io/api/random");
        int timeout = Integer.parseInt(props.getProperty("quote.timeout.seconds", "5"));
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeout))
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
        if (lastQuote != null && (now - lastFetchTime) < CACHE_DURATION_MS) {
            return lastQuote;
        }

        String text;
        String author;

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(5))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();

            // Handle non-200 responses gracefully
            if (status != 200) {
                log.warn("Quote API returned status {}: {}", status, truncate(resp.body()));
                return FALLBACK;
            }

            QuoteResponseItem[] responseArray = mapper.readValue(resp.body(), QuoteResponseItem[].class);
            if (responseArray == null || responseArray.length == 0 || responseArray[0] == null) {
                log.warn("Quote API returned empty payload");
                return FALLBACK;
            }
            if (responseArray.length == 1) {
                text = responseArray[0].getQ();
                author = responseArray[0].getA();
            } else {
                // Pick a random quote from the array if multiple are returned
                int index = (int) (Math.random() * responseArray.length);
                text = responseArray[index].getQ();
                author = responseArray[index].getA();
            }
            if (text == null || text.isBlank()) return FALLBACK;
            if (author == null || author.isBlank()) author = "Unknown";

            // format and cache result
            lastQuote = "“" + text + "” — " + author;
            lastFetchTime = now;

            return "\u201c" + text + "\u201d — " + author; // “text” — Author
        } catch (IOException | InterruptedException ie) {
            log.warn("Quote fetch failed: {}", ie.toString());
            return FALLBACK;
        } catch (Exception e) {
            log.error("Unexpected error fetching quote", e);
            return FALLBACK;
        }
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