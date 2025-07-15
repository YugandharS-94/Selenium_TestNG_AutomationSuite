package tests.entityportal;

import com.mdp.jahia.base.BaseClass;
import org.testng.annotations.Test;

public class LoginPageTest1 extends BaseClass {
//	private LoginPage loginPage;
//	private HomePage homePage;
//
//	@BeforeMethod
//	public void pageSetup() {
//		loginPage = new LoginPage();
//		homePage = new HomePage();
//
//	}
	@Test
	public void validLoginTest2() {
		loginPage.navigateToUrl();
		loginPage.enterUserName("Hello2")
		.enterPassword("World2").clickLogin();
		loginPage.verifyErrorMessage();
	}
	@Test
	public void validLoginTest3() {
		loginPage.enterUserName("Hello3")
				.enterPassword("World3").clickLogin();
		loginPage.verifyErrorMessage();
	}
}
