package com.test.support.driver;

import com.test.support.utilities.ConfigReader;

public class BrowserSelector {
    private static ConfigReader configReader = new ConfigReader();

    private BrowserSelector() {

    }

    public static BrowserFactory setBrowser(String browser) {
        switch (browser.toUpperCase()) {
            case "CHROME":
                return BrowserFactory.CHROME;
            case "FIREFOX":
                return BrowserFactory.FIREFOX;
            case "EDGE":
                return BrowserFactory.EDGE;
            case "SAFARI":
                return BrowserFactory.SAFARI;
            default:
                throw new IllegalArgumentException("Unexpected value: " + browser);
        }

    }

    public static BrowserFactory setBrowser() {

        String browser;
        browser = configReader.getProperty("browser");
        switch (browser.toUpperCase()) {
            case "CHROME":
                return BrowserFactory.CHROME;
            case "FIREFOX":
                return BrowserFactory.FIREFOX;
            case "EDGE":
                return BrowserFactory.EDGE;
            case "SAFARI":
                return BrowserFactory.SAFARI;
            default:
                throw new IllegalArgumentException("Unexpected value: " + browser);
        }

    }

}
