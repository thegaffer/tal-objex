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
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationError;

@XmlRootElement(name="middleware-result")
public class MiddlewareResult {

    /** Holds the ID of the container (or transactionId if suspended) */
    private String containerId;
    /** Holds the objects requested */
    private List<ObjexObjStateBean> objects;
    /** Holds the new references to any objects that have been created (if posted or put) */
    private Map<String, String> newReferences;
    /** Holds any validation errors in fulfilling the request */
    private List<ValidationError> errors;
    
    public MiddlewareResult() {}
    
    /**
     * Constructs a successful response containing just
     * the containerId.
     * 
     * @param containerId The containerId
     */
    public MiddlewareResult(String containerId) {
        this.containerId = containerId;
    }
    
    /**
     * Constructs a response containing the object requested
     * 
     * @param containerId The containerId
     * @param object The object
     */
    public MiddlewareResult(String containerId, ObjexObjStateBean bean) {
        this.containerId = containerId;
        this.objects = new ArrayList<ObjexObjStateBean>();
        this.objects.add(bean);
    }
    
    /**
     * Constructs a response containing the objects requested
     * 
     * @param containerId The containerId
     * @param objects The objects
     */
    public MiddlewareResult(String containerId, List<ObjexObjStateBean> objects) {
        this.containerId = containerId;
        this.objects = objects;
    }
    
    /**
     * Constructs a response that updated including new references
     * 
     * @param containerId The containerId
     * @param newReferences The new references
     */
    public MiddlewareResult(String containerId, Map<String, String> newReferences) {
        this.containerId = containerId;
        this.newReferences = newReferences;
    }
    
    /**
     * Constructs an error response
     * 
     * @param containerId The containerId
     * @param errors The errors when validating
     * @param hasErrors Unused, keeps the constructor separate from others
     */
    public MiddlewareResult(String containerId, List<ValidationError> errors, boolean hasErrors) {
        this.containerId = containerId;
        this.errors = errors;
    }

    /**
     * @return the objects
     */
    @XmlElement(type=XmlStateBean.class)
    @XmlJavaTypeAdapter(value=XmlStateBeanAdapter.class)
    public List<ObjexObjStateBean> getObjects() {
        return objects;
    }
    
    public void setObjects(List<ObjexObjStateBean> objects) {
        this.objects = objects;
    }

    /**
     * @return the containerId
     */
    @XmlAttribute
    public String getContainerId() {
        return containerId;
    }

    /**
     * Setter for the containerId field
     *
     * @param containerId the containerId to set
     */
    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * @return the newReferences
     */
    //@XmlElement
    @XmlTransient // TODO: Remove transient
    public Map<String, String> getNewReferences() {
        return newReferences;
    }

    /**
     * Setter for the newReferences field
     *
     * @param newReferences the newReferences to set
     */
    public void setNewReferences(Map<String, String> newReferences) {
        this.newReferences = newReferences;
    }

    /**
     * @return the errors
     */
    //@XmlElement
    @XmlTransient // TODO: Remove transient
    public List<ValidationError> getErrors() {
        return errors;
    }

    /**
     * Setter for the errors field
     *
     * @param errors the errors to set
     */
    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }
}
