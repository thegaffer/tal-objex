package org.talframework.objexj.runtime.rs.service.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.runtime.rs.MiddlewareRequest;
import org.talframework.objexj.runtime.rs.MiddlewareResult;
import org.talframework.objexj.runtime.rs.service.BaseRestfulTest;
import org.talframework.objexj.sample.beans.stock.CategoryBean;


public class TestObjexMiddlewareResource extends BaseRestfulTest {
    
    public TestObjexMiddlewareResource() throws Exception {
        super(ObjexMiddlewareResource.class.getPackage().getName());
    }
    
    @Test
    public void basic() {
        String resp = test("/_test/123", String.class, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void basicJson() {
        String resp = test("/_test/123", String.class, MediaType.APPLICATION_JSON_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void basicXml() {
        String resp = test("/_test/123", String.class, MediaType.APPLICATION_XML_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void getNoDepth() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("depth", "0");
        
        String resp = test("/_test/123", String.class, params, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void testUpdate() throws Exception {
        CategoryBean bean = new CategoryBean();
        bean.setId(new DefaultObjexID("Category", 1).toString());
        bean.setName("Changed Cat1");
        bean.setDescription("Description ...");
        List<ObjexObjStateBean> beans = new ArrayList<ObjexObjStateBean>();
        beans.add(bean);
        
        MiddlewareRequest request = new MiddlewareRequest();
        request.setObjects(beans);
        
        String req = marshallXml(new JAXBContextResolver(), request);
        
        MiddlewareResult result = testPost("/_test/123", MiddlewareResult.class, req, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(result);
        
        // Test the change was made - not needed, should get objects back!!
        Map<String, String> params = new HashMap<String, String>();
        params.put("depth", "0");
        
        System.out.println(System.currentTimeMillis());
        String resp = test("/_test/123", String.class, params, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(System.currentTimeMillis());
        System.out.println(resp);
    }
}
