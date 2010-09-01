package org.tpspencer.tal.objexj.container.middleware;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache.ObjectRole;
import org.tpspencer.tal.objexj.container.impl.SimpleTransactionCache;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
import org.tpspencer.tal.objexj.service.beans.CategoryBean;

/**
 * Tests the in memory middleware works
 *
 * @author Tom Spencer
 */
public class TestInMemoryMiddleware {
    
    private Mockery context = new JUnit4Mockery();
    
    /**
     * This adds our test container to the in memory store.
     * If we replace that with other collaborates to support
     * more real use (i.e. a file based usage) then this
     * can be removed and mocked instead.
     */
    @BeforeClass
    public static void containerSetup() {
        List<ObjexObjStateBean> objs = new ArrayList<ObjexObjStateBean>();
        objs.add(new CategoryBean("Cat1"));
        objs.add(new CategoryBean("Cat2"));
        SingletonContainerStore.getInstance().setObjects("123", objs);
        
        objs = new ArrayList<ObjexObjStateBean>();
        objs.add(new CategoryBean("Cat1"));
        objs.add(new CategoryBean("Cat2"));
        SingletonContainerStore.getInstance().setObjects("456", objs);
        
        objs = new ArrayList<ObjexObjStateBean>();
        objs.add(new CategoryBean("Cat1"));
        objs.add(new CategoryBean("Cat2"));
        SingletonContainerStore.getInstance().setObjects("789", objs);
        
        SingletonContainerCache.getInstance().setCache("123/trans", new SimpleTransactionCache());
    }

    @Test
    public void newContainer() {
        final Container container = context.mock(Container.class);
        
        InMemoryMiddleware underTest = new InMemoryMiddleware(null, true);
        underTest.init(container);
        
        Assert.assertNull(underTest.getContainerId());
        Assert.assertTrue(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertEquals(new DefaultObjexID("Test", 2), underTest.getIdStrategy().createId(null, null, "Test", null));
        Assert.assertNotNull(underTest.getCache());
        Assert.assertEquals(underTest.getCache(), underTest.open());
        
        context.checking(new Expectations() {{
            oneOf(container).getType(); will(returnValue("TestContainer"));
        }});
        
        Assert.assertTrue(underTest.save(null, null).startsWith("TestContainer/"));
        
        underTest.clear();
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void existingContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("123", false);
        Assert.assertEquals("123", underTest.getContainerId());
        Assert.assertFalse(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertEquals(new DefaultObjexID("Test", 3), underTest.getIdStrategy().createId(null, null, "Test", null));
        Assert.assertNull(underTest.getCache());
        Assert.assertNotNull(underTest.open());
        
        ObjexObjStateBean cat1 = underTest.loadObject(CategoryBean.class, new DefaultObjexID("Any", 1));
        Assert.assertNotNull(cat1);
        Assert.assertEquals("Cat1", ((CategoryBean)cat1).getName());
        
        ObjexObjStateBean cat2 = underTest.loadObject(CategoryBean.class, new DefaultObjexID("Any", 2));
        Assert.assertNotNull(cat2);
        Assert.assertEquals("Cat2", ((CategoryBean)cat2).getName());
    }
    
    @Test
    public void openExistingContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("123", true);
        Assert.assertEquals("123", underTest.getContainerId());
        Assert.assertFalse(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertEquals(new DefaultObjexID("Test", 3), underTest.getIdStrategy().createId(null, null, "Test", null));
        Assert.assertNotNull(underTest.getCache());
        Assert.assertEquals(underTest.getCache(), underTest.open());
        
        // Make sure we get the edited object, not the original
        TransactionCache cache = underTest.getCache();
        cache.addObject(ObjectRole.UPDATED, new DefaultObjexID("Any", 2), new CategoryBean("Cat2_edited"));
        ObjexObjStateBean cat2 = underTest.loadObject(CategoryBean.class, new DefaultObjexID("Any", 2));
        Assert.assertEquals("Cat2_edited", ((CategoryBean)cat2).getName());
    }
    
    @Test
    public void openExistingTransaction() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("123/trans", true);
        Assert.assertEquals("123", underTest.getContainerId());
        Assert.assertFalse(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertEquals(new DefaultObjexID("Test", 3), underTest.getIdStrategy().createId(null, null, "Test", null));
        Assert.assertNotNull(underTest.getCache());
        Assert.assertEquals(underTest.getCache(), underTest.open());
    }
    
    @Test
    public void saveContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("456", true);
        
        // Make some changes (as if container)
        TransactionCache cache = underTest.getCache();
        cache.addObject(ObjectRole.NEW, new DefaultObjexID("Category", 3), new CategoryBean("NewCat"));
        cache.addObject(ObjectRole.UPDATED, new DefaultObjexID("Category", 1), new CategoryBean("Cat1_edited"));
        cache.addObject(ObjectRole.REMOVED, new DefaultObjexID("Category", 2), new CategoryBean("Cat2_removed"));
        
        Assert.assertEquals("456", underTest.save(null, null));
        Assert.assertEquals(3, SingletonContainerStore.getInstance().getObjects("456").size());
        Assert.assertNotNull(SingletonContainerStore.getInstance().getObjects("456").get(0));
        Assert.assertNull(SingletonContainerStore.getInstance().getObjects("456").get(1));
        Assert.assertNotNull(SingletonContainerStore.getInstance().getObjects("456").get(2));
    }
    
    @Test
    public void suspendContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("789", true);
        
        // Make some changes (as if container)
        TransactionCache cache = underTest.getCache();
        cache.addObject(ObjectRole.NEW, new DefaultObjexID("Category", 3), new CategoryBean("NewCat"));
        cache.addObject(ObjectRole.UPDATED, new DefaultObjexID("Category", 1), new CategoryBean("Cat1_edited"));
        cache.addObject(ObjectRole.REMOVED, new DefaultObjexID("Category", 2), new CategoryBean("Cat2_removed"));
        
        Assert.assertEquals("789/trans", underTest.suspend());
        Assert.assertNotNull(SingletonContainerCache.getInstance().getCache("789/trans"));
    }
}
