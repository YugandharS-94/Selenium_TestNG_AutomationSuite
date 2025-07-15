package com.test.support.listener;

import com.test.support.base.BaseClass;
import com.test.support.utilities.ExtentManager;
import com.test.support.utilities.RetryAnalyzer;
import com.test.support.utilities.TestStatus;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Listeners implements ITestListener, IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentManager.startTest(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.logStep("Test Passed Successfully!", TestStatus.PASS);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.logFailure(BaseClass.getDriver(),"Test Failed!", TestStatus.FAIL);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.logSkip("Test Skipped!");
    }

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getReporter();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.endTest();
    }

}
