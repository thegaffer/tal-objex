package org.talframework.objexj.gaetest.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * This class contains the tests for the Stock part of the
 * application.
 *
 * @author Tom Spencer
 */
public class StockIntegrationTest {
    
    private Selenium selenium;

    @Before
    public void setup() {
        selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://127.0.0.1:8888/");
        selenium.start();
    }
    
    @After
    public void cleanUp() {
        if( selenium != null ) selenium.stop();
    }
    
    @Test
    public void basic() {
        selenium.open("/sampleApp/viewStock");
        
        String root = selenium.getCookieByName("category");
        
        createCategory("Cat1", "This is the first Category");
        selenium.createCookie("category=" + root, "path=/sampleApp/");
        createCategory("Cat2", "This is the second Category");
        selenium.createCookie("category=" + root, "path=/sampleApp/");
        
        createProduct("Product1", "Test1", "99.99", "British Pound");
        createProduct("Product2", "Test2", "29.99", "British Pound");
    }
    
    /**
     * Helper to create a category, assumes in viewStock page
     */
    private String createCategory(String name, String description) {
        selenium.click("link=New Category");
        selenium.waitForPageToLoad("30000");
        selenium.type("name", name);
        selenium.type("description", description);
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
        String ret = selenium.getCookieByName("category");
        
        // TODO: Tests
        
        return ret;
    }
    
    private String createProduct(String name, String description, String price, String currency) {
        selenium.click("link=New Product");
        selenium.waitForPageToLoad("30000");
        selenium.type("name", name);
        selenium.type("description", description);
        selenium.type("price", price);
        selenium.select("currency", currency);
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
        String ret = selenium.getCookieByName("product");
        
        // TODO: Tests
        
        return ret;
    }
}
