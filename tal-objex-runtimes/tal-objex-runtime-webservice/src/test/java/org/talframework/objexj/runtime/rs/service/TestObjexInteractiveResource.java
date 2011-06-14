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
package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.runtime.rs.ContainerResult;
import org.talframework.objexj.runtime.rs.test.middleware.JAXBContextResolver;
import org.talframework.objexj.runtime.rs.test.middleware.JSONContextResolver;
import org.talframework.objexj.sample.beans.stock.CategoryBean;
import org.talframework.objexj.sample.beans.stock.ProductBean;

public class TestObjexInteractiveResource extends BaseRestJUnit {
    
    public TestObjexInteractiveResource() throws Exception {
        super(JAXBContextResolver.class.getPackage().getName(), new JAXBContextResolver(), new JSONContextResolver());
    }

    /**
     * Tests we get the root object
     */
    @Test
    public void getRoot() {
        ContainerResult res = get("/_stock/123", ContainerResult.class, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertEquals("Stock/123", res.getContainerId());
        Assert.assertEquals(1, res.getObjects().size());
        
        CategoryBean bean = (CategoryBean)res.getObjects().get(0);
        Assert.assertEquals("Cat1", bean.getName()); 
        Assert.assertEquals("Description Cat1", bean.getDescription());
    }
    
    /**
     * Tests we get a 404 if the container does not exist
     */
    @Test
    public void getRootInvalid() {
        getInvalid("/_stock/1234", 404, MediaType.APPLICATION_XML_TYPE);
    }
    
    /**
     * Tests we can get the object ok
     */
    @Test
    public void getObject() {
        ContainerResult res = get("/_stock/123/Category/2", ContainerResult.class, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertEquals("Stock/123", res.getContainerId());
        Assert.assertEquals(1, res.getObjects().size());
        
        CategoryBean bean = (CategoryBean)res.getObjects().get(0);
        Assert.assertEquals("Cat2", bean.getName()); 
        Assert.assertEquals("Description Cat2", bean.getDescription());
    }
    
    /**
     * Tests we get a 404 if an object does not exist
     */
    @Test
    public void getObjectInvalid() {
        getInvalid("/_stock/123/Category/999", 404, MediaType.APPLICATION_XML_TYPE);
    }
    
    /**
     * Tests we can get the objects referred to by another
     */
    @Test
    public void getObjectReferences() {
        ContainerResult res = get("/_stock/123/Category/2/products", ContainerResult.class, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertEquals("Stock/123", res.getContainerId());
        Assert.assertEquals(2, res.getObjects().size());
        
        ProductBean bean = (ProductBean)res.getObjects().get(1);
        Assert.assertEquals("Product4", bean.getName()); 
        Assert.assertEquals("Description Product4", bean.getDescription());
    }
    
    /**
     * Tests we can run a query
     */
    @Test
    @Ignore("Query support not 100% yet")
    public void query() {
        
    }
    
    /**
     * Tests we get a 404 if we ask for an invalid query
     */
    @Test
    @Ignore("Query support not 100% yet")
    public void invalidQuery() {
        
    }
    
    /**
     * Ensures we can set an object
     */
    @Test
    public void set() {
        CategoryBean bean = new CategoryBean();
        bean.setName("New Cat2 Name");
        bean.setDescription("I have set this description");
        
        ContainerResult res = testPost("/_stock/123/Category/2", ContainerResult.class, bean, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertFalse(res.getContainerId().equals("/Stock/123"));
        Assert.assertEquals(1, res.getObjects().size());
        
        CategoryBean bean2 = (CategoryBean)res.getObjects().get(0);
        Assert.assertNotSame(bean, bean2);
        Assert.assertEquals("New Cat2 Name", bean2.getName()); 
        Assert.assertEquals("I have set this description", bean2.getDescription());
        Assert.assertNotNull(bean2.getProducts());
    }
    
    @Test
    public void create() {
        CategoryBean bean = new CategoryBean();
        bean.setName("Laptops");
        bean.setDescription("A new item added via RS");
        
        ContainerResult res = testPut("/_stock/123/Category/2/create/categories", ContainerResult.class, bean, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertFalse(res.getContainerId().equals("/Stock/123"));
        Assert.assertEquals(1, res.getObjects().size());
        
        CategoryBean bean2 = (CategoryBean)res.getObjects().get(0);
        Assert.assertNotNull(bean2);
        Assert.assertEquals("Laptops", bean2.getName()); 
        Assert.assertEquals("A new item added via RS", bean2.getDescription());
    }
 
    @Test
    @Ignore("Not implemented, not sure if this is valid as could just set!")
    public void add() {
        
    }
    
    @Test
    @Ignore("There are still some issues with removing")
    public void removeIndex() {
        
    }
    
    @Test
    @Ignore("There are still some issues with removing")
    public void removeKey() {
        
    }
    
    @Test
    public void save() {
        CategoryBean bean = new CategoryBean();
        bean.setName("New Cat2 Name");
        bean.setDescription("I have set this description");
        
        ContainerResult res = testPost("/_stock/123/Category/2", ContainerResult.class, bean, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertEquals("Stock/123:trans", res.getContainerId());
        
        res = testPost("/_stock/123:trans/save", ContainerResult.class, null, null, MediaType.APPLICATION_XML_TYPE);
        Assert.assertEquals("Stock/123", res.getContainerId());
    }
}
