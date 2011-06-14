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
