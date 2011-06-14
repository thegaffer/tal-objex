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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.runtime.rs.MiddlewareRequest;
import org.talframework.objexj.runtime.rs.MiddlewareResult;
import org.talframework.objexj.runtime.rs.test.middleware.JAXBContextResolver;
import org.talframework.objexj.runtime.rs.test.middleware.JSONContextResolver;
import org.talframework.objexj.sample.beans.stock.CategoryBean;


public class TestObjexMiddlewareResource extends BaseRestJUnit {
    
    public TestObjexMiddlewareResource() throws Exception {
        super(JAXBContextResolver.class.getPackage().getName(), new JAXBContextResolver(), new JSONContextResolver());
    }
    
    @Test
    public void basic() {
        String resp = get("/_test/123", String.class, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void basicJson() {
        String resp = get("/_test/123", String.class, MediaType.APPLICATION_JSON_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void basicXml() {
        String resp = get("/_test/123", String.class, MediaType.APPLICATION_XML_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void getNoDepth() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("depth", "0");
        
        String resp = get("/_test/123", String.class, params, MediaType.TEXT_PLAIN_TYPE);
        System.out.println(resp);
    }
    
    @Test
    public void testUpdate() throws Exception {
        CategoryBean bean = new CategoryBean();
        bean.setId(new DefaultObjexID("Category", 1).toString());
        bean.setName("Changed Cat1");
        bean.setDescription("Description ...");
        
        MiddlewareRequest request = new MiddlewareRequest();
        request.setObjects(bean);
        
        MiddlewareResult result = testPost("/_test/123", MiddlewareResult.class, request, MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_XML_TYPE);
        Assert.assertNotNull(result);
        
        // Test the change was made - not needed, should get objects back!!
        Map<String, String> params = new HashMap<String, String>();
        params.put("depth", "0");
        
        System.out.println(System.currentTimeMillis());
        MiddlewareResult result2 = get("/_test/123", MiddlewareResult.class, params, MediaType.APPLICATION_XML_TYPE);
        System.out.println(System.currentTimeMillis());
        Assert.assertNotNull(result2);
        Assert.assertEquals("Stock/123", result2.getContainerId()); // TODO: Should be test
        Assert.assertEquals(1, result2.getObjects().size());
        Assert.assertEquals("Changed Cat1", ((CategoryBean)result2.getObjects().get(0)).getName());
    }
}
