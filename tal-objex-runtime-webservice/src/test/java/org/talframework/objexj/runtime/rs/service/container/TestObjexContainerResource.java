package org.talframework.objexj.runtime.rs.service.container;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.talframework.objexj.runtime.rs.service.BaseRestfulTest;

public class TestObjexContainerResource extends BaseRestfulTest {
    
    public TestObjexContainerResource() throws Exception {
        super(ObjexContainerResource.class.getPackage().getName());
    }
    
    @Test
    public void basic() {
        String resp = test("/test/123", String.class, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void getXml() {
        String resp = test("/test/123", String.class, MediaType.APPLICATION_XML_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void getJson() {
        String resp = test("/test/123", String.class, MediaType.APPLICATION_JSON_TYPE);
        System.out.println(resp);
    }
    
    @Test
    @Ignore("HTML not added yet")
    public void getHtml() {
        String resp = test("/test/123", String.class, MediaType.TEXT_HTML_TYPE);
        System.out.println(resp);
    }
    
    // TODO: Get a specific object
}
