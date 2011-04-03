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
