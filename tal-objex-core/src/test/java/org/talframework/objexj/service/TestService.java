package org.tpspencer.tal.objexj.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.impl.SimpleContainerStrategy;
import org.tpspencer.tal.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.tpspencer.tal.objexj.container.middleware.SingletonContainerStore;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.service.beans.CategoryBean;
import org.tpspencer.tal.objexj.service.beans.ProductBean;

/**
 * This class represents a service that would be written
 * to access an Objex container and manipulate it. This
 * method serves as a basic integration test, but also a
 * documentation of how to use objex.
 * 
 * <p>Note: In this test when interacting with the objects
 * we get the state bean and then change that. This is not
 * really the way to use objects, but rather get hold of
 * a business interface from the ObjexObj and use it 
 * directly. This test will be changed in the future to do
 * this, but for now it is a guide more on how to use a
 * container.</p>
 * 
 * @author Tom Spencer
 */
public class TestService {
	
	private static ContainerFactory factory;
	
	/**
	 * Sets up the definition of our little test container
	 * using the InMemory middleware implementation. This
	 * is done only once for all tests
	 */
	@BeforeClass
	public static void setup() {
	    ContainerStrategy strategy = new SimpleContainerStrategy("Test", "Category", 
	            new SimpleObjectStrategy("Category", null, CategoryBean.class),
	            new SimpleObjectStrategy("Product", null, ProductBean.class));
	    
	    ContainerMiddlewareFactory middlewareFactory = new InMemoryMiddlewareFactory();
	    
	    factory = new SimpleContainerFactory(strategy, middlewareFactory);
	    
	    // Now setup a couple of containers
	    List<ObjexObjStateBean> test1Objs = new ArrayList<ObjexObjStateBean>(3);
	    test1Objs.add(new CategoryBean("RootCategory"));
	    test1Objs.add(new ProductBean("Product1"));
	    test1Objs.add(new CategoryBean("Cat1"));
	    SingletonContainerStore.getInstance().setObjects("Test/123", test1Objs);
	    
	    List<ObjexObjStateBean> test2Objs = new ArrayList<ObjexObjStateBean>(6);
        test2Objs.add(new CategoryBean("RootCategory"));
        test2Objs.add(new CategoryBean("Cat1"));
        test2Objs.add(new CategoryBean("Cat2"));
        test2Objs.add(new CategoryBean("Cat3"));
        test2Objs.add(new CategoryBean("Cat4"));
        test2Objs.add(new CategoryBean("Cat5"));
        SingletonContainerStore.getInstance().setObjects("Test/456", test2Objs);
	}
	
	/**
	 * The first test creates a new container
	 */
	@Test
	public void create() {
	    Container container = factory.create();
	    Assert.assertTrue(container.isOpen()); // Is open after creating
	    Assert.assertTrue(container.isNew()); // Is new when just created
	    
	    // Changing the root object
	    container.getRootObject().setProperty("description", "This is the real description");
	    
	    container.saveContainer();
	}
	
	/**
	 * This method describes how to create an aggregate
	 * response from objects inside a container. In this
	 * example we know the IDs of the objects we want, 
	 * we could choose to walk the objects from the root
	 * of the container, i.e. get object 1. The ID of
	 * the container though is neccessary.
	 */
	@Test
	public void createAggregateResponse() {
	    Container container = factory.get("Test/123");
		
		ObjexObj obj1 = container.getObject("Category/3");
		ObjexObj obj2 = container.getObject("Product/2");
		
		// You could now form and return you aggregate object, we are going to test it though!
		Assert.assertNotNull(obj1);
		Assert.assertEquals("Cat1", obj1.getProperty("name"));
		Assert.assertNotNull(obj2);
		Assert.assertEquals("Product1", obj2.getProperty("name"));
	}
	
	/**
	 * This method describes how to make an immediate
	 * change on a container
	 */
	@Test
	public void update() {
	    Container store = factory.open("Test/123");
		
		ObjexObj category = store.getObject("Category/1");
		category.setProperty("name", "RootCat_edited");
		
		store.saveContainer();
		
		// Asserts
		Container store1 = factory.get("Test/123");
		Assert.assertEquals("RootCat_edited", store1.getObject("Category/1").getProperty("name"));
	}
	
	/**
	 * This method describes how to make updates inside
	 * the long-lived edit
	 */
	@Test
	public void updateLongLivedEdit() {
	    // Start the transaction - 1 call
	    Container store = factory.open("Test/456");
		String transId = store.suspend(); // Unlikely you will just suspend it
		
		// ... Other things happen
		
		// Update Container inside transaction
		store = factory.open(transId);
		Assert.assertNotNull(store);
		
		ObjexObj category = store.getObject("Category/4");
		category.setProperty("name", "Cat1_edited_again");
		Assert.assertEquals("Cat1_edited_again", store.getObject("Category/4").getProperty("name"));
		
		// ... Other things happen
		
		// Now go and save the container
		store.saveContainer();
		Assert.assertFalse(store.isOpen());
		
		// Test that our change is now part of the transaction.
		Container store1 = factory.get("Test/456");
		category = store1.getObject("Category/4");
		Assert.assertEquals("Cat1_edited_again", category.getProperty("name"));
	}
}
