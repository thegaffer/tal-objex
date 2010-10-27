package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.runtime.rs.DocumentResult;
import org.talframework.objexj.runtime.rs.test.container.JAXBContextResolver;

public class TestObjexContainerResource extends BaseRestfulTest {
    
    public TestObjexContainerResource() throws Exception {
        super(JAXBContextResolver.class.getPackage().getName());
    }
    
    @Test
    public void basic() {
        String resp = get("/test/123", String.class, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void getXml() {
        DocumentResult result = get("/test/123", DocumentResult.class, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(result);
        Assert.assertEquals("123", result.getContainerId());
        Assert.assertFalse(result.isPartial());
        Assert.assertNotNull(result.getObject());
    }
    
    @Test
    public void getJson() {
        DocumentResult result = get("/test/123", DocumentResult.class, MediaType.APPLICATION_JSON_TYPE);
        Assert.assertNotNull(result);
        Assert.assertEquals("123", result.getContainerId());
        Assert.assertFalse(result.isPartial());
        Assert.assertNotNull(result.getObject());
    }
    
    @Test
    @Ignore("HTML not added yet")
    public void getHtml() {
        String resp = get("/test/123", String.class, MediaType.TEXT_HTML_TYPE);
        System.out.println(resp);
    }
    
    // TODO: Get a specific object
}
