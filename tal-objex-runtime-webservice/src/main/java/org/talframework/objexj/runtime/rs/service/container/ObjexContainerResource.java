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

package org.talframework.objexj.runtime.rs.service.container;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.runtime.rs.ContainerResult;

/**
 * This Restful WebService exposes a container in its natural
 * form. Consequently it does not support updates at present.
 * This class is a base class and you must derive from it and
 * provide both the container factory and annotate your class
 * with @Path to set the root path this webservice responds to.
 *
 * @author Tom Spencer
 */
public abstract class ObjexContainerResource {
    
    /**
     * This must be implemented by the derived class to get
     * the actual container factory.
     * 
     * @return The container factory to get instances from
     */
    protected abstract ContainerFactory getFactory();
    
    @GET
    @Path("/{containerId}")
    public ContainerResult get(@PathParam("containerId") String containerId) {
        Container container = getFactory().get(containerId);
        return new ContainerResult(container.getId(), false, container.getRootObject());
    }
    
    /*@GET
    @Path("/{containerId}/{objectType}/{objectId}")
    public ObjexObj getObject(@PathParam("containerId") String containerId, @PathParam("objectType") String type, @PathParam("objectId") long id) {
        ObjexID objId = new DefaultObjexID(type, id);
        Container container = getFactory().get(containerId);
        return container.getObject(objId);
    }*/
    
    @GET
    @Path("/{containerId}/{objectType}/{objectId}")
    public ContainerResult getObject(@PathParam("containerId") String containerId, @PathParam("objectType") String type, @PathParam("objectId") String id) {
        ObjexID objId = new DefaultObjexID(type, id);
        Container container = getFactory().get(containerId);
        return new ContainerResult(container.getId(), true, container.getObject(objId));
    }
}