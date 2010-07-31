package org.tpspencer.tal.objexj.gae;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleTransactionCache;
import org.tpspencer.tal.objexj.gae.object.ContainerBean;

public class TestGAEMiddleware {
    
    private Mockery context = new JUnit4Mockery();
    private Container container;
    private ContainerStrategy strategy;
    
    @Before
    public void setup() {
        strategy = context.mock(ContainerStrategy.class);
        container = context.mock(Container.class);
    }

    /**
     * Ensures basic checks
     */
    @Test
    public void basic() {
        // Setup
        ContainerBean bean = new ContainerBean();
        bean.setType("Test");
        bean.setContainerId("123");
        
        // Test
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        // Check
        Assert.assertEquals("Test/123", underTest.getContainerId());
        Assert.assertTrue(underTest.isNew());
        Assert.assertEquals(GAEAllocateObjexIDStrategy.getInstance(), underTest.getIdStrategy());
        Assert.assertNull(underTest.getCache());
    }
    
    /**
     * Ensures basic checks if we have a transaction
     */
    @Test
    public void transaction() {
        // Setup
        ContainerBean bean = new ContainerBean();
        bean.setType("Test");
        bean.setContainerId("123");
        
        GAETransaction trans = new GAETransaction();
        trans.setContainerBean(bean);
        trans.setCache(new SimpleTransactionCache());
        
        // Test
        GAEMiddleware underTest = new GAEMiddleware(strategy, trans);
        underTest.init(container);
        
        // Check
        Assert.assertEquals("Test/123", underTest.getContainerId());
        Assert.assertTrue(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertTrue(underTest.getIdStrategy() instanceof GAENewObjexIDStrategy);
        Assert.assertNotNull(underTest.getCache());
    }
    
    /**
     * Just checks cannot load if not initialised
     */
    @Test(expected=IllegalStateException.class)
    public void loadIfNotInitialised() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.loadObject(String.class, null);
    }
    
    /**
     * Just checks cannot save if no transaction
     */
    @Test(expected=IllegalStateException.class)
    public void saveIfNoTransaction() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        underTest.save();
    }
    
    /**
     * Just checks cannot suspend if no transaction
     */
    @Test(expected=IllegalStateException.class)
    public void suspendIfNoTransaction() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        underTest.suspend();
    }
    
    /**
     * Just checks it is ok to call clear at any time.
     */
    public void clearIfNoTransaction() {
        ContainerBean bean = new ContainerBean();
        GAEMiddleware underTest = new GAEMiddleware(strategy, bean);
        underTest.init(container);
        
        underTest.clear();
    }
}
