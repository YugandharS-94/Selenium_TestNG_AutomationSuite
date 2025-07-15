package com.test.support.pages.entity;

import com.test.support.actiondriver.ActionDriver;
import com.test.support.base.BaseClass;
import org.openqa.selenium.By;
import org.testng.Assert;

public class HomePage {
    private final By logOutDropDown = By.id("menuLabel");
//    private final By logOutButton = By.xpath("//a[contains(@href,'logout')]/i");
    private final By logOutButton = By.xpath("//a[normalize-space()='Logout']");

    private final By helpLink = By.xpath("//a[contains(text(),'Show Help')]");
    private final By helpDialogBox = By.xpath("//div[@id='HelpDialog']//div[@class='modal-content']");
    private ActionDriver actionDriver;

    public HomePage() {
        this.actionDriver = BaseClass.getActionDriver();
    }

    public HomePage clickOnLogOutDropDown() {
        actionDriver.click(logOutDropDown);
        actionDriver.resetBorder(logOutDropDown);
        return this;
    }
    private HomePage verifyLogoutButton(){
        actionDriver.isDisplayed(logOutButton);
        return this;
    }
    public LoginPage clickOnLogoutButton() {
        verifyLogoutButton();
        actionDriver.click(logOutButton, "logout button");
        return new LoginPage();
    }

    public void checkLogoutButton() {
        Assert.assertTrue(actionDriver.isDisplayed(logOutButton));
    }
}
