package tests.entityportal;

import com.mdp.jahia.base.BaseClass;
import org.testng.annotations.Test;

public class DummyTest extends BaseClass {

    @Test
    public void dummyTest() {
        System.out.println("dummyTest");
        System.out.println(getDriver().getTitle());
        System.out.println("CurrentThread: " + Thread.currentThread().getId());

    }

    @Test
    public void dummyTest1() {
        System.out.println("dummyTest1");
        System.out.println(getDriver().getTitle());
        System.out.println("CurrentThread: " + Thread.currentThread().getId());

    }
}
