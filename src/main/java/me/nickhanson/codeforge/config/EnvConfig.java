package me.nickhanson.codeforge.config;

import org.apache.logging.log4j.Logger;

import java.util.Properties;


public final class EnvConfig {

    /**
     * The application environment, e.g., "dev" or "prod".
     */
    private static final String ENV = System.getenv().getOrDefault("APP_ENV", "dev").toLowerCase();

    // Prevent instantiation
    private EnvConfig() {}

    /**
     * Get the environment-specific property value.
     * @param props the Properties object
     * @param baseKey the base key (without environment suffix)
     * @return the property value for the current environment
     * @throws IllegalStateException if the property is missing
     */
    public static String get(Logger logger, Properties props, String baseKey) {
        // Matches property keys with environment suffixes.
        // For example: baseKey="redirectURL" -> looks for "redirectURL.dev" or "redirectURL.prod"
        // depending on the ENV value.
        logger.info("Fetching property for environment: " + ENV);
        String key = baseKey + "." + ENV;
        String path = props.getProperty(key);
        if (path == null || path.isEmpty())
            throw new IllegalStateException("Missing property: " + key);
        return path;
    }
}