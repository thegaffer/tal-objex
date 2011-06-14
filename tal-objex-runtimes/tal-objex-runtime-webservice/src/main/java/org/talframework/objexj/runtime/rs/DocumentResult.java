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
@XmlRootElement(name="document-result")
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
