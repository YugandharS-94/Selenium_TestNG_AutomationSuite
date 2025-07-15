package com.test.support.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.test.support.base.BaseClass;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager extends BaseClass {

    private static final String DATE_PATTERN = "dd-MMM-yyyy";
    private static final String DATETIME_PATTERN = "dd-MMM-yyyy_HH-mm-ss";
    private static final String userDir = System.getProperty("user.dir");
    static String folderName = "";
    static String fileName = "";
    static String screenshotFolder = "";
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // Initialize the Extent Report
    public static synchronized ExtentReports getReporter() {
        String date = new SimpleDateFormat(DATE_PATTERN).format(new Date());
        folderName = "/src/test/resources/ExtentReport/" + date;
        String dateTime = new SimpleDateFormat(DATETIME_PATTERN).format(new Date());
        fileName = "Report_" + dateTime + ".html";
        fileName = "ExtentReport-Latest.html";
        screenshotFolder = folderName + "/images/";
        File folder = new File("./" + folderName);
        if (!folder.exists()) {
            folder.mkdir();

        }

        if (extent == null) {
            String reportPath = "./" + folderName.concat("/").concat(fileName);
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            String siteName = getConfig().getProperty("site");
            if (siteName.equals("entity")) {
                spark.config().setReportName("Entity Portal Test Report");
            } else if (siteName.equals("ops")) {
                spark.config().setReportName("OpsConsole Test Report");
            }
            spark.config().setDocumentTitle("Jahia Automation Test Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            // Adding system information
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));

        }

        return extent;
    }

    // Start the Test
    public static synchronized ExtentTest startTest(String testName) {
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    // End a Test
    public static synchronized void endTest() {
        getReporter().flush();
    }

    // Get Current Thread's test
    public static synchronized ExtentTest getTest() {
        return test.get();
    }

    // Method to get the name of the current test
    public static String getTestName() {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            return currentTest.getModel().getName();
        } else {
            return "No test is currently active for this thread";
        }
    }


    // Log a step
    public static void logStep(String logMessage, TestStatus status) {
        if (status.equals(TestStatus.INFO)) {
            getTest().info(logMessage);
        } else if (status.equals(TestStatus.PASS)) {
            getTest().pass(logMessage);

        }
    }

    public static void logStep(String logMessage) {

        getTest().info(logMessage);

    }

    public static void logStep(WebDriver driver, String logMessage) {
        attachScreenshot(driver, logMessage);
        getTest().info(logMessage);
    }

    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
        getTest().pass(logMessage);
        //Screenshot method
        attachScreenshot(driver, screenShotMessage);

    }

    // Log a step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String screenShotMessage, TestStatus status) {
//        getTest().pass(logMessage);
        // Screenshot method
        attachScreenshotImg(driver, screenShotMessage, status);

    }

    // Log a step validation with screenshot
    public static void logStepWithScreenshot(String screenShotMessage, TestStatus status) {
//        getTest().pass(logMessage);
        // Screenshot method
        attachScreenshotImg(BaseClass.getDriver(), screenShotMessage, status);

    }


    // Log a step validation for API
    public static void logStepValidationForAPI(String logMessage) {
        getTest().pass(logMessage);
    }

    // Log a Failure
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMessage);
        // Screenshot method
        attachScreenshot(driver, screenShotMessage);
    }

    public static void logFailure(WebDriver driver, String logMessage, TestStatus status) {
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
//        getTest().fail(colorMessage);
        // Screenshot method
        attachScreenshotImg(driver, colorMessage, status);
    }

    // Log a Failure for API
    public static void logFailureAPI(String logMessage) {
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMessage);
    }

    // Log a skip
    public static void logSkip(String logMessage) {
        String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
        getTest().skip(colorMessage);
    }

    // Take a screenshot with date and time in the file
    public static synchronized String takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        // Format date and Time for file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        // Saving the screenshot to a file
        String destPath = "/src/test/resources/screenshots/" + screenshotName + "_"
                          + timeStamp + ".png";

        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert screenshot to Base64 fir embedding in the Report
        String base64Format = convertToBase64(src);
        return base64Format;
    }


    // Take a screenshot with date and time in the file based on boolean flag
    public static synchronized String takeScreenshotNormal(WebDriver driver, String screenshotName, Boolean iBase64) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);


        if (Boolean.FALSE.equals(iBase64)) {
            // Format date and Time for file name
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = screenshotName + "_" + timeStamp + ".png";
            // Saving the screenshot to a file
            String destPath = "./" + screenshotFolder + fileName;

            File finalPath = new File(destPath);
            try {
                FileUtils.copyFile(src, finalPath);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
//            System.out.println(destPath);
//            System.out.println("Normal Image captured");
            return fileName;
        } else {
// Convert screenshot to Base64 fir embedding in the Report
//            String base64Format = convertToBase64(src);
            //            System.out.println("Base64 image captured");
            return ts.getScreenshotAs(OutputType.BASE64);
        }
    }

    // Convert screenshot to Base64 format
    public static String convertToBase64(File screenShotFile) {
        String base64Format = "";
        // Read the file content into a byte array
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
            base64Format = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {

            logger.error(e.getMessage());
        }
        return base64Format;
    }

    // Attach screenshot to report using Base64
    public static synchronized void attachScreenshot(WebDriver driver, String message) {
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestName());
            getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
                    .createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot:" + message);
            e.printStackTrace();
        }
    }

    public static synchronized void attachScreenshotImg(WebDriver driver, String message, TestStatus status) {
        boolean isBase64 = Boolean.parseBoolean(getConfig().getProperty("isBase64Image"));
//        System.out.println("Is Base64 : " + isBase64);
//        System.out.println("Screenshot path : " + screenshotFolder);
        try {
            String screenShotName = takeScreenshotNormal(driver, getTestName(), isBase64);
//            System.out.println("Screenshotname : " + screenShotName);
            Media image;
            if (isBase64 == false) {
//                System.out.println("Attaching normal screenshot");
                image = MediaEntityBuilder
                        .createScreenCaptureFromPath(screenshotFolder + screenShotName).build();
            } else {
//                System.out.println("Attaching base64 screenshot");
                image = MediaEntityBuilder
                        .createScreenCaptureFromBase64String(screenShotName).build();
            }
            if (status.equals(TestStatus.INFO) && message != null) {
                getTest().info(message, image);
            } else if (status.equals(TestStatus.PASS) && message != null) {
                getTest().pass(message, image);
            } else if (status.equals(TestStatus.FAIL) && message != null) {
                getTest().fail(message, image);

            }
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot:" + message);
            e.printStackTrace();
        }
    }

    private static WebDriver getRegisterDriver() {

        return driverMap.get(Thread.currentThread().getId());
    }

    // Register WebDriver for current Thread
    public static void registerDriver(WebDriver driver) {
        driverMap.put(Thread.currentThread().getId(), driver);
    }


}
