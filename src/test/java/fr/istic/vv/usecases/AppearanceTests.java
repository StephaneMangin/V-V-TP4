package fr.istic.vv.usecases;

import fr.istic.vv.utils.LoginFacilities;
import org.junit.*;
import org.openqa.selenium.*;

import static org.junit.Assert.*;

public class AppearanceTests extends LoginFacilities {

    /**
     * Tests the title presence.
     *
     * @type Functional
     * @oracle Should be equals to "MdMS - Diversify"
     * @passed Yes
     */
    @Test
    public void testTitleAssert() throws Exception {
        assertEquals("MdMS - Diversify", driver.getTitle());
    }

    /**
     * Tests the login name presence after aythentication.
     *
     * @type Functional
     * @oracle Should return true
     * @passed Yes
     */
    @Test
    public void testLoginNameAssert() throws Exception {
        login("admin", "admin");
        assertTrue(isElementPresent(By.linkText("admin")));
    }

}
