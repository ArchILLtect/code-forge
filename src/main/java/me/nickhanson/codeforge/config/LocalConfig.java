package me.nickhanson.codeforge.config;

import java.util.Properties;

/**
 * Loads local configuration properties from a file.
 */
public class LocalConfig implements PropertiesLoader {

    private static Properties localProps;

    /**
     * Loads the local configuration properties from 'test-db.properties'.
     * @return Properties object containing the loaded properties.
     */
    public static Properties load() {
        if (localProps == null) {
            LocalConfig loader = new LocalConfig();
            localProps = loader.loadProperties("/test-db.properties");
        }
        return localProps;
    }

    /**
     * Retrieves the value of a specific property by key.
     * @param key The property key.
     * @return The property value associated with the key.
     */
    public static String get(String key) {
        Properties props = load();
        return props.getProperty(key);
    }
}