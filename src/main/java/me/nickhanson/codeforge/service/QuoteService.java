package me.nickhanson.codeforge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class QuoteService {

    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);

    @Value("${app.quote.apiUrl:https://zenquotes.io/api/random}")
    private String apiUrl;

    private static final String FALLBACK = "Keep it simple. — CodeForge";

    private final RestTemplate http;

    public QuoteService() {
        this.http = new RestTemplate();
    }

    /**
     * Fetches a random inspirational quote from the external API.
     * If the API call fails or returns unexpected data, a fallback quote is returned.
     *
     * @return A quote string in the format "“Quote text” — Author"
     */
    public String getRandomQuote() {
        log.info("Fetching random quote from {}", apiUrl);
        try {
            ResponseEntity<List<Map<String, Object>>> response = http.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<Map<String, Object>> body = response.getBody();
            if (body != null && !body.isEmpty()) {
                Map<String, Object> q = body.get(0);
                String text = (String) q.get("q");
                String author = (String) q.get("a");
                return "“" + text + "” — " + author;
            } else {
                log.error("Unexpected response: status={} body={}", response.getStatusCode(), body);
            }
        } catch (RestClientException ex) {
            log.error("API call failed: {}", ex.getMessage(), ex);
        }
        log.info("Returning fallback quote");
        return FALLBACK;
    }
}