package org.tpspencer.tal.objexj.container.middleware;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.container.ContainerStrategy;

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

    @Test
    public void get() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.getMiddleware(strategy, "Test/1234"));
    }
    
    @Test
    public void create() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.createContainer(strategy));
    }
    
    @Test
    public void open() {
        InMemoryMiddlewareFactory factory = new InMemoryMiddlewareFactory();
        Assert.assertNotNull(factory.getTransaction(strategy, "Test/1234"));
    }
}
