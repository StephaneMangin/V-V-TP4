package fr.istic.vv.usecases;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FunctionalLoginUseCasesTest {
    private static WebDriver driver;
    private static String baseUrl;
    private static boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();
    private static WebDriver.Navigation nav;

    private static void login() throws Exception {
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys("admin");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.cssSelector("button.btn.btn-success")).click();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseUrl);
        login();
        nav = driver.navigate();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        nav.to(baseUrl + "/signout");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
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

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    @Test
    public void testTitleAssert() throws Exception {
        assertEquals("MdMS - Diversify", driver.getTitle());
    }

    @Test
    public void testLogin() throws Exception {
        login();
    }

    @Test
    public void testLoginName() throws Exception {
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys("admin");
        driver.findElement(By.cssSelector("button.btn.btn-success")).click();
        assertTrue(isElementPresent(By.linkText("admin")));
    }

}
