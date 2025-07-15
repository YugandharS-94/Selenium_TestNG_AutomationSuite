package com.test.support.actiondriver;

import com.test.support.base.BaseClass;
import com.test.support.utilities.ExtentManager;
import com.test.support.utilities.TestStatus;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionDriver {

    public static final Logger logger = BaseClass.logger;
    private WebDriver driver;
    private WebDriverWait wait;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getConfig().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("WebDriver instance is created.");
    }

    // Method to click an element
    public void click(By by) {
        String elementDescription = getElementDescription(by);
        try {
            applyBorder(by, "green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
//            ExtentManager.logStep("clicked an element: " + elementDescription);
            logger.info("clicked an element--> {}", elementDescription);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error(() -> "Unable to click element:" + e.getMessage());
            ExtentManager.logFailure(driver, "Unable to click on element: " + elementDescription + "_unable to click", TestStatus.FAIL);

        }
    }

    public void click(By by, String message) {
        String elementDescription = getElementDescription(by);
        try {
            applyBorder(by, "green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            String logMessage = "Clicked on " + message;
            ExtentManager.logStep(logMessage, TestStatus.INFO);
            logger.info("clicked an element--> {}", elementDescription);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to click element:" + e.getMessage());
            ExtentManager.logFailure(driver, "Unable to click on element: " + elementDescription + "_unable to click", TestStatus.FAIL);

        }
    }

    // Method to enter text into an input field --Avoid Code Duplication - fix the
    // multiple calling method
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            // driver.findElement(by).clear();
            // driver.findElement(by).sendKeys(value);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info(() -> "Entered text on " + getElementDescription(by) + " --> " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            ExtentManager.logFailure(driver, "Unable to enter the value: " + e.getMessage(), TestStatus.FAIL);
            logger.error("Unable to enter the value: {}", e.getMessage());
        }
    }

    // Method to get text from an input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            return driver.findElement(by).getText();
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to get the text: {}", e.getMessage());
            return "";
        }
    }

    // Method to compare Two text -- changed the return type
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                applyBorder(by, "green");
                logger.info(() -> "Texts are Matching:" + actualText + " equals " + expectedText);
                ExtentManager.logStepWithScreenshot(driver, "Compare Text",
                        "Text Verified Successfully! " + actualText + " equals " + expectedText);
                return true;
            } else {
                applyBorder(by, "red");
                logger.error(() -> "Texts are not Matching:" + actualText + " not equals " + expectedText);
                ExtentManager.logFailure(driver, "Text Comparison Failed!",
                        "Text Comparison Failed! " + actualText + " not equals " + expectedText);
                return false;
            }
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to compare Texts: {}", e.getMessage());
        }
        return false;
    }

    // Method to compare Two text -- changed the return type
    public boolean compareUrlText(String actualText, String expectedText) {
        try {
            if (expectedText.equals(actualText)) {
                logger.info(() -> "Url's are Matching:" + actualText + " equals " + expectedText);
                ExtentManager.logStep("Url's Verified Successfully! " + actualText + " equals " + expectedText, TestStatus.PASS);
                return true;
            } else {

                logger.error(() -> "Url's are not Matching:" + actualText + " not equals " + expectedText);
                ExtentManager.logFailureAPI(
                        "Url Comparison Failed! " + actualText + " not equals " + expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("Unable to compare Url's: {}", e.getMessage());
        }
        return false;
    }

    // Method to compare Two text -- changed the return type
    public boolean verifyContainText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (actualText.contains(expectedText)) {
                applyBorder(by, "green");
                logger.info(() -> "Texts are Matching: " + actualText + " contains " + expectedText);
                ExtentManager.logStepWithScreenshot(driver,
                        "Text Verified Successfully!", TestStatus.PASS);
                return true;
            } else {
                applyBorder(by, "red");
                logger.error(() -> "Texts are not Matching:" + actualText + " not contains " + expectedText);
                ExtentManager.logFailure(driver,
                        "Text Comparison Failed! " + actualText + " not equals " + expectedText, TestStatus.FAIL);
                return false;
            }
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to compare Texts: {}", e.getMessage());
            ExtentManager.logFailure(driver,
                    "Unable to verify the message " + e.getMessage(), TestStatus.FAIL);
        }
        return false;
    }

    /*
     * Method to check if an element is displayed public boolean isDisplayed(By by)
     * { try { waitForElementToBeVisible(by); boolean isDisplayed =
     * driver.findElement(by).isDisplayed(); if (isDisplayed) {
     * System.out.println("Element is Displayed"); return isDisplayed; } else {
     * return isDisplayed; } } catch (Exception e) {
     * System.out.println("Element is not displayed:"+e.getMessage()); return false;
     * } }
     */

    // Simplified the method and remove redundant conditions
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            logger.info(() -> "Element is displayed {}" + getElementDescription(by));
            ExtentManager.logStepWithScreenshot(driver, "Element is displayed: " + getElementDescription(by), TestStatus.PASS);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Element is not displayed: {}", e.getMessage());
            ExtentManager.logFailure(driver, "Element is not displayed: " + getElementDescription(by), TestStatus.FAIL);
            return false;
        }
    }

    // Wait for the page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully.");
        } catch (Exception e) {
            logger.error(() -> "Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
        }
    }

    // Scroll to an element -- Added a semicolon ; at the end of the script string
    public void scrollToElement(By by) {
        try {
            applyBorder(by, "green");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to locate element: {}", e.getMessage());
        }
    }

    // Wait for Element to be clickable
    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("element is not clickable: {}", e.getMessage());
        }
    }

    // Wait for Element to be Visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible: {}", e.getMessage());
        }
    }

    // Method to get the description of an element using By locator
    public String getElementDescription(By locator) {
        // Check for null driver or locator to avoid NullPointerException
        if (driver == null) {
            return "Driver is not initialized.";
        }
        if (locator == null) {
            return "Locator is null.";
        }

        try {
            // Find the element using the locator
            WebElement element = driver.findElement(locator);

            // Get element attributes
            String name = element.getDomProperty("name");
            String id = element.getDomProperty("id");
            String text = element.getText();
            String className = element.getDomProperty("class");
            String placeholder = element.getDomProperty("placeholder");

            // Return a description based on available attributes
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with ID: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 50);
            } else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            } else if (isNotEmpty(placeholder)) {
                return "Element with placeholder: " + placeholder;
            } else {
                return "Element located using: " + locator.toString();
            }
        } catch (Exception e) {
            // Log exception for debugging
            logger.error(e.getMessage()); // Replace with a logger in a real-world scenario
            return "Unable to describe element due to error: " + e.getMessage();
        }
    }

    // Utility method to check if a string is not null or empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long strings
    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    // Utility Method to Border an element
    public void applyBorder(By by, String color) {
        try {
            // Locate the element
            WebElement element = driver.findElement(by);
            // Apply the border
            String script = "arguments[0].style.border='3px solid " + color + "'";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script, element);
            logger.info(() -> "Applied the border with color " + color + " to element: " + getElementDescription(by));
        } catch (Exception e) {
            logger.warn("Failed to apply the border to an element: {}", getElementDescription(by), e);
        }
    }

    public void resetBorder(By by) {
        try {
            WebElement element = driver.findElement(by);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border=''", element);
        } catch (Exception e) {
            logger.warn("Failed to reset the border from the element {}", getElementDescription(by), e);
        }
    }

    // ===================== Select Methods =====================

    // Method to select a dropdown by visible text
    public void selectByVisibleText(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByVisibleText(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value: {}", value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error(() -> "Unable to select dropdown value: " + value, e);
        }
    }

    // Method to select a dropdown by value
    public void selectByValue(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByValue(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value by actual value: {}", value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error(() -> "Unable to select dropdown by value: " + value, e);
        }
    }

    // Method to select a dropdown by index
    public void selectByIndex(By by, int index) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByIndex(index);
            applyBorder(by, "green");
            logger.info("Selected dropdown value by index: {}", index);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error(() -> "Unable to select dropdown by index: " + index, e);
        }
    }

    // Method to get all options from a dropdown
    public List<String> getDropdownOptions(By by) {
        List<String> optionsList = new ArrayList<>();
        try {
            WebElement dropdownElement = driver.findElement(by);
            Select select = new Select(dropdownElement);
            for (WebElement option : select.getOptions()) {
                optionsList.add(option.getText());
            }
            applyBorder(by, "green");
            logger.info(() -> "Retrieved dropdown options for {}" + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to get dropdown options: {}", e.getMessage());
        }
        return optionsList;
    }

    // ===================== JavaScript Utility Methods =====================

    // Method to click using JavaScript
    public void clickUsingJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            applyBorder(by, "green");
            logger.info(() -> "Clicked element using JavaScript: {}" + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to click using JavaScript", e);
        }
    }

    // Method to scroll to the bottom of the page
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        logger.info("Scrolled to the bottom of the page.");
    }

    // Method to highlight an element using JavaScript
    public void highlightElementJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
            logger.info(() -> "Highlighted element using JavaScript: {}" + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to highlight element using JavaScript", e);
        }
    }

    // ===================== Window and Frame Handling =====================

    // Method to switch between browser windows
    public void switchToWindow(String windowTitle) {
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(windowTitle)) {
                    logger.info("Switched to window: {}", windowTitle);
                    return;
                }
            }
            logger.warn(() -> "Window with title " + windowTitle + " not found.");
        } catch (Exception e) {
            logger.error("Unable to switch window", e);
        }
    }

    // Method to switch to an iframe
    public void switchToFrame(By by) {
        try {
            driver.switchTo().frame(driver.findElement(by));
            logger.info(() -> "Switched to iframe: {}" + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to switch to iframe", e);
        }
    }

    // Method to switch back to the default content
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.info("Switched back to default content.");
    }

    // ===================== Alert Handling =====================

    // Method to accept an alert popup
    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            logger.info("Alert accepted.");
        } catch (Exception e) {
            logger.error("No alert found to accept", e);
        }
    }

    // Method to dismiss an alert popup
    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            logger.info("Alert dismissed.");
        } catch (Exception e) {
            logger.error("No alert found to dismiss", e);
        }
    }

    // Method to get alert text
    public String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            logger.error("No alert text found", e);
            return "";
        }
    }

    // ===================== Browser Actions =====================

    public void refreshPage() {
        try {
            driver.navigate().refresh();
            ExtentManager.logStep("Page refreshed successfully.");
            logger.info("Page refreshed successfully.");
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to refresh page", "refresh_page_failed");
            logger.error("Unable to refresh page: {}", e.getMessage());
        }
    }

    public String getCurrentURL() {
        try {
            String url = driver.getCurrentUrl();
            ExtentManager.logStep("Current URL fetched: " + url, TestStatus.INFO);
            logger.info("Current URL fetched: {}", url);
            return url;
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "get_current_url_failed", TestStatus.FAIL);
            logger.error("Unable to fetch current URL: {}", e.getMessage());
            return null;
        }
    }


    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
            ExtentManager.logStep("Browser window maximized.");
            logger.info("Browser window maximized.");
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to maximize window", "maximize_window_failed");
            logger.error(() -> "Unable to maximize window: " + e.getMessage());
        }
    }

    // ===================== Advanced WebElement Actions =====================

    public void moveToElement(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(by)).perform();
            ExtentManager.logStep("Moved to element: " + elementDescription);
            logger.info("Moved to element --> {}", elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to move to element", elementDescription + "_move_failed");
            logger.error("Unable to move to element: {}", e.getMessage());
        }
    }

    public void dragAndDrop(By source, By target) {
        String sourceDescription = getElementDescription(source);
        String targetDescription = getElementDescription(target);
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
            ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
            logger.info(() -> "Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to drag and drop", sourceDescription + "_drag_failed");
            logger.error("Unable to drag and drop: {}", e.getMessage());
        }
    }

    public void doubleClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.doubleClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Double-clicked on element: " + elementDescription);
            logger.info("Double-clicked on element --> {}", elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to double-click element",
                    elementDescription + "_doubleclick_failed");
            logger.error("Unable to double-click element: {}", e.getMessage());
        }
    }

    public void rightClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.contextClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Right-clicked on element: " + elementDescription);
            logger.info("Right-clicked on element --> {}", elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to right-click element",
                    elementDescription + "_right-click_failed");
            logger.error("Unable to right-click element: {}", e.getMessage());
        }
    }

    public void sendKeysWithActions(By by, String value) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(driver.findElement(by), value).perform();
            ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);
            logger.info(() -> "Sent keys to element --> " + elementDescription + " | Value: " + value);

        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to send keys", elementDescription + "_sendkeys_failed");
            logger.error("Unable to send keys to element: {}", e.getMessage());
        }
    }

    public void clearText(By by) {
        String elementDescription = getElementDescription(by);
        try {
            driver.findElement(by).clear();
            ExtentManager.logStep("Cleared text in element: " + elementDescription);
            logger.info("Cleared text in element --> {}", elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(driver, "Unable to clear text ", elementDescription + "_clear_failed");
            logger.error("Unable to clear text in element: {}", e.getMessage());
        }
    }

    // Method to upload a file
    public void uploadFile(By by, String filePath) {
        try {
            driver.findElement(by).sendKeys(filePath);
            applyBorder(by, "green");
            logger.info("Uploaded file: {}", filePath);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to upload file: {}", e.getMessage());
        }
    }


}
