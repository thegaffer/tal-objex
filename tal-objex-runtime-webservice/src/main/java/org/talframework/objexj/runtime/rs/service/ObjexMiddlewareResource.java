/*
 * Copyright 2010 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.runtime.rs.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.container.TransactionCache.ObjectRole;
import org.talframework.objexj.exceptions.ContainerInvalidException;
import org.talframework.objexj.locator.InterceptingContainerFactory;
import org.talframework.objexj.object.RecursiveObjectCompiler;
import org.talframework.objexj.object.writer.BeanReader;
import org.talframework.objexj.runtime.rs.MiddlewareRequest;
import org.talframework.objexj.runtime.rs.MiddlewareResult;
import org.talframework.tal.aspects.annotations.Profile;
import org.talframework.tal.aspects.annotations.Trace;

/**
 * This Restful WebService is aimed at exposing a container to
 * a remote middleware. Essentially this allows the remote
 * client to make and orchestrate changes and then post all
 * those changes to this service where they are acted upon.
 * This service exposes the state beans rather than the full
 * Objex objects as exposed in the {@link ObjexDocumentResource}
 * service.
 * 
 * TODO: Get specific object
 * TODO: Get objects from property of object
 * TODO: Run query (more complex as objects not known)
 * TODO: Generating New IDs is not currently done!?!
 * 
 * @author Tom Spencer
 */
public abstract class ObjexMiddlewareResource {
    
    private final String containerType;
    private final InterceptingContainerFactory factory;
    
    public ObjexMiddlewareResource(String type, InterceptingContainerFactory factory) {
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
     * Gets all objects in a container from the root with the
     * specified depth.
     *  
     * @param containerId
     * @param depth
     * @return
     */
    @GET
    @Path("/{containerId}")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public MiddlewareResult get(
            @PathParam("containerId") String containerId, 
            @QueryParam("depth") @DefaultValue("1") int depth) {
        containerId = getFullContainerId(containerId);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.get(interceptor, containerId);
        
        List<ObjexObjStateBean> objs = interceptor.findObjects(getObjects(container, null, depth).keySet());
        return new MiddlewareResult(container.getId(), objs);
    }
    
    /**
     * Call to post a set of updates to an existing container
     * and then save the container. The input request can
     * contain new, updated or removed objects. The changes
     * are applied to the existing container via the business
     * object - so additional processing etc is preserved. 
     * 
     * @param containerId
     * @param request
     * @return
     */
    @POST
    @Path("/{containerId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public MiddlewareResult post(
            @PathParam("containerId") String containerId, 
            MiddlewareRequest request) {
        containerId = getFullContainerId(containerId);
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.open(interceptor, containerId);
        
        try {
            Map<String, String> newRefs = processRequest(container, interceptor, request);
            
            containerId = container.saveContainer();
            return new MiddlewareResult(containerId, newRefs);
        }
        catch( ContainerInvalidException e ) {
            return new MiddlewareResult(null, e.getRequest().getErrors(), true);
        }
    }
    
    /**
     * Call to create a new container and add all the given objects
     * into this container. Other than creating the container acts
     * just as is post() above.
     * 
     * @param request The request holding the object
     * @return The result holding ID of container and any errors
     */
    @PUT
    @Path("/create")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Trace @Profile
    public MiddlewareResult put(MiddlewareRequest request ) {
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = factory.create(interceptor);
        
        try {
            Map<String, String> newRefs = processRequest(container, interceptor, request);
            
            String containerId = container.saveContainer();
            return new MiddlewareResult(containerId, newRefs);
        }
        catch( ContainerInvalidException e ) {
            return new MiddlewareResult(null, e.getRequest().getErrors(), true);
        }
    }
    
    /**
     * Helper method to process a post or put request.
     * 
     * @param container The container
     * @param interceptor The interceptor
     * @param request The request
     */
    private Map<String, String> processRequest(Container container, WebServiceInterceptor interceptor, MiddlewareRequest request) {
        TransactionCache cache = interceptor.getCache();
        
        // Add in any new objects directly to the cache
        List<ObjexObjStateBean> beans = request.getNewObjects();
        if( beans != null ) {
            for( ObjexObjStateBean bean : beans ) {
                // FUTURE: Do we need to generate a new ID
                cache.addObject(ObjectRole.NEW, DefaultObjexID.getId(bean.getId()), bean);
            }
        }
        
        // Remove any deleted objects directly
        List<ObjexID> removedBeans = request.getRemovedObjects();
        if( removedBeans != null ) {
            for( ObjexID id : removedBeans ) {
                cache.addObject(ObjectRole.REMOVED, id, interceptor.findObject(container.getObject(id).getId()));
            }
        }
        
        // Read in each new and updated object
        BeanReader reader = new BeanReader();
        if( beans != null ) beans.addAll(request.getObjects());
        else beans = request.getObjects();
        if( beans != null ) {
            for( ObjexObjStateBean bean : beans ) {
                ObjexObj obj = container.getObject(bean.getId());
                reader.readObject(bean, obj);
            }
        }
    
        return null;
    }
    
    /**
     * Helper to get objects from the container.
     * 
     * @param container
     * @param obj
     * @param depth
     * @return
     */
    private Map<ObjexID, ObjexObj> getObjects(Container container, ObjexObj obj, int depth) {
        if( obj == null ) obj = container.getRootObject();
        
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        compiler.setRecurseDepth(depth);
        return compiler.getObjects(obj);
    }
}
