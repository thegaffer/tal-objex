package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleTransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

/**
 * Ensures the stock service will give us repository instances.
 * 
 * @author Tom Spencer
 */
public class TestOrderService {
    
    private Mockery context = new JUnit4Mockery();
    
    /** Mocked factory for service to use */
    private ContainerFactory containerFactory = null;
    /** Mocked middleware factory for integration type unit tests */
    private ContainerMiddlewareFactory middlewareFactory = null;
    
    @Before
    public void setup() {
        containerFactory = context.mock(ContainerFactory.class);
        middlewareFactory = context.mock(ContainerMiddlewareFactory.class);
    }
    
    @Test
    public void create() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final EditableContainer container = context.mock(EditableContainer.class);
        // final ObjexObj obj = context.mock(ObjexObj.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).create(with(any(String.class)));
              will(returnValue(container));
        }});
        
        OrderRepository repo = service.createNewOrder("123");
        Assert.assertNotNull(repo);
        // Assert.assertNotNull(repo.getOrder());
        context.assertIsSatisfied();
    }
    
    @Test
    public void createIntegration() {
        OrderServiceImpl service = new OrderServiceImpl(middlewareFactory);
        final TransactionMiddleware middleware = context.mock(TransactionMiddleware.class);
        final TransactionCache cache = new SimpleTransactionCache();
        final ObjexID id = context.mock(ObjexID.class);
            
        context.checking(new Expectations() {{
            oneOf(middlewareFactory).createContainer(with(any(ContainerStrategy.class)), with(any(String.class))); 
              will(returnValue(middleware));
            oneOf(middleware).createNewId("Order"); will(returnValue(id));
            oneOf(middleware).getRawId(id); will(returnValue("o1"));
            oneOf(middleware).init(with(any(Container.class)));
            allowing(middleware).getCache(); will(returnValue(cache));
            // allowing(id).isNull(); will(returnValue(false));
            allowing(middleware).convertId("Order|1"); will(returnValue(id));
            //oneOf(middleware).getObjectType(id); will(returnValue("Order"));
        }});
        
        OrderRepository repo = service.createNewOrder("123");
        Assert.assertNotNull(repo);
        //Assert.assertNotNull(repo.getOrder());
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
    public void getIntegration() {
        OrderServiceImpl service = new OrderServiceImpl(middlewareFactory);
        final ContainerMiddleware middleware = context.mock(ContainerMiddleware.class);
        
        context.checking(new Expectations() {{
            oneOf(middlewareFactory).getMiddleware(with(any(ContainerStrategy.class)), with("123")); will(returnValue(middleware));
            oneOf(middleware).init(with(any(Container.class)));
        }});
        
        OrderRepository repo = service.getRepository("123");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void open() {
        OrderServiceImpl service = new OrderServiceImpl(containerFactory);
        final EditableContainer container = context.mock(EditableContainer.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).getTransaction("123", "321"); will(returnValue(container));
        }});
        
        OrderRepository repo = service.getOpenRepository("123", "321");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void openIntegration() {
        OrderServiceImpl service = new OrderServiceImpl(middlewareFactory);
        final TransactionMiddleware middleware = context.mock(TransactionMiddleware.class);
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middlewareFactory).getTransaction(with(any(ContainerStrategy.class)), with("123"), with("321")); will(returnValue(middleware));
            oneOf(middleware).init(with(any(EditableContainer.class)));
            oneOf(middleware).getCache(); will(returnValue(cache));
        }});
        
        OrderRepository repo = service.getOpenRepository("123", "321");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
}
