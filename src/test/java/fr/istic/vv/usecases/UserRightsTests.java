package fr.istic.vv.usecases;

import com.gargoylesoftware.htmlunit.WebClient;
import fr.istic.vv.utils.LoginFacilities;
import org.junit.*;
import org.openqa.selenium.*;

import static org.junit.Assert.*;

public class UserRightsTests extends LoginFacilities {

    @Test
    public void testEditNoLogin() throws Exception {
        driver.findElement(By.xpath("(//a[contains(text(),'Edit')])[1]")).click();
        assertEquals("×\nYou are not allowed to access /edit/article_2 resource", driver.findElement(By.xpath("//div[2]/div")).getText());
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
    public void testArticleCreateWithoutTitle() throws Exception {
        login("admin", "admin");
        nav.to(baseUrl + "/add");
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("");
        driver.findElement(By.id("save")).click();
        assertEquals("×\nWarning! You must give a title and some content in order to add an article :)", driver.findElement(By.id("form-alert")).getText());
    }

    @Test
    public void testArticleCreateWithoutContent() throws Exception {
        login("admin", "admin");
        nav.to(baseUrl + "/add");
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("Example");
        driver.findElement(By.id("save")).click();
        assertEquals("×\nWarning! You must give a title and some content in order to add an article :)", driver.findElement(By.id("form-alert")).getText());
    }

    @Test
    public void testArticleCreate() throws Exception {
        login("admin", "admin");

        nav.to(baseUrl + "/add");
        driver.findElement(By.id("title")).clear();
        driver.findElement(By.id("title")).sendKeys("Example");
        // Mandatory to bypass selectors to insert content into codeMirror
        ((JavascriptExecutor) driver).executeScript("document.editor.setValue('This is my content')");
        driver.findElement(By.id("save")).click();
        assertTrue(isElementPresent(By.xpath("//h2[contains(text(), 'Example')]")));
        assertTrue(isElementPresent(By.xpath("//p[contains(text(), 'This is my content')]")));
    }

    @Test
    public void testArticleDelete() throws Exception {
        login("admin", "admin");
        driver.findElement(By.xpath("//a[@href='delete/article_0']")).click();
        assertEquals("×\nArticle \"article_0\" deleted successfully", driver.findElement(By.className("alert-success")).getText());
    }

}
