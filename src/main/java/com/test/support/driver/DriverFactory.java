package com.test.support.driver;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

	private static ThreadLocal<WebDriver> driverObj = new ThreadLocal<>();

	public WebDriver setDriver(BrowserFactory browser) {
		if (driverObj.get() == null) {
			synchronized (DriverFactory.class) {
				if (driverObj.get() == null) {
					switch (browser) {
					case CHROME, FIREFOX, EDGE, SAFARI:
						driverObj.set(browser.createDriver());
						break;
					}
				}
			}
		}
		return driverObj.get();
	}

	public WebDriver getDriver() {
		if (driverObj.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driverObj.get();
	}

	public void quitDriver() {
		if (driverObj.get() != null) {
			driverObj.get().quit();
			driverObj.remove();

		}
	}

}
