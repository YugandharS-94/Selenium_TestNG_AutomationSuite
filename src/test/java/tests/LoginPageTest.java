package tests.entityportal;

import com.mdp.jahia.base.BaseClass;
import com.mdp.jahia.utilities.EncryptDecrypt;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {
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
    public void validLoginTest() {
        loginPage.navigateToUrl();
        loginPage.enterUserName("Hello")
                .enterPassword("World").clickLogin();
        loginPage.verifyErrorMessage();
    }

    @Parameters({"username", "password"})
    @Test
    public void validLoginTest1(String username, String password) {
        loginPage.navigateToUrl();
        loginPage.enterUserName(EncryptDecrypt.decrypt(username))
                .enterPassword(EncryptDecrypt.decrypt(password)).verifyLoginButton();
        loginPage.clickLogin();
        homePage.clickOnLogOutDropDown()
                .clickOnLogoutButton();
        loginPage.verifyCurrentHomePageUrl();

    }
}
