package org.tpspencer.tal.objexj.sample.model.stock.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;

/**
 * Ensures the stock service will give us repository instances.
 * 
 * @author Tom Spencer
 */
public class TestStockService {
    
    private Mockery context = new JUnit4Mockery();
    
    /** Mocked factory for service to use */
    private ContainerFactory containerFactory = null;
    
    @Before
    public void setup() {
        containerFactory = context.mock(ContainerFactory.class);
    }
    
    @Test
    public void get() {
        StockServiceImpl service = new StockServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).get("Stock"); will(returnValue(container));
        }});
        
        StockRepository repo = service.getRepository();
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void open() {
        StockServiceImpl service = new StockServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).open("Stock"); will(returnValue(container));
        }});
        
        StockRepository repo = service.getOpenRepository();
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
    
    @Test
    public void openExisting() {
        StockServiceImpl service = new StockServiceImpl(containerFactory);
        final Container container = context.mock(Container.class);
        
        context.checking(new Expectations() {{
            oneOf(containerFactory).open("123"); will(returnValue(container));
        }});
        
        StockRepository repo = service.getOpenRepository("123");
        Assert.assertNotNull(repo);
        context.assertIsSatisfied();
    }
}
