/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.container.middleware;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.impl.DefaultContainerObjectCache;
import org.talframework.objexj.object.testmodel.objex.Category;
import org.talframework.objexj.object.testmodel.objex.Product;

/**
 * Tests the in memory middleware works
 *
 * @author Tom Spencer
 */
public class TestInMemoryMiddleware {
    
    private InternalContainer container = null;
    
    /**
     * This adds our test container to the in memory store.
     * If we replace that with other collaborates to support
     * more real use (i.e. a file based usage) then this
     * can be removed and mocked instead.
     */
    @BeforeClass
    public static void containerSetup() {
        // Container 1
        Map<ObjexID, Map<String, Object>> objs = new HashMap<ObjexID, Map<String,Object>>();
        Map<String, Object> vals = new HashMap<String, Object>();
        vals.put("name", "Cat1"); vals.put("description", "Cat1");
        objs.put(new DefaultObjexID("Category", 1), vals);
        vals = new HashMap<String, Object>();
        vals.put("name", "Cat2"); vals.put("description", "Cat2");
        objs.put(new DefaultObjexID("Category", 2), vals);
        SingletonContainerStore.getInstance().setObjects("123", objs);
        
        // Container 2
        objs = new HashMap<ObjexID, Map<String,Object>>();
        vals = new HashMap<String, Object>();
        vals.put("name", "Cat1"); vals.put("description", "Cat1");
        objs.put(new DefaultObjexID("Category", 1), vals);
        vals = new HashMap<String, Object>();
        vals.put("name", "Cat2"); vals.put("description", "Cat2");
        objs.put(new DefaultObjexID("Category", 2), vals);
        SingletonContainerStore.getInstance().setObjects("456", objs);
        
        // Container 3
        objs = new HashMap<ObjexID, Map<String,Object>>();
        vals = new HashMap<String, Object>();
        vals.put("name", "Cat1"); vals.put("description", "Cat1");
        objs.put(new DefaultObjexID("Category", 1), vals);
        vals = new HashMap<String, Object>();
        vals.put("name", "Cat2"); vals.put("description", "Cat2");
        objs.put(new DefaultObjexID("Category", 2), vals);
        SingletonContainerStore.getInstance().setObjects("789", objs);
        
        //SingletonContainerCache.getInstance().setCache("123:trans", new SimpleTransactionCache());
    }
    
    @Before
    public void setup() {
        container = mock(InternalContainer.class);
        when(container.getType()).thenReturn("Stock");
    }

    @Test
    public void newContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware(null, false);
        underTest.init(container);
        underTest.open();
        
        Assert.assertTrue(underTest.isNew());
        Assert.assertTrue(underTest.isOpen());
        Assert.assertEquals(new DefaultObjexID("Product", 1), underTest.getNextObjectId("Product"));
        
        Product product = new Product("Product1", "Test", 1, 9.99);
        product.init(container, new DefaultObjexID("Prouct", 2), null);
        DefaultContainerObjectCache cache = new DefaultContainerObjectCache(1);
        cache.addObject(product, true);
        
        Assert.assertTrue(underTest.save(cache, null, null).startsWith("Stock/"));
    }
    
    @Test
    public void existingContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("123", false);
        Assert.assertEquals("123", underTest.getContainerId());
        Assert.assertFalse(underTest.isNew());
        
        Category cat1 = new Category();
        cat1.init(container, new DefaultObjexID("Category", 1), null);
        Assert.assertNotNull(underTest.loadObject(cat1));
        Assert.assertEquals("Cat1", cat1.getName());
        
        Category cat2 = new Category();
        cat2.init(container, new DefaultObjexID("Category", 2), null);
        Assert.assertNotNull(underTest.loadObject(cat2));
        Assert.assertEquals("Cat2", cat2.getName());
    }
    
    @Test
    public void openExistingContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("123", false);
        Assert.assertEquals("123", underTest.getContainerId());
        Assert.assertFalse(underTest.isNew());
        underTest.open();
        Assert.assertTrue(underTest.isOpen());
        Assert.assertEquals(new DefaultObjexID("Product", 3), underTest.getNextObjectId("Product"));
        
        // Now get one of the objects and edit it, then save it away
        Category cat2 = new Category();
        cat2.init(container, new DefaultObjexID("Category", 2), null);
        Assert.assertNotNull(underTest.loadObject(cat2));
        Assert.assertEquals("Cat2", cat2.getName());
        
        cat2.setName("Cat2_edited");
        DefaultContainerObjectCache cache = new DefaultContainerObjectCache(1);
        cache.addObject(cat2, true);
        
        Assert.assertEquals("123:trans", underTest.suspend(cache));
    }
    
    //@Test
    /*public void openExistingTransaction() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("123:trans", true);
        Assert.assertEquals("123", underTest.getContainerId());
        Assert.assertFalse(underTest.isNew());
        Assert.assertNotNull(underTest.getIdStrategy());
        Assert.assertEquals(new DefaultObjexID("Test", 3), underTest.getIdStrategy().createId(null, null, "Test", null));
        Assert.assertNotNull(underTest.getCache());
        Assert.assertEquals(underTest.getCache(), underTest.open());
    }*/
    
    @Test
    public void saveContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("456", true);
        
        // Make some changes (as if container)
        Category newCat1 = new Category("NewCat", "NewCat");
        newCat1.init(container, new DefaultObjexID("Category", 3), null);
        Category cat1 = new Category();
        cat1.init(container, new DefaultObjexID("Category", 1), null);
        Category cat2 = new Category();
        cat2.init(container, new DefaultObjexID("Category", 2), null);
        Assert.assertNotNull(underTest.loadObjects(cat1, cat2));
        
        cat1.setDescription("Test 1");
        cat2.setDescription("Test 3");
        
        DefaultContainerObjectCache cache = new DefaultContainerObjectCache(1);
        cache.addObject(newCat1, true);
        cache.addObject(cat1, false);
        cache.addObject(cat2, false);
        
        Assert.assertEquals("456", underTest.save(cache, null, null));
        Assert.assertEquals(3, SingletonContainerStore.getInstance().getObjects("456").size());
        Assert.assertNotNull(SingletonContainerStore.getInstance().getObjects("456").get(new DefaultObjexID("Category", 1)));
        Assert.assertNotNull(SingletonContainerStore.getInstance().getObjects("456").get(new DefaultObjexID("Category", 2)));
        Assert.assertNotNull(SingletonContainerStore.getInstance().getObjects("456").get(new DefaultObjexID("Category", 3)));
    }
    
    @Test
    public void suspendContainer() {
        InMemoryMiddleware underTest = new InMemoryMiddleware("789", true);
        
        Category newCat1 = new Category("NewCat", "NewCat");
        newCat1.init(container, new DefaultObjexID("Category", 3), null);
        Category cat1 = new Category();
        cat1.init(container, new DefaultObjexID("Category", 1), null);
        Category cat2 = new Category();
        cat2.init(container, new DefaultObjexID("Category", 2), null);
        Assert.assertNotNull(underTest.loadObjects(cat1, cat2));
        
        cat1.setDescription("Test 1");
        cat2.setDescription("Test 3");
        
        DefaultContainerObjectCache cache = new DefaultContainerObjectCache(1);
        cache.addObject(newCat1, true);
        cache.addObject(cat1, false);
        cache.addObject(cat2, false);
        
        Assert.assertEquals("789:trans", underTest.suspend(cache));
        Assert.assertNotNull(SingletonContainerCache.getInstance().getCache("789"));
        // Test the maps held!!
    }
}
