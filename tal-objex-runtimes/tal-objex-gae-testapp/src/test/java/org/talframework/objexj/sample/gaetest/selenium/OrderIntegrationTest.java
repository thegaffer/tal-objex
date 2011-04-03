/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
