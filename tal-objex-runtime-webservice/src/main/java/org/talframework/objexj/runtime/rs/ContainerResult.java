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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.runtime.rs.service.ObjexContainerResource;

/**
 * This object represents a result from interacting with the
 * {@link ObjexContainerResource} Web Service.
 *
 * @author Tom Spencer
 */
@XmlRootElement(name="container-result")
public class ContainerResult {

    /** Holds the containers ID - always provided and will change due to suspending transactions */
    private String containerId;
    /** Holds the requested object (or objects if query/property) or the created/updated object back */
    private List<ObjexObjStateBean> objects;
    /** Holds any validation errors in fulfilling the request */
    private List<ValidationError> errors;
    
    public ContainerResult() {}
    
    /**
     * Constructs a successful response containing just
     * the containerId.
     * 
     * @param containerId The containerId
     */
    public ContainerResult(String containerId) {
        this.containerId = containerId;
    }
    
    /**
     * Constructs a response containing the object requested
     * 
     * @param containerId The containerId
     * @param object The object
     */
    public ContainerResult(String containerId, ObjexObjStateBean bean) {
        this.containerId = containerId;
        this.objects = new ArrayList<ObjexObjStateBean>();
        this.objects.add(bean);
    }
    
    /**
     * Constructs a response containing the object that has
     * been created/updated and any errors it now has.
     * 
     * @param containerId The containerId
     * @param object The object
     * @param errors The errors
     */
    public ContainerResult(String containerId, ObjexObjStateBean bean, List<ValidationError> errors) {
        this.containerId = containerId;
        this.objects = new ArrayList<ObjexObjStateBean>();
        this.objects.add(bean);
        this.errors = errors;
    }
    
    /**
     * Constructs a response containing the objects requested
     * 
     * @param containerId The containerId
     * @param objects The objects
     */
    public ContainerResult(String containerId, List<ObjexObjStateBean> objects) {
        this.containerId = containerId;
        this.objects = objects;
    }
    
    /**
     * Constructs an error response
     * 
     * @param containerId The containerId
     * @param errors The errors when validating
     * @param hasErrors Unused, keeps the constructor separate from others
     */
    public ContainerResult(String containerId, List<ValidationError> errors, boolean hasErrors) {
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
