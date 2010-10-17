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

package org.talframework.objexj.runtime.rs.service.middleware;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.container.TransactionCache.ObjectRole;
import org.talframework.objexj.locator.InterceptingContainerFactory;
import org.talframework.objexj.object.RecursiveObjectCompiler;
import org.talframework.objexj.runtime.rs.MiddlewareRequest;
import org.talframework.objexj.runtime.rs.MiddlewareResult;
import org.talframework.objexj.runtime.rs.service.WebServiceInterceptor;
import org.talframework.objexj.runtime.rs.service.container.ObjexContainerResource;

/**
 * This Restful WebService exposes a container to allow a client
 * to fully interact with the Objex Container, included get and
 * updates (POST and PUT). This service directly exposes the
 * state beans rather than the full Objex objects as exposed in
 * the {@link ObjexContainerResource} web service.
 *
 * @author Tom Spencer
 */
public abstract class ObjexMiddlewareResource {
    
    /**
     * This must be implemented by the derived class to get
     * the actual container factory.
     * 
     * @return The container factory to get instances from
     */
    protected abstract InterceptingContainerFactory getFactory();

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
    public MiddlewareResult get(
            @PathParam("containerId") String containerId, 
            @QueryParam("depth") @DefaultValue("1") int depth) {
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = getFactory().get(interceptor, containerId);
        ObjexObj obj = container.getRootObject();
        
        RecursiveObjectCompiler compiler = new RecursiveObjectCompiler(container);
        compiler.setRecurseDepth(depth);
        
        MiddlewareResult ret = new MiddlewareResult();
        ret.setObjects(interceptor.findObjects(compiler.getObjects(obj).keySet()));
        return ret;
    }

    @POST
    @Path("/{containerId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MiddlewareResult post(
            @PathParam("containerId") String containerId, 
            MiddlewareRequest request) {
        
        WebServiceInterceptor interceptor = new WebServiceInterceptor();
        Container container = getFactory().open(interceptor, containerId);
        
        // TODO: Need to process new, updated and removed objects separately
        TransactionCache cache = interceptor.getCache();
        for( ObjexObjStateBean bean : request.getObjects() ) {
            cache.addObject(ObjectRole.UPDATED, DefaultObjexID.getId(bean.getId()), bean);
        }
        
        // TODO: Need option to suspect
        container.saveContainer();
        
        MiddlewareResult ret = new MiddlewareResult();
        return ret;
    }
}
