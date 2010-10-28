package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.runtime.rs.test.container.JAXBContextResolver;
import org.talframework.objexj.runtime.rs.test.container.JSONContextResolver;

public class TestObjexDocumentResource extends BaseRestJUnit {
    
    public TestObjexDocumentResource() throws Exception {
        super(JAXBContextResolver.class.getPackage().getName(), new JAXBContextResolver(), new JSONContextResolver());
    }
    
    @Test
    public void basic() {
        String resp = get("/test/123", String.class, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void getXml() {
        String result = get("/test/123", String.class, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void getJson() {
        String result = get("/test/123", String.class, MediaType.APPLICATION_JSON_TYPE);
        Assert.assertNotNull(result);
    }
    
    @Test
    @Ignore("HTML not added yet")
    public void getHtml() {
        String resp = get("/test/123", String.class, MediaType.TEXT_HTML_TYPE);
        Assert.assertNotNull(resp);
    }
    
    // TODO: Get a specific object
}