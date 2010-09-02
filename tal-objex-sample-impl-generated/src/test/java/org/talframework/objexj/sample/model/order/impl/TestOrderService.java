package org.talframework.objexj.sample.model.order.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.model.order.impl.OrderServiceImpl;

/**
 * Unit test of the service class
 * 
 * @author Tom Spencer
 */
public class TestOrderService {
    
    private Mockery context = new JUnit4Mockery();
    
    /** Mocked factory for service to use */
    private ContainerFactory containerFactory = null;
    
    @Before
    public void setup() {
        containerFactory = context.mock(ContainerFactory.class);
    }
    
    @Test
    public void create() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        // final ObjexObj obj = context.mock(ObjexObj.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).create();
              will(returnValue(container));
        }});
        
        OrderRepository repo = service.createNewOrder();
        Assert.assertNotNull(repo);
        // Assert.assertNotNull(repo.getOrder());
        context.assertIsSatisfied();
    }
    
    @Test
    public void get() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).get("123"); will(returnValue(container));
        }});
        
        OrderRepository repo = service.getRepository("123");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void open() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).open("321"); will(returnValue(container));
        }});
        
        OrderRepository repo = service.getOpenRepository("321");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
}
