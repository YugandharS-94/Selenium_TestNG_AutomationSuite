package com.test.support.base;

import com.test.support.actiondriver.ActionDriver;
import com.test.support.driver.BrowserFactory;
import com.test.support.driver.BrowserSelector;
import com.test.support.driver.DriverFactory;
import com.test.support.driver.ZapProxySetting;
import com.test.support.pages.entity.HomePage;
import com.test.support.pages.entity.LoginPage;
import com.test.support.utilities.ConfigReader;
import com.test.support.utilities.ExtentManager;
import com.test.support.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
    private static final ConfigReader configReader = new ConfigReader();
    private static final ThreadLocal<WebDriver> driverObj = new ThreadLocal<>();
    private static final ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    protected LoginPage loginPage;
    protected HomePage homePage;
    private ClientApi api;

    public static ConfigReader getConfig() {
        return configReader;
    }

    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            logger.error("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    public static WebDriver getDriver() {
        if (driverObj.get() == null) {
            logger.error("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driverObj.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up the driver for: {}", this.getClass().getName());
        setDriver(BrowserSelector.setBrowser());
        if (Boolean.parseBoolean(getConfig().getProperty("zapTest"))) {
            api = new ClientApi(ZapProxySetting.ZAP_PROXY_ADDRESS, ZapProxySetting.ZAP_PROXY_PORT, ZapProxySetting.ZAP_API_KEY);
        }

        configureBrowser();

        logger.info("WebDriver is initialized and browser maximized");

        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initialized for thread: {}", Thread.currentThread().getId());
        pagesSetup();
        logger.info("Page objects initialized for: {}", this.getClass().getName());

    }

    // configure browser setting like implicit wait, maximize browser and nagivate
    // to url
    private void configureBrowser() {
        getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(Integer.parseInt(getConfig().getProperty("implicitWait"))));
        getDriver().manage().window().maximize();
        ExtentManager.registerDriver(getDriver());
        logger.info("Driver registered with Extent report for the thread: {}", Thread.currentThread().getId());
        try {
            if (getConfig().getProperty("site").equalsIgnoreCase("entity")) {
                getDriver().get(getConfig().getProperty("entityUrl"));
            } else if (getConfig().getProperty("site").equalsIgnoreCase("ops")) {
                getDriver().get(getConfig().getProperty("opsUrl"));
            }
        } catch (Exception e) {

            logger.error("Failed to navigate to the URL: {}", e.getMessage());
            Assert.fail("Failed to navigate to the site");
        }
    }

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

    public void quitDriver() {
        if (driverObj.get() != null) {
            driverObj.get().quit();
            driverObj.remove();
            actionDriver.remove();

        }
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    @AfterMethod(alwaysRun = true)

    public void tearDown() {
        if (api != null && Boolean.parseBoolean(getConfig().getProperty("zapTest"))) {
            String title = "Jahia ZAP Security Report";
            String template = "traditional-html";
            String description = "This is Jahia ZAP security test report";
            String reportName = "Jahia-zap-report.html";
            String targetFolder = System.getProperty("user.dir");
            try {
                ApiResponse response = api.reports.generate(title, template, null, description, null, null, null, null, null, reportName, null, targetFolder, null);
                logger.info(() -> "Zap report generated at this Location: " + response.toString());
            } catch (ClientApiException e) {
                logger.warn(e);
            }
        }
        quitDriver();

    }

    @AfterSuite
    public void endSuite() {
        ExtentManager.endTest();
    }

    public void pagesSetup() {
        loginPage = new LoginPage();
        homePage = new HomePage();
    }
}
