package fr.istic.vv.usecases;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class UserRightsTests {
    private static WebDriver driver;
    private static String baseUrl;
    private static boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();
    private static WebDriver.Navigation nav;

    private void login(String name, String password) throws Exception {
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys(name);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button.btn.btn-success")).click();
        for (int second = 0;; second++) {
            if (second >= 30) {
                fail("timeout");
            }
            try {
                if (isElementPresent(By.linkText("admin"))) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        driver = new HtmlUnitDriver(true);
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(baseUrl);
        nav = driver.navigate();
    }

    @Before
    public void setUp() throws Exception{

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
    public void testEditNoLogin() throws Exception {
        driver.findElement(By.xpath("(//a[contains(text(),'Edit')])[2]")).click();
        assertEquals("×\nYou are not allowed to access /edit/article_1 resource", driver.findElement(By.xpath("//div[2]/div")).getText());
    }

    @Test
    public void testUserRightsWrongPassword() throws Exception {
        login("admin", "truc");
        assertEquals("×\nWrong login and/or password", driver.findElement(By.xpath("//div[2]/div")).getText());
    }

    @Test
    public void testUserRightsWrongLogin() throws Exception {
        login("machin", "bidule");
        assertEquals("×\nWrong login and/or password", driver.findElement(By.xpath("//div[2]/div")).getText());
    }

    @Test
    public void testUserRightsLoginAssert() throws Exception {
        login("admin", "admin");
        driver.findElement(By.cssSelector("button.btn.btn-success")).click();
        assertEquals("Hi admin! You are now signed in.\nYou will be automatically redirected to index in a few seconds...", driver.findElement(By.cssSelector("p")).getText());
    }

    @Test
    public void testArticleCreateWithoutTitle() throws Exception {
        login("admin", "admin");
        driver.findElement(By.linkText("Add article")).click();
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("");
        driver.findElement(By.id("save")).click();
        assertEquals("×\nWarning! You must give a title and some content in order to save an article :)", driver.findElement(By.id("form-alert")).getText());
    }

    @Test
    public void testArticleCreateWithoutContent() throws Exception {
        login("admin", "admin");
        driver.findElement(By.linkText("Add article")).click();
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("Example");
        driver.findElement(By.id("save")).click();
        assertEquals("×\nWarning! You must give a title and some content in order to save an article :)", driver.findElement(By.id("form-alert")).getText());
    }

    @Test
    public void testArticleCreate() throws Exception {
        login("admin", "admin");
        driver.findElement(By.linkText("Add article")).click();
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("Example");
        driver.findElement(By.cssSelector("div > textarea")).clear();
        driver.findElement(By.cssSelector("div > textarea")).sendKeys("My content");
        driver.findElement(By.id("save")).click();
        assertTrue(isElementPresent(By.xpath("//h2[contains(text(), 'Example')]")));
        assertTrue(isElementPresent(By.xpath("//p[contains(text(), 'My content')]")));
    }

    @Test
    public void testArticleDelete() throws Exception {
        login("admin", "admin");
        driver.findElement(By.xpath("(//a[contains(text(),'Edit')])[2]")).click();
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("");
        driver.findElement(By.id("save")).click();
        assertEquals("×\nWarning! You must give a title and some content in order to save an article :)", driver.findElement(By.id("form-alert")).getText());
    }

}
