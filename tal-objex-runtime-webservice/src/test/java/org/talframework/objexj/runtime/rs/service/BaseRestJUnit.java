package org.talframework.objexj.runtime.rs.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.middleware.SingletonContainerStore;
import org.talframework.objexj.sample.beans.stock.CategoryBean;
import org.talframework.objexj.sample.beans.stock.ProductBean;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.test.framework.JerseyTest;

/**
 * This base class is used when testing the container and
 * middleware resources. It creates a full container prior
 * to any tests and removes it again when finished
 *
 * @author Tom Spencer
 */
public class BaseRestJUnit extends JerseyTest {

    private static final long VALID_MONTH = 2592000000L;
    
    /** Holds the JAXB XML Resolver for client requests */
    private ContextResolver<JAXBContext> xmlResolver;
    /** Holds the JAXB JSON Resolver for client requests */
    private ContextResolver<JAXBContext> jsonResolver;
    
    /*public BaseRestfulTest(String packageName) throws Exception {
        super(ObjexExceptionMapper.class.getPackage().getName(), packageName);
    }*/
    
    public BaseRestJUnit(String packageName, ContextResolver<JAXBContext> xmlResolver, ContextResolver<JAXBContext> jsonResolver) throws Exception {
        super(ObjexExceptionMapper.class.getPackage().getName(), packageName);
        this.xmlResolver = xmlResolver;
        this.jsonResolver = jsonResolver;
    }
    
    /**
     * Helper method to form a call to a web resource
     * 
     * @param res The resource to hit
     * @param expected The expected return type
     * @param accept The types we accept (if none no accept is sent)
     * @return The response
     */
    protected <T> T get(String res, Class<T> expected, MediaType... accept) {
        return get(res, expected, null, accept);
    }
    
    /**
     * Helper to form a call to a web service that we expect to
     * fail.
     * 
     * @param res
     * @param expectedStatus
     * @param accept
     */
    protected void getInvalid(String res, int expectedStatus, MediaType... accept) {
        getInvalid(res, expectedStatus, null, accept);
    }
    
    /**
     * Helper method to form a call to a web resource with parameters
     * 
     * @param res The resource to hit
     * @param expected The expected return type
     * @param params A map of parameters
     * @param accept The types we accept (if none no accept is sent)
     * @return The response
     */
    protected <T> T get(String res, Class<T> expected, Map<String, String> params, MediaType... accept) {
        WebResource resource = resource().path(res);
        if( params != null ) {
            for( String name : params.keySet() ) {
                String val = params.get(name);
                resource = resource.queryParam(name, val);
            }
        }
        
        String ret = null;
        if( accept != null && accept.length > 0 ) {
            ret = resource.accept(accept).get(String.class);
        }
        else {
            ret = resource.get(String.class);
        }
        
        return getResponse(ret, expected, accept);
    }
    
    protected void getInvalid(String res, int expectedStatus, Map<String, String> params, MediaType... accept) {
        try {
            get(res, String.class, params, accept);
        }
        catch( UniformInterfaceException e ) {
            Assert.assertEquals(expectedStatus, e.getResponse().getStatus());
        }
    }
    
    /**
     * Helper method to form a call to a web resource with parameters
     * 
     * @param res The resource to hit
     * @param expected The expected return type
     * @param payload The object to send in the request
     * @param payloadType The mime type to send
     * @param accept The types we accept (if none no accept is sent)
     * @return The response
     */
    protected <T> T testPost(String res, Class<T> expected, Object payload, MediaType payloadType, MediaType... accept) {
        return testPostPut(res, false, expected, payload, payloadType, accept);
    }
    
    /**
     * Helper method to form a call to a web resource with parameters
     * 
     * @param res The resource to hit
     * @param expected The expected return type
     * @param payload The object to send in the request
     * @param payloadType The mime type to send
     * @param accept The types we accept (if none no accept is sent)
     * @return The response
     */
    protected <T> T testPut(String res, Class<T> expected, Object payload, MediaType payloadType, MediaType... accept) {
        return testPostPut(res, true, expected, payload, payloadType, accept);
    }
    
    /**
     * Helper method to form a call to a web resource with parameters
     * 
     * @param res The resource to hit
     * @param put If true then request is a PUT, otherwise a POST
     * @param expected The expected return type
     * @param payload The object to send in the request
     * @param payloadType The mime type to send
     * @param accept The types we accept (if none no accept is sent)
     * @return The response
     */
    protected <T> T testPostPut(String res, boolean put, Class<T> expected, Object payload, MediaType payloadType, MediaType... accept) {
        String realPayload = null;
        
        if( payload != null ) {
            if( !(payload instanceof String) ) {
                if( payloadType.equals(MediaType.APPLICATION_XML_TYPE) && xmlResolver != null ) {
                    realPayload = marshall(xmlResolver, payload);
                }
                else if( payloadType.equals(MediaType.APPLICATION_JSON_TYPE) && jsonResolver != null ) {
                    realPayload = marshall(jsonResolver, payload);
                }
            }
            else {
                realPayload = payload.toString();
            }
        }
        
        WebResource resource = resource().path(res);
        
        Builder builder = resource.getRequestBuilder();
        if( realPayload != null ) builder = builder.entity(realPayload, payloadType);
        if( accept != null && accept.length > 0 ) builder = builder.accept(accept);
        
        String ret = put ? builder.put(String.class) : builder.post(String.class);
        return getResponse(ret, expected, accept);
    }
    
    private <T> T getResponse(String response, Class<T> expected, MediaType... accept) {
        if( expected.equals(String.class) ) {
            return expected.cast(response);
        }
        else if( accept != null && accept[0].equals(MediaType.APPLICATION_XML_TYPE) && xmlResolver != null ) {
            return unmarshall(xmlResolver, response, expected);
        }
        else if( accept != null && accept[0].equals(MediaType.APPLICATION_JSON_TYPE) && jsonResolver != null ) {
            return unmarshall(jsonResolver, response, expected);
        }
        else {
            throw new RuntimeException("Cannot convert result given accept type and resolvers: " + response);
        }
    }
    
    private String marshall(ContextResolver<JAXBContext> resolver, Object object) {
        try {
            JAXBContext context = resolver.getContext(object.getClass());
            StringWriter writer = new StringWriter();
            context.createMarshaller().marshal(object, writer);
            return writer.toString();
        }
        catch( Exception e ) {
            throw new RuntimeException("Caught issue!", e);
        }
    }
    
    private <T> T unmarshall(ContextResolver<JAXBContext> resolver, String text, Class<T> expected) {
        try {
            JAXBContext context = resolver.getContext(expected);
            StringReader reader = new StringReader(text);
            return expected.cast(context.createUnmarshaller().unmarshal(reader));
        }
        catch( Exception e ) {
            throw new RuntimeException("Caught issue!", e);
        }
    }
    
    @Before
    public void setup() {
        CategoryBean cat1 = createCategory(1, 0, "Cat1", "Description Cat1");
        CategoryBean cat2 = createCategory(2, 1, "Cat2", "Description Cat2");
        CategoryBean cat3 = createCategory(3, 1, "Cat3", "Description Cat3");
        CategoryBean cat4 = createCategory(4, 3, "Cat4", "Description Cat4");
        
        ProductBean prod1 = createProduct(5, 1, "Product1", "Description Product1", 9.99, "GBP", 0);
        ProductBean prod2 = createProduct(6, 1, "Product2", "Description Product2", 29.99, "GBP", 0);
        ProductBean prod3 = createProduct(7, 2, "Product3", "Description Product3", 1.99, "GBP", 0);
        ProductBean prod4 = createProduct(8, 2, "Product4", "Description Product4", 24.97, "GBP", 0);
        ProductBean prod5 = createProduct(9, 3, "Product5", "Description Product5", 99.99, "GBP", 0);
        ProductBean prod6 = createProduct(10, 3, "Product6", "Description Product6", 100, "GBP", 0);
        ProductBean prod7 = createProduct(11, 4, "Product7", "Description Product7", 349.95, "GBP", 0);
        ProductBean prod8 = createProduct(12, 4, "Product8", "Description Product8", 223.56, "GBP", 0);
        
        cat1.setProducts(createList(prod1, prod2));
        cat1.setCategories(createList(cat2, cat3));
        cat2.setProducts(createList(prod3, prod4));
        cat3.setProducts(createList(prod5, prod6));
        cat3.setCategories(createList(cat4));
        cat4.setProducts(createList(prod7, prod8));
        
        List<ObjexObjStateBean> objs = new ArrayList<ObjexObjStateBean>();
        objs.add(cat1);
        objs.add(cat2);
        objs.add(cat3);
        objs.add(cat4);
        objs.add(prod1);
        objs.add(prod2);
        objs.add(prod3);
        objs.add(prod4);
        objs.add(prod5);
        objs.add(prod6);
        objs.add(prod7);
        objs.add(prod8);
        
        SingletonContainerStore.getInstance().setObjects("123", objs);
        SingletonContainerStore.getInstance().setObjects("Stock/123", objs);
    }
    
    @After
    public void teardown() {
        SingletonContainerStore.getInstance().setObjects("123", null);
        SingletonContainerStore.getInstance().setObjects("Stock/123", null);
    }
    
    /**
     * @return A category
     */
    private CategoryBean createCategory(int index, int parentIndex, String name, String desc) {
        CategoryBean ret = new CategoryBean();
        if( parentIndex > 0 ) ret.create(new DefaultObjexID("Category", parentIndex));
        ret.preSave(new DefaultObjexID("Category", index));
        ret.setName(name);
        ret.setDescription(desc);
        return ret;
    }
    
    private ProductBean createProduct(int index, int parentIndex, String name, String desc, double price, String currency, int validFor) {
        ProductBean ret = new ProductBean();
        ret.create(new DefaultObjexID("Category", parentIndex));
        ret.preSave(new DefaultObjexID("Product", index));
        ret.setName(name);
        ret.setDescription(desc);
        ret.setPrice(price);
        ret.setCurrency(currency);
        ret.setEffectiveFrom(new Date());
        ret.setEffectiveTo(new Date(ret.getEffectiveFrom().getTime() + (validFor > 0 ? validFor : VALID_MONTH)));
        return ret;
    }
    
    private List<String> createList(ObjexObjStateBean... beans) {
        List<String> ret = new ArrayList<String>();
        
        for( ObjexObjStateBean bean : beans ) {
            ret.add(bean.getId().toString());
        }
        
        return ret;
    }
}
