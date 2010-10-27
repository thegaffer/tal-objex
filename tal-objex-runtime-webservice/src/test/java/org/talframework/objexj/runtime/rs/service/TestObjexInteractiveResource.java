package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.runtime.rs.MiddlewareResult;
import org.talframework.objexj.runtime.rs.test.middleware.JAXBContextResolver;
import org.talframework.objexj.runtime.rs.test.middleware.JSONContextResolver;
import org.talframework.objexj.sample.beans.stock.CategoryBean;
import org.talframework.objexj.sample.beans.stock.ProductBean;

public class TestObjexInteractiveResource extends BaseRestfulTest {
    
    public TestObjexInteractiveResource() throws Exception {
        super(JAXBContextResolver.class.getPackage().getName(), new JAXBContextResolver(), new JSONContextResolver());
    }

    /**
     * Tests we get the root object
     */
    @Test
    public void getRoot() {
        MiddlewareResult res = get("/_stock/123", MiddlewareResult.class, MediaType.APPLICATION_XML_TYPE);
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
        MiddlewareResult res = get("/_stock/123/Category/2", MiddlewareResult.class, MediaType.APPLICATION_XML_TYPE);
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
        MiddlewareResult res = get("/_stock/123/Category/2/products", MiddlewareResult.class, MediaType.APPLICATION_XML_TYPE);
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
        
        MiddlewareResult res = testPost("/_stock/123/Category/2", MiddlewareResult.class, bean, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
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
        
        MiddlewareResult res = testPut("/_stock/123/Category/2/create/categories", MiddlewareResult.class, bean, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
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
        
        MiddlewareResult res = testPost("/_stock/123/Category/2", MiddlewareResult.class, bean, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(res);
        Assert.assertEquals("Stock/123:trans", res.getContainerId());
        
        res = testPost("/_stock/123:trans/save", MiddlewareResult.class, null, null, MediaType.APPLICATION_XML_TYPE);
        Assert.assertEquals("Stock/123", res.getContainerId());
    }
}
