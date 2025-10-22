package com.wch.member.util;

import java.util.Properties;

public class ConfigHelper {
    
    public static final String DEFAULT_CACHE_SIZE = getProperty("cache.size", "100");
    
    private static final Properties CONFIG = loadConfig();
    
    public static final int MAX_CACHE_SIZE = Integer.parseInt(DEFAULT_CACHE_SIZE);
    
    private static Properties loadConfig() {
        Properties props = new Properties();
        props.setProperty("cache.size", "500");
        props.setProperty("cache.timeout", "30000");
        return props;
    }
    
    public static String getProperty(String key, String defaultValue) {
        if (CONFIG == null) {
            return defaultValue;
        }
        return CONFIG.getProperty(key, defaultValue);
    }
    
    public static int getCacheSize() {
        return MAX_CACHE_SIZE;
    }
}