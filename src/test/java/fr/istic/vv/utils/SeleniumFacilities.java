package fr.istic.vv.utils;

import fr.istic.vv.mdms.AbstractMDMSTest;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by smangin on 28/10/15.
 *
 * This helper class allow to keep a same instance of firefox
 */
public abstract class SeleniumFacilities extends AbstractMDMSTest {

    protected static WebDriver driver;
    protected static String baseUrl;
    protected static WebDriver.Navigation nav;

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    @BeforeClass
    public static void setUpClass() {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseUrl);
        nav = driver.navigate();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        driver.quit();
    }

}
