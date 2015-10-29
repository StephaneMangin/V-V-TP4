package fr.istic.vv.utils;

import org.junit.Before;
import org.openqa.selenium.*;

/**
 * Created by smangin on 28/10/15.
 */
public abstract class LoginFacilities extends SeleniumFacilities {

    public void logout() {
        nav.to(baseUrl + "/signout");
        driver.manage().deleteAllCookies();
    }

    public void login(String name, String password) {
        WebElement login = driver.findElement(By.name("login"));
        WebElement pwd = driver.findElement(By.name("password"));
        login.clear();
        login.sendKeys(name);
        pwd.clear();
        pwd.sendKeys(password);
        driver.findElement(By.cssSelector("button.btn-success")).click();
    }

    @Before
    public void setUp() {
        logout();
    }

}
