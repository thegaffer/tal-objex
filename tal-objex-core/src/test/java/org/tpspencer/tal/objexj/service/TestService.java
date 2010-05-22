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
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleTransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerFactory;
import org.tpspencer.tal.objexj.locator.SimpleContainerLocator;
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
	
	@Before
	public void setup() {
		strategy = context.mock(ContainerStrategy.class);
		final ContainerMiddlewareFactory middlewareFactory = context.mock(ContainerMiddlewareFactory.class);
		final TransactionMiddleware middleware = context.mock(TransactionMiddleware.class);
		
		Map<String, ContainerFactory> factories = new HashMap<String, ContainerFactory>();
		SimpleContainerFactory factory = new SimpleContainerFactory(strategy, middlewareFactory);
		factories.put("Stock", factory);
		
		final ObjexID idCategory = context.mock(ObjexID.class, "categoryId");
		final ObjexID idProduct = context.mock(ObjexID.class, "productId");
		
		containerLocator = new SimpleContainerLocator(factories);

		final CategoryBean category = new CategoryBean();
		category.setName("Cat1");
		final ProductBean product = new ProductBean();
		product.setName("Product1");
		
		final ObjectStrategy categoryStrategy = new SimpleObjectStrategy(CategoryBean.class);
		final ObjectStrategy productStrategy = new SimpleObjectStrategy(ProductBean.class);
		final Map<String, ObjectStrategy> objectStrategies = new HashMap<String, ObjectStrategy>();
		objectStrategies.put("Category", categoryStrategy);
		objectStrategies.put("Product", productStrategy);
		
		final TransactionCache cache = new SimpleTransactionCache();
		
		context.checking(new Expectations() {{
			allowing(idCategory).isNull(); will(returnValue(false));
			allowing(idProduct).isNull(); will(returnValue(false));
			
			allowing(middlewareFactory).getMiddleware(with(strategy), (String)with(anything())); will(returnValue(middleware));
			allowing(middlewareFactory).createTransaction(with(strategy), (String)with(anything())); will(returnValue(middleware));
			allowing(middlewareFactory).createTransaction(with(strategy), (String)with(anything()), with(any(ObjexObjStateBean.class))); will(returnValue(middleware));
			allowing(middlewareFactory).getTransaction(strategy, "Stock/1", "1"); will(returnValue(middleware));
			
			allowing(middleware).init(with(any(Container.class)));
			allowing(middleware).convertId(1); will(returnValue(idCategory));
			allowing(middleware).convertId(null); will(returnValue(null));
			allowing(middleware).convertId(20); will(returnValue(idCategory));
			allowing(middleware).convertId(idCategory); will(returnValue(idCategory));
			allowing(middleware).getObjectType(idCategory); will(returnValue("Category"));
			allowing(middleware).convertId(21); will(returnValue(idProduct));
			allowing(middleware).convertId(idProduct); will(returnValue(idProduct));
			allowing(middleware).getObjectType(idProduct); will(returnValue("Product"));
			allowing(middleware).convertId("CategoryBean|1"); will(returnValue(idCategory));
			
			oneOf(middleware).createNewId(with("Category"), with(any(ObjexObjStateBean.class))); will(returnValue(idCategory));
			
			allowing(middleware).init(with(any(Container.class)));
			// allowing(transaction).init(with(any(Container.class)));
			
			allowing(middleware).loadObject(CategoryBean.class, idCategory);
			  will(returnValue(category));
			allowing(middleware).loadObject(ProductBean.class, idProduct);
			  will(returnValue(product));
			  
			atMost(1).of(middleware).suspend(with(any(TransactionCache.class))); will(returnValue("1"));
			atMost(1).of(middleware).clear(with(any(TransactionCache.class)));
			allowing(middleware).getCache(); will(returnValue(cache));
			
			allowing(strategy).getObjectStrategies(); will(returnValue(objectStrategies));
			
			allowing(middleware).save(with(any(TransactionCache.class)));
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
		Container container = containerLocator.get("Stock/1");
		
		ObjexObj obj1 = container.getObject(20);
		ObjexObj obj2 = container.getObject(21);
		
		CategoryBean category = obj1.getStateObject(CategoryBean.class);
		ProductBean product = obj2.getStateObject(ProductBean.class);
		
		// You could now form and return you aggregate object, we are going to test it though!
		Assert.assertNotNull(category);
		Assert.assertEquals("Cat1", category.getName());
		Assert.assertNotNull(product);
		Assert.assertEquals("Product1", product.getName());
	}
	
	/**
	 * This method creates a new container
	 */
	@Test
	public void createDocument() {
	    context.checking(new Expectations(){{
	        allowing(strategy).getRootObjectName(); will(returnValue("CategoryBean"));
	        oneOf(strategy).getObjectStrategy("CategoryBean"); will(returnValue(new SimpleObjectStrategy(CategoryBean.class)));
	        
	    }});
	    
	    EditableContainer store = containerLocator.create("Stock/1");
		
		ObjexObj root = store.getRootObject();
        root.getStateObject(CategoryBean.class).setName("Hi");
        
		store.saveContainer();
		
		// End of simple example, now test
		String id = store.getId();
		Container container = containerLocator.get(id);
		Assert.assertNotNull(container);
		Assert.assertNotNull(container.getObject(1));
		Assert.assertEquals("Hi", container.getObject(1).getProperty("name"));
	}
	
	/**
	 * This method describes how to make an immediate
	 * change on a container
	 */
	@Test
	public void update() {
		EditableContainer store = containerLocator.open("Stock/1");
		
		ObjexObj obj1 = store.getObject(20);
		CategoryBean category = obj1.getStateObject(CategoryBean.class);
		category.setName("Cat1_edited");
		
		store.saveContainer();
		
		// Example ends, Test
		Container store1 = containerLocator.get("Stock/1");
		category = store1.getObject(20).getStateObject(CategoryBean.class);
		Assert.assertEquals("Cat1_edited", category.getName());
	}
	
	/**
	 * This method describes how to start a long lived
	 * edit
	 */
	@Test
	public void startLongLivedEdit() {
		EditableContainer store = containerLocator.open("Stock/1");
		Assert.assertNotNull(store.suspend());
		
		// Example ends, Close is so this test is isolated
		store.closeContainer();
		Assert.assertFalse(store.isOpen());
	}
	
	/**
	 * This method describes how to make updates inside
	 * the long-lived edit
	 */
	@Test
	public void updateLongLivedEdit() {
		EditableContainer store = containerLocator.open("Stock/1");
		String transId = store.suspend();
		
		// Example Starts
		store = containerLocator.getTransaction("Stock/1", transId);
		Assert.assertNotNull(store);
		
		ObjexObj obj1 = store.getObject(20);
		CategoryBean category = obj1.getStateObject(CategoryBean.class);
		category.setName("Cat1_edited_again");
		
		Assert.assertEquals("Cat1_edited_again", store.getObject(20).getProperty("name"));
		
		// Store this transaction away
		store.saveContainer();
		Assert.assertFalse(store.isOpen());
		
		// Example ends, Test
		Container store1 = containerLocator.get("Stock/1");
		category = store1.getObject(20).getStateObject(CategoryBean.class);
		Assert.assertEquals("Cat1_edited_again", category.getName());
	}
}
