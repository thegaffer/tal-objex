package org.tpspencer.tal.objexj.service;

import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleTransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.service.beans.CategoryBean;
import org.tpspencer.tal.objexj.service.beans.ProductBean;

/**
 * This class represents a service that would be written
 * to access an Objex container and manipulate it. This
 * method serves as a basic integration test, but also a
 * documentation of how to use objex
 * 
 * @author Tom Spencer
 */
public class TestService {
	
	private Mockery context = new JUnit4Mockery();
	private ContainerFactory containerLocator;
	private ContainerStrategy strategy;
	private ContainerMiddlewareFactory middlewareFactory;
	private TransactionMiddleware middleware;
	
	@Before
	public void setup() {
		strategy = context.mock(ContainerStrategy.class);
		middlewareFactory = context.mock(ContainerMiddlewareFactory.class);
		middleware = context.mock(TransactionMiddleware.class);
		
		containerLocator = new SimpleContainerFactory(strategy, middlewareFactory);
		
		/*
		final CategoryBean category = new CategoryBean();
		category.setName("Cat1");
		final ProductBean product = new ProductBean();
		product.setName("Product1");
		*/
		final ObjexID rootId = new DefaultObjexID("Category", 1);
		
		final ObjectStrategy categoryStrategy = new SimpleObjectStrategy("Category", null, CategoryBean.class);
		final ObjectStrategy productStrategy = new SimpleObjectStrategy("Product", null, ProductBean.class);
		final Map<String, ObjectStrategy> objectStrategies = new HashMap<String, ObjectStrategy>();
		objectStrategies.put("Category", categoryStrategy);
		objectStrategies.put("Product", productStrategy);
		
		// final TransactionCache cache = new SimpleTransactionCache();
		
		context.checking(new Expectations() {{
		    allowing(strategy).getObjectStrategy("Category"); will(returnValue(categoryStrategy));
            allowing(strategy).getObjectStrategy("Product"); will(returnValue(productStrategy));
            allowing(strategy).getRootObjectName(); will(returnValue("Category"));
            allowing(strategy).getRootObjectID(); will(returnValue(rootId));
            
            /*
			allowing(middlewareFactory).createContainer(with(strategy)); will(returnValue(middleware));
			allowing(middlewareFactory).getTransaction(strategy, "1"); will(returnValue(middleware));
			
			atMost(1).of(middleware).suspend(); will(returnValue("1"));
            atMost(1).of(middleware).clear();
            allowing(middleware).getCache(); will(returnValue(cache));
            allowing(middleware).save();
			*/
		}});
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
	    final ObjexID idCategory = new DefaultObjexID("Category", 20);
        final ObjexID idProduct = new DefaultObjexID("Product", 21);
        final CategoryBean currentCategory = new CategoryBean("Cat1");
        final ProductBean currentProduct = new ProductBean("Product1");
        
	    context.checking(new Expectations() {{
	        oneOf(middlewareFactory).getMiddleware(strategy, "Stock/1"); will(returnValue(middleware));
	        oneOf(middleware).getContainerId(); will(returnValue("Stock/1"));
	        oneOf(middleware).init(with(any(Container.class)));
	        oneOf(middleware).loadObject(CategoryBean.class, idCategory);
                will(returnValue(currentCategory));
            oneOf(middleware).loadObject(ProductBean.class, idProduct);
                will(returnValue(currentProduct));
	    }});
	    
		Container container = containerLocator.get("Stock/1");
		
		ObjexObj obj1 = container.getObject("Category/20");
		ObjexObj obj2 = container.getObject("Product/21");
		
		CategoryBean obtainedCategory = obj1.getStateObject(CategoryBean.class);
		ProductBean obtainedProduct = obj2.getStateObject(ProductBean.class);
		
		// You could now form and return you aggregate object, we are going to test it though!
		Assert.assertNotNull(obtainedCategory);
		Assert.assertEquals("Cat1", obtainedCategory.getName());
		Assert.assertNotNull(obtainedProduct);
		Assert.assertEquals("Product1", obtainedProduct.getName());
		context.assertIsSatisfied();
	}
	
	/**
	 * This method creates a new container
	 */
	@Test
	public void createDocument() {
	    // Setup
	    final TransactionCache cache = new SimpleTransactionCache();
	    final ObjexID rootId = new DefaultObjexID("Category", 1);
	    
	    context.checking(new Expectations() {{
	        oneOf(middlewareFactory).createContainer(with(strategy)); will(returnValue(middleware));
	        oneOf(middleware).getCache(); will(returnValue(cache));
	        oneOf(middleware).init(with(any(Container.class)));
	        oneOf(middleware).getCache(); will(returnValue(cache));
	        oneOf(middleware).save(null, null); will(returnValue("Stock/1"));
	    }});
        
	    // Test
        EditableContainer store = containerLocator.create();
		ObjexObj root = store.getRootObject();
        root.getStateObject(CategoryBean.class).setName("Hi");
        String id = store.saveContainer();
		
		// Asserts
        Assert.assertEquals("Stock/1", id);
		CategoryBean bean = (CategoryBean)cache.getNewObjects().get(rootId);
		Assert.assertNotNull(bean);
		Assert.assertEquals("Hi", bean.getName());
		context.assertIsSatisfied();
	}
	
	/**
	 * This method describes how to make an immediate
	 * change on a container
	 */
	@Test
	public void update() {
	    // Setup
	    final ObjexID idRoot = new DefaultObjexID("Category", 1);
        final ObjexID idCategory = new DefaultObjexID("Category", 20);
        final CategoryBean currentCategory = new CategoryBean("Cat1");
        final TransactionCache cache = new SimpleTransactionCache();
        
	    context.checking(new Expectations() {{
	        oneOf(middlewareFactory).getTransaction(strategy, "Stock/1"); will(returnValue(middleware));
            oneOf(middleware).getContainerId(); will(returnValue("Stock/1"));
            oneOf(middleware).init(with(any(Container.class)));
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(middleware).loadObject(CategoryBean.class, idCategory);
                will(returnValue(currentCategory));
            oneOf(middleware).loadObject(CategoryBean.class, idRoot);
                will(returnValue(currentCategory));
            oneOf(middleware).save(null, null); will(returnValue("Stock/1"));
            
            oneOf(middlewareFactory).getMiddleware(strategy, "Stock/1"); will(returnValue(middleware));
            oneOf(middleware).getContainerId(); will(returnValue("Stock/1"));
            oneOf(middleware).init(with(any(Container.class)));
            oneOf(middleware).loadObject(CategoryBean.class, idCategory);
                will(returnValue(currentCategory));
        }});
        
	    // Test
        EditableContainer store = containerLocator.open("Stock/1");
		
		ObjexObj obj1 = store.getObject("Category/20");
		CategoryBean category = obj1.getStateObject(CategoryBean.class);
		category.setName("Cat1_edited");
		
		store.saveContainer();
		
		// Asserts
		Container store1 = containerLocator.get("Stock/1");
		category = store1.getObject("Category/20").getStateObject(CategoryBean.class);
		Assert.assertEquals("Cat1_edited", category.getName());
	}
	
	/**
	 * This method describes how to make updates inside
	 * the long-lived edit
	 */
	@Test
	public void updateLongLivedEdit() {
	    // Setup
	    final ObjexID idRoot = new DefaultObjexID("Category", 1);
	    final ObjexID idCategory = new DefaultObjexID("Category", 20);
        final CategoryBean currentCategory = new CategoryBean("Cat1");
	    final TransactionCache cache = new SimpleTransactionCache();
	    
	    context.checking(new Expectations() {{
	        // Initial Open
	        oneOf(middlewareFactory).getTransaction(strategy, "Stock/1"); will(returnValue(middleware));
            oneOf(middleware).getContainerId(); will(returnValue("Stock/1"));
            oneOf(middleware).init(with(any(Container.class)));
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(middleware).suspend(); will(returnValue("Stock/trans:123"));
            oneOf(middleware).clear();
            
            // Open transaction, make an edit and save
            oneOf(middlewareFactory).getTransaction(strategy, "Stock/trans:123"); will(returnValue(middleware));
            oneOf(middleware).getContainerId(); will(returnValue("Stock/1"));
            oneOf(middleware).init(with(any(Container.class)));
            oneOf(middleware).getCache(); will(returnValue(cache));
            oneOf(middleware).loadObject(CategoryBean.class, idCategory);
                will(returnValue(currentCategory));
            oneOf(middleware).loadObject(CategoryBean.class, idRoot);
                will(returnValue(currentCategory));
            oneOf(middleware).save(null, null); will(returnValue("Stock/1"));
            
            // Get container and query
            oneOf(middlewareFactory).getMiddleware(strategy, "Stock/1"); will(returnValue(middleware));
            oneOf(middleware).getContainerId(); will(returnValue("Stock/1"));
            oneOf(middleware).init(with(any(Container.class)));
            oneOf(middleware).loadObject(CategoryBean.class, idCategory);
                will(returnValue(currentCategory));
        }});
        
	    // Test I
        EditableContainer store = containerLocator.open("Stock/1");
		String transId = store.suspend();
		Assert.assertEquals("Stock/trans:123", transId);
		store.closeContainer();
		
		// Asserts I
		store = containerLocator.open(transId);
		Assert.assertNotNull(store);
		
		ObjexObj obj1 = store.getObject("Category/20");
		CategoryBean category = obj1.getStateObject(CategoryBean.class);
		category.setName("Cat1_edited_again");
		
		Assert.assertEquals("Cat1_edited_again", store.getObject("Category/20").getProperty("name"));
		
		// Test II
		store.saveContainer();
		Assert.assertFalse(store.isOpen());
		
		// Asserts II
		Container store1 = containerLocator.get("Stock/1");
		category = store1.getObject("Category/20").getStateObject(CategoryBean.class);
		Assert.assertEquals("Cat1_edited_again", category.getName());
	}
}
