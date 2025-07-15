package com.test.support.pages.entity;

import com.test.support.actiondriver.ActionDriver;
import com.test.support.base.BaseClass;
import com.test.support.pages.basepage.BasePage;
import com.test.support.utilities.ExtentManager;
import com.test.support.utilities.TestStatus;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginPage extends BasePage {

    private final By userId = By.id("username");
    private final By password = By.id("password");
    private final By loginButton = By.id("submit");
    private final By forgotPassword = By.xpath("//a[contains(@href,'forgot')]");
    private final By errorMessage = By.xpath("//div[contains(@class,'alert-danger')]");
    private ActionDriver actionDriver;

    public LoginPage() {
        this.actionDriver = BaseClass.getActionDriver();
    }

    public LoginPage enterUserName(String userName) {
        actionDriver.enterText(userId, userName);
        ExtentManager.logStepWithScreenshot("Entered the userName", TestStatus.INFO);
        actionDriver.resetBorder(userId);
        return this;
    }

    public LoginPage enterPassword(String pass) {
        actionDriver.enterText(password, pass);
        ExtentManager.logStepWithScreenshot("Entered the password", TestStatus.INFO);
        actionDriver.resetBorder(password);
        return this;
    }

    public LoginPage forgotThePassword() {
        actionDriver.click(forgotPassword, "'forgot your password?' link");
        ExtentManager.logStepWithScreenshot("Clicked on 'forgot your password?' link", TestStatus.INFO);
        return this;
    }

    public String getErrorMessage() {
        return actionDriver.getText(errorMessage);
    }

    public void verifyErrorMessage() {

        Assert.assertTrue(actionDriver.verifyContainText(errorMessage, BaseClass.getConfig().getProperty("loginError")));
    }

    public HomePage clickLogin() {
        actionDriver.click(loginButton, "Login button");

        return new HomePage();
    }

    public void verifyLoginButton() {
        Assert.assertTrue(actionDriver.isDisplayed(loginButton));

    }

    public void verifyCurrentHomePageUrl() {
        String currentURL = actionDriver.getCurrentURL();
        Assert.assertTrue(actionDriver.compareUrlText(currentURL, siteUrl));
    }

}
