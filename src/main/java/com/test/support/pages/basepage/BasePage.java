package com.test.support.pages.basepage;

import com.test.support.utilities.ExtentManager;
import com.test.support.utilities.TestStatus;
import org.testng.Assert;

import static com.test.support.base.BaseClass.getConfig;
import static com.test.support.base.BaseClass.getDriver;

public class BasePage {

    protected String siteUrl = null;

    public void navigateToUrl() {
        try {
            if (getConfig().getProperty("site").equalsIgnoreCase("entity")) {
                siteUrl = getConfig().getProperty("entityUrl");
                getDriver().get(siteUrl);
                ExtentManager.logStepWithScreenshot("Navigated to the entity portal login page", TestStatus.INFO);
            } else if (getConfig().getProperty("site").equalsIgnoreCase("ops")) {
                siteUrl = getConfig().getProperty("opsUrl");
                getDriver().get(siteUrl);
                ExtentManager.logStepWithScreenshot("Navigated to the ops console login page", TestStatus.INFO);
            }
        } catch (Exception e) {
            ExtentManager.logFailure(getDriver(), "Failed to navigate to the site: " + getConfig().getProperty("site"), TestStatus.FAIL);
            Assert.fail("Failed to navigate to the site");
        }
    }
}
