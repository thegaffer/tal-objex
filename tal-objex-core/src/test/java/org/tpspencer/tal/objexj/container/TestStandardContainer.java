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

/**
 * This class tests the StandardContainer
 * 
 * @author Tom Spencer
 */
public class TestStandardContainer {
	
	private Mockery context = new JUnit4Mockery();
	
	private ContainerStrategy strategy;
	private ContainerMiddleware middleware;
	
	@Before
	public void setup() {
		strategy = context.mock(ContainerStrategy.class);
		middleware = context.mock(ContainerMiddleware.class);
	}

	@Test
	public void basic() {
		final ObjexID id = context.mock(ObjexID.class);
		final ObjexObj obj = context.mock(ObjexObj.class); 
		
		context.checking(new Expectations() {{
			oneOf(middleware).init(with(any(Container.class)));
			oneOf(middleware).convertId("1234"); will(returnValue(id));
			allowing(middleware).convertId(id); will(returnValue(id));
			oneOf(middleware).loadObject(with(any(Class.class)), (ObjexID)with(anything()));
			  will(returnValue(obj));
			oneOf(strategy).getObjectStrategies();
			  will(returnValue(null));
			oneOf(middleware).getObjectType(id); will(returnValue("Test"));
		}});
		
		StandardContainer container = new StandardContainer(strategy, middleware, "test");
			
		Assert.assertEquals("test", container.getId());
		// TODO: Re-instate - need object strategy!!
		//Assert.assertNotNull(container.getObject("1234"));
	}
}
