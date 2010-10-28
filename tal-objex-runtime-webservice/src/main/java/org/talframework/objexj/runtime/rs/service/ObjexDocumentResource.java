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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.runtime.rs.DocumentResult;
import org.talframework.tal.aspects.annotations.Profile;
import org.talframework.tal.aspects.annotations.Trace;

/**
 * This Restful WebService exposes the container as a Document.
 * Typically Stores are not exposed vai thia Web Service.
 * 
 * <p>As with all the Objex Restful WebServices you must derived
 * from this class and provide the root path to expose on. 
 * For instance:</p>
 * 
 * <code><pre>
 * @Path("/someRoot")
 * public class MyContainerResource extends ObjexDocumentResource {
 *      
 * }
 * </pre></code>
 * 
 * @author Tom Spencer
 */
public abstract class ObjexDocumentResource {
    
    private final String containerType;
    private final ContainerFactory factory;
    
    public ObjexDocumentResource(String type, ContainerFactory factory) {
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
     * Gets the full container from the root object.
     * 
     * @param containerId The containerId to get
     * @return The result to then output
     */
    @GET
    @Path("/{containerId}")
    @Trace @Profile
    public DocumentResult get(@PathParam("containerId") String containerId) {
        Container container = factory.get(getFullContainerId(containerId));
        return new DocumentResult(container.getId(), false, container.getRootObject());
    }
    
    /**
     * Gets only a particular object from the root and
     * then outputs it (and its children).
     * 
     * @param containerId The containerId
     * @param type The type of object
     * @param id The ID of the object
     * @return The result to then output
     */
    @GET
    @Path("/{containerId}/{objectType}/{objectId}")
    @Trace @Profile
    public DocumentResult getObject(@PathParam("containerId") String containerId, @PathParam("objectType") String type, @PathParam("objectId") String id) {
        ObjexID objId = new DefaultObjexID(type, id);
        Container container = factory.get(getFullContainerId(containerId));
        return new DocumentResult(container.getId(), true, container.getObject(objId));
    }
}
