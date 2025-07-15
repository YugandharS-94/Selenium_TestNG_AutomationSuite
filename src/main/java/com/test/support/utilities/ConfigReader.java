package com.test.support.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private Properties properties;


    public Properties getConfigReader(String configPath) {
        properties = new Properties();
        try {

            InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(configPath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Properties getConfigReader() {
        properties = new Properties();
        try {
            String configPath = "config.properties";
            InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(configPath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getProperty(String key) {
        return getConfigReader().getProperty(key);
    }

}
