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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.object.xml.XmlObjexObj;

/**
 * This class represents the root element when returning a
 * container from a web service.
 *
 * @author Tom Spencer
 */
@XmlRootElement(name="result")
@XmlType(propOrder={"containerId", "partial", "object"})
public class DocumentResult {

    /** Holds the containers ID */
    private String containerId;
    /** If true then this is a partial container */
    private boolean partial;
    /** The root object of the result */
    private ObjexObj object;
    
    public DocumentResult() {}
    
    public DocumentResult(String id, boolean partial, ObjexObj obj) {
        this.containerId = id;
        this.partial = partial;
        this.object = obj;
    }

    /**
     * @return the obj
     */
    @XmlElement(type=XmlObjexObj.class)
    public ObjexObj getObject() {
        return object;
    }

    /**
     * Setter for the obj field
     *
     * @param obj the obj to set
     */
    public void setObject(ObjexObj obj) {
        this.object = obj;
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
     * @return the partial
     */
    @XmlAttribute
    public boolean isPartial() {
        return partial;
    }

    /**
     * Setter for the partial field
     *
     * @param partial the partial to set
     */
    public void setPartial(boolean partial) {
        this.partial = partial;
    }
}
