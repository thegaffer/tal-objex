package org.tpspencer.tal.objexj.sample.model.stock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.model.stock.impl.StockServiceImpl;

/**
 * Ensures the stock service will give us repository instances.
 * 
 * @author Tom Spencer
 */
public class TestStockService {
    
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
    public void get() {
        StockServiceImpl service = new StockServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).get("Stock/Stock"); will(returnValue(container));
        }});
        
        StockRepository repo = service.getRepository();
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void getIntegration() {
        StockServiceImpl service = new StockServiceImpl(middlewareFactory);
        final ContainerMiddleware middleware = context.mock(ContainerMiddleware.class);
        
        context.checking(new Expectations() {{
            oneOf(middlewareFactory).getMiddleware("Stock/Stock"); will(returnValue(middleware));
            oneOf(middleware).init(with(any(Container.class)));
        }});
        
        StockRepository repo = service.getRepository();
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void open() {
        StockServiceImpl service = new StockServiceImpl(containerFactory);
        final EditableContainer container = context.mock(EditableContainer.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).open("Stock/Stock", true); will(returnValue(container));
        }});
        
        StockRepository repo = service.getOpenRepository();
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void openIntegration() {
        StockServiceImpl service = new StockServiceImpl(middlewareFactory);
        final TransactionMiddleware middleware = context.mock(TransactionMiddleware.class);
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middlewareFactory).createTransaction("Stock/Stock", true); will(returnValue(middleware));
            oneOf(middleware).init(with(any(EditableContainer.class)));
            oneOf(middleware).getCache(); will(returnValue(cache));
        }});
        
        StockRepository repo = service.getOpenRepository();
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void openExisting() {
        StockServiceImpl service = new StockServiceImpl(containerFactory);
        final EditableContainer container = context.mock(EditableContainer.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).get("Stock/Stock", "123"); will(returnValue(container));
        }});
        
        StockRepository repo = service.getOpenRepository("123");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void openExistingIntegration() {
        StockServiceImpl service = new StockServiceImpl(middlewareFactory);
        final TransactionMiddleware middleware = context.mock(TransactionMiddleware.class);
        final TransactionCache cache = context.mock(TransactionCache.class);
        
        context.checking(new Expectations() {{
            oneOf(middlewareFactory).getTransaction("Stock/Stock", "123"); will(returnValue(middleware));
            oneOf(middleware).init(with(any(EditableContainer.class)));
            oneOf(middleware).getCache(); will(returnValue(cache));
        }});
        
        StockRepository repo = service.getOpenRepository("123");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
}
