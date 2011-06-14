/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.gaetest.selenium;

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
public class OrderIntegrationTest {
    
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
        selenium.open("/sampleApp/newOrder");
        
        selenium.type("name", "Test Company");
        selenium.type("account", Long.toString(System.currentTimeMillis()));
        selenium.click("submit");
        selenium.waitForPageToLoad("30000");
        
        createItem("Acer e345", "Laptop", "1", "no", "349.99", "GBP");
        createItem("HP 847", "22\" Monitor", "2", "no", "179.99", "GBP");
        
        // Save
        selenium.click("link=Save");
        selenium.waitForPageToLoad("30000");
        
        // Re-open
        selenium.click("link=Open");
        selenium.waitForPageToLoad("30000");
        
        createItem("TravelMate Lite", "Laptop Bag", "1", "no", "19.99", "GBP");
        
        selenium.click("link=Save");
        selenium.waitForPageToLoad("30000");
    }
    
    /**
     * Helper to create a category, assumes in viewStock page
     */
    private void createItem(String name, String description, String quantity, String measure, String price, String currency) {
        selenium.click("link=New Item");
        selenium.waitForPageToLoad("30000");
        selenium.type("name", name);
        selenium.type("description", description);
        selenium.type("quantity", quantity);
        selenium.type("measure", measure);
        selenium.type("price", price);
        selenium.type("currency", currency);
        selenium.click("submit");
        selenium.waitForPageToLoad("30000");
        
        // TODO: Tests
        
    }
    
}
