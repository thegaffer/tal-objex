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
package org.talframework.objexj.runtime.rs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;

/**
 * This class represents a request to change some objects
 * inside a container.
 *
 * @author Tom Spencer
 */
@XmlRootElement(name="request")
public class MiddlewareRequest {

    /** Holds any new objects */
    private List<ObjexObjStateBean> newObjects;
    /** Holds any changed objects */
    private List<ObjexObjStateBean> objects;
    /** Holds any removed objects */
    private List<ObjexID> removedObjects;
    
    /**
     * Default constructor
     */
    public MiddlewareRequest() {}
    
    /**
     * Constructs a request to just update some objects or
     * create new ones in a put
     * 
     * @param objects The objects
     * @param newObjects If true the objects are treated as new ones
     */
    public MiddlewareRequest(List<ObjexObjStateBean> objects, boolean newObjects) {
        if( newObjects ) this.newObjects = objects;
        else this.objects = objects;
    }
    
    /**
     * Full constructor that can pass updated, new and removed objects.
     * 
     * @param objects The updated objects
     * @param newObjects The new objects
     * @param removedObjects The removed objects
     */
    public MiddlewareRequest(List<ObjexObjStateBean> objects, List<ObjexObjStateBean> newObjects, List<ObjexID> removedObjects) {
        this.objects = objects;
        this.newObjects = newObjects;
        this.removedObjects = removedObjects;
    }

    /**
     * @return the objects
     */
    @XmlElement
    @XmlJavaTypeAdapter(value=XmlStateBeanAdapter.class)
    public List<ObjexObjStateBean> getObjects() {
        return objects;
    }
    
    public void setObjects(List<ObjexObjStateBean> objects) {
        this.objects = objects;
    }
    
    public void setObjects(ObjexObjStateBean... objects) {
        this.objects = new ArrayList<ObjexObjStateBean>();
        for( ObjexObjStateBean obj : objects ) {
            this.objects.add(obj);
        }
    }

    /**
     * @return the newObjects
     */
    @XmlElement
    @XmlJavaTypeAdapter(value=XmlStateBeanAdapter.class)
    public List<ObjexObjStateBean> getNewObjects() {
        return newObjects;
    }

    /**
     * Setter for the newObjects field
     *
     * @param newObjects the newObjects to set
     */
    public void setNewObjects(List<ObjexObjStateBean> newObjects) {
        this.newObjects = newObjects;
    }

    /**
     * @return the removedObjects
     */
    @XmlElement
    @XmlJavaTypeAdapter(value=XmlStateBeanAdapter.class)
    public List<ObjexID> getRemovedObjects() {
        return removedObjects;
    }

    /**
     * Setter for the removedObjects field
     *
     * @param removedObjects the removedObjects to set
     */
    public void setRemovedObjects(List<ObjexID> removedObjects) {
        this.removedObjects = removedObjects;
    }
}
