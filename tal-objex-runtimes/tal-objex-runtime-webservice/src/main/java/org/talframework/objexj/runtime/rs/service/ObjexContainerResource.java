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
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest.ValidationType;
import org.talframework.objexj.exceptions.ContainerInvalidException;
import org.talframework.objexj.locator.InterceptingContainerFactory;
import org.talframework.objexj.object.writer.BeanObjectReader;
import org.talframework.objexj.query.DefaultQueryRequest;
import org.talframework.objexj.runtime.rs.ContainerResult;
import org.talframework.objexj.validation.CurrentValidationRequest;
import org.talframework.objexj.validation.SimpleValidationRequest;
import org.talframework.tal.aspects.annotations.Profile;
import org.talframework.tal.aspects.annotations.Trace;

/**
 * This Restful Web Service is aimed at exposing a container
 * to an interactive thin client. This allows the client
 * to get, create and edit the state of any object in the
 * container. It also allows them to seperately amend the
 * relationships between objects by allowing them to create/
 * add and remove objects from reference fields. All 
 * interactions result in the container being suspended 
 * until a call to save the container comes in.
 * 
 * <p>The only drawback to this is the containerId must be
 * a single value (no slashes). It would be possible to 
 * override all the methods such that a new Path is added
 * as appropriate.</p>
 *
 * @author Tom Spencer
 */
public abstract class ObjexContainerResource {
    
    private final String containerType;
    private final InterceptingContainerFactory factory;
    
    public ObjexContainerResource(String type, InterceptingContainerFactory factory) {
        this.containerType = type;
        this.factory = factory;
    }
    
    /**
     * Helper to get the full containerId given the ID
     * part passed in a request.
     * 
     * @param containerId The ID part of the container
     * @return The full ID
     */
    private String getFullContainerId(String containerId) {
        return containerType + "/" + containerId;
    }
    
    /**
     * This method allows the client to get the
     * root object of a container.
     * 
     * @return The result containing the object(s)
     */
    @GET
    @Path("/{containerId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult getRoot(@PathParam("containerId") String containerId) {
        containerId = getFullContainerId(containerId);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.get(interceptor, containerId);
        ObjexObj root = container.getRootObject();
        ObjexObjStateBean rootBean = interceptor.findObject(root.getId());
        
        return new ContainerResult(containerId, rootBean);
    }

    /**
     * This method allows the client to get an
     * individual object.
     * 
     * @return The result containing the object(s)
     */
    @GET
    @Path("/{containerId}/{objectType}/{objectId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult get(
            @PathParam("containerId") String containerId, 
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId) {
        containerId = getFullContainerId(containerId);
        ObjexID objId = new DefaultObjexID(objectType, objectId, true);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.get(interceptor, containerId);
        ObjexObj obj = container.getObject(objId);
        ObjexObjStateBean objBean = interceptor.findObject(obj.getId());
        
        return new ContainerResult(containerId, objBean);
    }
    
    /**
     * This method allows the client to get an
     * all objects held in a reference property of 
     * an object. It does not matter whether the 
     * field holds owned or unowned references.
     * 
     * @return The result containing the object(s)
     */
    @GET
    @Path("/{containerId}/{objectType}/{objectId}/{fieldName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult getReferences(
            @PathParam("containerId") String containerId, 
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId,
            @PathParam("fieldName") String fieldName) {
        containerId = getFullContainerId(containerId);
        ObjexID objId = new DefaultObjexID(objectType, objectId, true);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.get(interceptor, containerId);
        ObjexObj obj = container.getObject(objId);
        
        Object prop = obj.getProperty(fieldName);
        List<ObjexObjStateBean> objects = interceptor.findObjects(prop);
        
        return new ContainerResult(containerId, objects);
    }
    
    /**
     * This method allows the client to run a
     * query to get a set of objects.
     * 
     * @return The result containing the object(s)
     */
    @GET
    @Path("/{containerId}/query/{queryName}/{offset}/{pageSize}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult query(
            @PathParam("containerId") String containerId, 
            @PathParam("queryName") String query,
            @PathParam("offset") @DefaultValue("0") int offset,
            @PathParam("pageSize") @DefaultValue("10") int pageSize,
            @Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        containerId = getFullContainerId(containerId);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.get(interceptor, containerId);
        
        Map<String, Object> params = new HashMap<String, Object>();
        // TODO: Convert the parameters

        QueryRequest request = new DefaultQueryRequest(query, offset, pageSize, params, null, false, null);
        QueryResult result = container.executeQuery(request);
        List<ObjexObjStateBean> beans = interceptor.findObjects(result.getResults());

        return new ContainerResult(containerId, beans);
    }
    
    /**
     * This method allows the client to set the
     * state of the given object(s). Only the
     * simple state fields are updated.
     * 
     * @return The result containing any errors and the objects back
     */
    @POST
    @Path("/{containerId}/{objectType}/{objectId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult set(
            @PathParam("containerId") String containerId,
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId,
            Object bean) {
        containerId = getFullContainerId(containerId);
        ObjexID objId = new DefaultObjexID(objectType, objectId, true);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.open(interceptor, containerId);
        
        ObjexObj obj = container.getObject(objId); // Ensures it is loaded
        
        SimpleValidationRequest request = new SimpleValidationRequest(ValidationType.FIELD);
        try {
            CurrentValidationRequest.start(request);
            
            BeanObjectReader reader = new BeanObjectReader(true, true, true);
            reader.readObject(bean, obj);
            
            request.setType(ValidationType.INTRA_OBJECT);
            obj.validate(request);
        }
        finally {
            CurrentValidationRequest.end(null);
        }
        
        containerId = container.suspend();
        
        return new ContainerResult(containerId, interceptor.findObject(obj.getId()), request.getErrors());
    }
    
    /**
     * This method allows the client to create a new
     * object as a child of the given object. This is
     * only allowed if the field owns its members.
     * 
     * FUTURE: Support being able to set a type
     * 
     * @return The result containing any errors and the changed and new object
     */
    @PUT
    @Path("/{containerId}/{objectType}/{objectId}/create/{fieldName}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult create(
            @PathParam("containerId") String containerId, 
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId,
            @PathParam("fieldName") String fieldName,
            Object bean) {
        containerId = getFullContainerId(containerId);
        ObjexID objId = new DefaultObjexID(objectType, objectId, true);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.open(interceptor, containerId);
        ObjexObj obj = container.getObject(objId); // Ensures it is loaded
        ObjexObj newObj = obj.createReference(fieldName, null);
        ObjexObjStateBean newBean = interceptor.findObject(newObj.getId());
        
        if( bean != null ) {
            SimpleValidationRequest request = new SimpleValidationRequest(ValidationType.FIELD);
            try {
                CurrentValidationRequest.start(request);
                
                BeanObjectReader reader = new BeanObjectReader(true, true, true);
                reader.readObject(bean, newObj);
                
                request.setType(ValidationType.INTRA_OBJECT);
                obj.validate(request);
            }
            finally {
                CurrentValidationRequest.end(null);
            }
        }
        
        containerId = container.suspend();
        
        return new ContainerResult(containerId, newBean);
    }
    
    /**
     * This method allows the client to add a reference
     * to a reference field. This is not allowed if the
     * field holds an owned reference
     * 
     * @return The result containing the changed object
     */
    @POST
    @Path("/{containerId}/{objectType}/{objectId}/add/{fieldName}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult add(
            @PathParam("containerId") String containerId, 
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId,
            @PathParam("fieldName") String fieldName) {
        throw new UnsupportedOperationException("Cannot add, will probably remove, just use set");
    }
    
    /**
     * This method allows the client to remove a reference
     * to another object from the given field. If the field
     * owns it's references, the object is effectively 
     * removed. 
     * 
     * @return The result containing the changed object
     */
    @DELETE
    @Path("/{containerId}/{objectType}/{objectId}/removeIndex/{fieldName}/{index}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult removeIndex(
            @PathParam("containerId") String containerId, 
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId,
            @PathParam("fieldName") String fieldName,
            @PathParam("index") int index) {
        throw new UnsupportedOperationException("Cannot remove yet");
    }
    
    /**
     * This method allows the client to remove a reference
     * to another object from the given field. If the field
     * owns it's references, the object is effectively 
     * removed. 
     * 
     * @return The result containing the changed object
     */
    @DELETE
    @Path("/{containerId}/{objectType}/{objectId}/removeKey/{fieldName}/{key}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult removeKey(
            @PathParam("containerId") String containerId, 
            @PathParam("objectType") String objectType, 
            @PathParam("objectId") String objectId,
            @PathParam("fieldName") String fieldName,
            @PathParam("key") String key) {
        throw new UnsupportedOperationException("Cannot remove yet"); 
    }
    
    /**
     * This method allows the client to save the container in
     * its current state.
     * 
     * @return The middleware result containing any errors
     */
    @POST
    @Path("/{containerId}/save")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public ContainerResult save(@PathParam("containerId") String containerId) {
        containerId = getFullContainerId(containerId);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.open(interceptor, containerId);
        // TODO: If not open!?!
        
        try {
            containerId = container.saveContainer();
            return new ContainerResult(containerId);
        }
        catch( ContainerInvalidException e ) {
            List<ValidationError> errors = e.getRequest().getErrors();
            return new ContainerResult(containerId, errors, true);
        }
    }
}
