package org.tpspencer.tal.objexj.container.middleware;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.exceptions.ContainerExistsException;

/**
 * Simple test for the in memory middleware factory
 *
 * @author Tom Spencer
 */
public class TestInMemoryMiddlewareFactory {
    
    private Mockery context = new JUnit4Mockery();
    private ContainerStrategy strategy = null;
    
    @Before
    public void setup() {
        strategy = context.mock(ContainerStrategy.class);
    }
    
    @After
    public void end() {
        SingletonContainerStore.getInstance().setObjects("999", null);
    }

    @Test
    public void get() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.getMiddleware(strategy, "Test/1234"));
    }
    
    @Test
    public void create() {
        context.checking(new Expectations() {{
            oneOf(strategy).getContainerId(); will(returnValue(null));
        }});
        
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.createContainer(strategy));
    }

    @Test(expected=ContainerExistsException.class)
    public void noCreateIfExists() {
        context.checking(new Expectations() {{
            oneOf(strategy).getContainerId(); will(returnValue("999"));
        }});
        
        SingletonContainerStore.getInstance().setObjects("999", new ArrayList<ObjexObjStateBean>());
        
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        factory.createContainer(strategy);
    }
    
    @Test
    public void open() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.getTransaction(strategy, "Test/1234"));
    }
}
