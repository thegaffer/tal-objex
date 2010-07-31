package org.tpspencer.tal.objexj.container;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.service.beans.CategoryBean;

/**
 * This class tests the StandardContainer
 * 
 * @author Tom Spencer
 */
public class TestStandardContainer {
	
	private Mockery context = new JUnit4Mockery();
	
	private ContainerStrategy strategy;
	private ObjectStrategy testStrategy;
	private ContainerMiddleware middleware;
	
	@Before
	public void setup() {
		strategy = context.mock(ContainerStrategy.class);
		testStrategy = context.mock(ObjectStrategy.class, "test");
		middleware = context.mock(ContainerMiddleware.class);
		
		context.checking(new Expectations() {{
            atMost(1).of(middleware).init(with(any(Container.class)));
		}});
	}

	@Test
	public void basic() {
		final ObjexObj obj = context.mock(ObjexObj.class); 
		
		context.checking(new Expectations() {{
			oneOf(middleware).loadObject(with(any(Class.class)), (ObjexID)with(anything()));
			  will(returnValue(obj));
			oneOf(strategy).getObjectStrategy("Test"); will(returnValue(testStrategy));
			oneOf(testStrategy).getStateClass(); will(returnValue(CategoryBean.class));
		}});
		
		StandardContainer container = new StandardContainer(strategy, middleware, "test");
			
		Assert.assertEquals("test", container.getId());
		Assert.assertNotNull(container.getObject("Test/1234"));
	}
	
	@Test
	public void nullIdObject() {
	    StandardContainer container = new StandardContainer(strategy, middleware, "test");
	    Assert.assertNull(container.getObject(null));
	}
}
