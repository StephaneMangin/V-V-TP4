package fr.istic.vv.usecases;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import fr.istic.vv.mdms.AbstractMDMSTest;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.junit.Assert.*;

public class AppearanceTests {
    private static WebDriver driver;
    private static String baseUrl;
    private static StringBuffer verificationErrors = new StringBuffer();
    private static WebDriver.Navigation nav;

    private static void logout() throws Exception {
        driver.get(baseUrl + "/signout");
    }

    private static void login(String name, String password) throws Exception {
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys(name);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button.btn.btn-success")).click();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Bypass => Error in expression; ':' found after identifier "progid".
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.DefaultCssErrorHandler").setLevel(Level.OFF);
        // Selected generic driver to spedd up test processing
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseUrl);
        nav = driver.navigate();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        logout();
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Test
    public void testTitleAssert() throws Exception {
        assertEquals("MdMS - Diversify", driver.getTitle());
    }

    @Test
    public void testLoginNameAssert() throws Exception {
        login("admin", "admin");
        System.out.println("logged in");
        for (int second = 0; second < 5; second++) {
            System.out.println("pass : " + second);
            try {
                if (isElementPresent(By.linkText("admin"))) {
                    assertTrue(true);
                }
                System.out.println("not found");
            } catch (Exception e) {
                System.out.printf("exception");
            }
            Thread.sleep(1000);
        }
        fail("timeout");
    }

}
