package org.tpspencer.tal.objexj.container;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

/**
 * This class tests the standard transaction
 * 
 * @author Tom Spencer
 */
public class TestStandardTransaction {
    
private Mockery context = new JUnit4Mockery();
    
    private ContainerStrategy strategy;
    private ObjectStrategy testStrategy;
    private TransactionMiddleware middleware;
    private TransactionCache cache;
    
    @Before
    public void setup() {
        strategy = context.mock(ContainerStrategy.class);
        testStrategy = context.mock(ObjectStrategy.class, "test");
        middleware = context.mock(TransactionMiddleware.class);
        cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            atMost(1).of(middleware).init(with(any(Container.class)));
            atMost(1).of(middleware).getCache(); will(returnValue(cache));
        }});
    }
    
    @Test
    public void nullIdObject() {
        SimpleTransaction container = new SimpleTransaction(strategy, middleware, "test");
        Assert.assertNull(container.getObject(null));
    }
}
