package fr.istic.vv.usecases;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FunctionalNoLoginUseCasesTest {
    private static WebDriver driver;
    private static String baseUrl;
    private static boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();
    private static WebDriver.Navigation nav;

    @BeforeClass
    public static void setUpClass() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseUrl);
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
    public void testLoginAssert() throws Exception {
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys("admin");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.cssSelector("button.btn.btn-success")).click();
        assertEquals("Hi admin! You are now signed in.\nYou will be automatically redirected to index in a few seconds...", driver.findElement(By.cssSelector("p")).getText());
    }

    @Test
    public void testEditNoLogin() throws Exception {
        driver.get(baseUrl + "/");
        driver.findElement(By.xpath("(//a[contains(text(),'Edit')])[2]")).click();
        assertEquals("×\nYou are not allowed to access /edit/article_1 resource", driver.findElement(By.xpath("//div[2]/div")).getText());
    }
}
