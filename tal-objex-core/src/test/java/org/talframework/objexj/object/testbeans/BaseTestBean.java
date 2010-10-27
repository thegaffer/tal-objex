/*
 * Copyright 2009 Thomas Spencer
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

package org.talframework.objexj.object.testbeans;

import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.util.beans.cloner.Cloner;
import org.talframework.util.beans.cloner.GenericCloner;

/**
 * This class is the base for all of the test beans we use
 * when testing ObjexCore. It implements most of the
 * {@link ObjexObjStateBean} interface to keep the test
 * beans as simple as possible.
 *
 * @author Tom Spencer
 */
public abstract class BaseTestBean implements ObjexObjStateBean {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String parentId;
    private boolean editable = false;
    
    /**
     * Sets the parentId and then sets the object to be editable
     */
    public final void create(ObjexID parentId) {
        if( parentId != null ) this.parentId = parentId.toString();
        editable = true;
    }
    
    /**
     * Sets the id member to this value
     */
    public final void preSave(Object id) {
        this.id = id.toString();
    }

    /**
     * Simply returns the variable
     */
    public final Object getId() {
        return id;
    }
    
    /**
     * Here only to setup in tests.
     * 
     * @param id The id to set to
     */
    public final void setId(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    /**
     * Simply returns the variable
     */
    public final String getParentId() {
        return parentId;
    }
    
    /**
     * Here only to setup in tests.
     * 
     * @param id The id to set to
     */
    public final void setParentId(String id) {
        this.parentId = id;
    }
    
    /**
     * Uses the class name as the type of object minus the Bean
     * suffix if one exists.
     */
    public String getObjexObjType() {
        String ret = this.getClass().getSimpleName();
        if( ret.endsWith("Bean") ) ret = ret.substring(0, ret.length() - 4);
        return ret;
    }
    
    /**
     * Returns the editable member
     */
    public final boolean isEditable() {
        return editable;
    }
    
    /**
     * Sets this bean to be editable
     */
    public final void setEditable() {
        editable = true;
    }
    
    /**
     * Considered a no-op for simple testing, but can be overridden
     * in derived class as needed
     */
    public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
        // No-op
    }
    
    /**
     * Uses the bean utils wrapper to clone the object
     */
    public ObjexObjStateBean cloneState() {
        Cloner cloner = new GenericCloner();
        return cloner.shallowClone(this);
    }
}
