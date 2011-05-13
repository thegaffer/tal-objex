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

package org.talframework.objexj.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.container.middleware.SingletonContainerStore;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.object.DefaultObjectStrategy;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.pojo.Category;
import org.talframework.objexj.object.testmodel.pojo.Product;

/**
 * This integration class demonstrated how to interact with
 * Objex containers in a number of different scenarios.
 * 
 * <p>The example are all based on our simple 'Stock' objects
 * used in other tests. The root object is a category object 
 * that holds more 'categories' and products. The container is 
 * treated as a document, but in reality a store is probably 
 * more realistic.</p>
 * 
 * <p>Note that we use the interfaces to our objects, which 
 * are preffixed 'I' simply because we wanted to create objects
 * of the same name - I do not recommend the prefix 'I' for
 * instances (though if that floats your boat then hey ho).</p>
 * 
 * @author Tom Spencer
 */
public class TestService {
	
	private ContainerFactory factory;
	
	/**
	 * This sets up the initial data for our tests in the store
	 * that the InMemory middleware uses
	 */
	@BeforeClass
	public static void staticsetup() {
	    // Test/123, just a root object
	    Map<ObjexID, Map<String, Object>> objs = new HashMap<ObjexID, Map<String,Object>>();
	    Map<String, Object> vals = new HashMap<String, Object>();
	    vals.put("name", "Root");
	    vals.put("description", "A default root category");
	    objs.put(new DefaultObjexID("Category", 1), vals);
	    SingletonContainerStore.getInstance().setObjects("Test/123", objs);
	    
	    // Test/456, as above
	    objs = new HashMap<ObjexID, Map<String,Object>>();
        vals = new HashMap<String, Object>();
        vals.put("name", "Root");
        vals.put("description", "A default root category");
        objs.put(new DefaultObjexID("Category", 1), vals);
        SingletonContainerStore.getInstance().setObjects("Test/456", objs);
        
        // Test/789, as above
        objs = new HashMap<ObjexID, Map<String,Object>>();
        vals = new HashMap<String, Object>();
        vals.put("name", "Root");
        vals.put("description", "A default root category");
        objs.put(new DefaultObjexID("Category", 1), vals);
        SingletonContainerStore.getInstance().setObjects("Test/789", objs);
	}
	
	@Before
	public void setup() {
	    // Create a strategy for the container type - this is done once in config
	    ContainerStrategy strategy = new SimpleContainerStrategy("Test", "Category",
	            DefaultObjectStrategy.calculateStrategy(null, Category.class, true),
	            DefaultObjectStrategy.calculateStrategy(null, Product.class, true));
        
	    // Setup our middleware factory - again you have one of these, setup in config
        ContainerMiddlewareFactory middlewareFactory = new InMemoryMiddlewareFactory();
        
        // Finally we setup our container factory - and you guessed it, we have 1 of these, setup in config
        factory = new SimpleContainerFactory(strategy, middlewareFactory);
	}
	
	/**
	 * Simply creates a new container, gets holds of the root category
	 * and sets it's properties.
	 */
	@Test
	public void create() {
	    // Ask the factory to create our new container
	    Container container = factory.create();
	    
	    // Get the root object for the container, a category, a just interact with it.
	    ICategory category = container.getRootObject().getBehaviour(ICategory.class);
        category.setName("Root Category");
	    category.setDescription("This is a real description");
	    
	    // Now save the container away
	    String containerId = container.saveContainer();
	    container = null; // Container cannot be used after save (or suspend or clear), this serves to re-inforce the point!
	    
	    // Show that the container now exists under containerId
	    container = factory.get(containerId);
	    assertEquals("Root Category", container.getRootObject().getBehaviour(ICategory.class).getName());
	}
	
	/**
	 * This example shows how we can get an existing container and once
	 * we have the root object, simply walk through that object quite
	 * naturally and without recourse to Objex.
	 */
	@Test
	public void getContainer() {
	    // Open the container and get the root object
	    Container container = factory.get("Test/123");
	    ICategory category = container.getRootObject().getBehaviour(ICategory.class);
	    
	    // Right, so now we can just "walk" the product
	}
	
	/**
	 * This example shows what happens if you try to edit an object, but
	 * don't open it first. It should be noted that there are ways to
	 * cheat the protection demonstrated in this test, but it is not
	 * possible for those changes to be saved without directly accessing
	 * the internals of the container (which you obviously should not do!)
	 */
	@Test(expected=IllegalStateException.class)
	public void editUnopenedContainer() {
	    Container container = factory.get("Test/123");
        ICategory category = container.getRootObject().getBehaviour(ICategory.class);
        
        category.setName("Something");
	}
	
	/**
	 * This example takes an existing container (similar in state to the
	 * one created above) and adds some new categories and products into
	 * it, then saves them away
	 */
	@Test
	public void updateContainer() {
	    Container container = factory.open("Test/456");
	    ICategory category = container.getRootObject().getBehaviour(ICategory.class);
	    
	    // Lets change the name (proving we are actually changing the name)
	    assertNotSame("The Root Category", category.getName());
        category.setName("The Root Category");
	    
	    // Add some new categories to the root, a product and then a product into one of our new categories
        // Note because its an ObjexObj any reference or child list, set or map prop will be non-null!
	    category.getCategories().put("Laptops", new Category("Cat1", "A New Category"));
	    category.getCategories().put("Projectors", new Category("Cat2", "A second Category"));
	    category.getProducts().add(new Product("Product1", "The secret ingredient", 2, 59.99));
	    category.getCategories().get("Laptops").getProducts().add(new Product("Acer e456", "The next gen laptops", 50, 349.99));
	 
	    // Note: We do need to get 'back' our category having added it because we need the ObjexObj version
	    // So although a Category() is not implementing the ObjexObj, it is now, for instance ...
	    assertFalse(ObjexObj.class.isAssignableFrom(Category.class));
	    assertTrue(category.getCategories().get("Laptops") instanceof ObjexObj);   // Magic
	    assertTrue(category.getCategories().get("Laptops") instanceof ICategory);  // Well not magic, but nice!
	    
	    // Lets just save the container
	    container.saveContainer();
	    container = null; // Container cannot be used after save (or suspend or clear), this serves to re-inforce the point!
	    
	    // And lets now prove that the changes were saved
	    container = factory.get("Test/456");
	    category = container.getRootObject().getBehaviour(ICategory.class);
	    assertEquals("The Root Category", category.getName());
	    assertEquals(2, category.getCategories().size());
	    assertEquals(1, category.getProducts().size());
	    assertEquals(1, category.getCategories().get("Laptops").getProducts().size());
    }
	
	/**
	 * This example shows how our changes can extend over a couple of
	 * separate edits by using the suspend functionality.
	 * 
	 * <p>Although we are writing this single test, instead think of
	 * the break here because completely separate - perhaps two seperate
	 * invocations of a Web App or Web Service. The time in between can
	 * be quite large.</p>
	 */
	@Test
	public void updateLongLivedEdit() {
	    // The initial call, open the container, make a change and suspend it
	    Container container = factory.open("Test/789");
        ICategory category = container.getRootObject().getBehaviour(ICategory.class);
        
        category.setName("Stock");
        
        String transactionId = container.suspend();
        container = null; // Container cannot be used after suspend (or save or clear), this serves to re-inforce the point!
        
        // ... Other things happen
        // Here we will return the transactionId and keep it somewhere, then
        // provide it to the next call below
        
        // The second part, lets create a new category and then save everything
        // Remember you need to get everything again as this will be in a new method
        container = factory.open(transactionId);
        category = container.getRootObject().getBehaviour(ICategory.class);
        assertEquals("Stock", category.getName());
        
        category.setDescription("Oh yes, the new description");
        category.getCategories().put("New", new Category("NewCat", "New & Exciting products"));
        
        container.saveContainer();
        container = null; // Container cannot be used after save (or suspend or clear), this serves to re-inforce the point!
        
        // Now lets just get the container back and test our changes
	    container = factory.get("Test/789");
	    assertEquals("Stock", container.getRootObject().getProperty("name"));
	    assertNotNull(container.getRootObject().getBehaviour(ICategory.class).getCategories().get("New"));
	}
}
