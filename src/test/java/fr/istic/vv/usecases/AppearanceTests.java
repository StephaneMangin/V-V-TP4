package fr.istic.vv.usecases;

import fr.istic.vv.utils.LoginFacilities;
import org.junit.*;
import org.openqa.selenium.*;

import static org.junit.Assert.*;

public class AppearanceTests extends LoginFacilities {

    @Test
    public void testTitleAssert() throws Exception {
        assertEquals("MdMS - Diversify", driver.getTitle());
    }

    @Test
    public void testLoginNameAssert() throws Exception {
        login("admin", "admin");
        assertTrue(isElementPresent(By.linkText("admin")));
    }

}
