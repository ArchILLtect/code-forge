package me.nickhanson.codeforge.config;

import java.util.Properties;

public class LocalConfig implements PropertiesLoader {

    private static Properties localProps;

    public static Properties load() {
        if (localProps == null) {
            LocalConfig loader = new LocalConfig();
            localProps = loader.loadProperties("/local.properties");
        }
        return localProps;
    }

    public static String get(String key) {
        Properties props = load();
        return props.getProperty(key);
    }
}