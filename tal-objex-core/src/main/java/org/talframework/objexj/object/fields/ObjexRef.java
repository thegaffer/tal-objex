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
package org.talframework.objexj.object.fields;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;

/**
 * This fairly simple class is held by the Objex collections
 * implementations as the value. Each instance holds a reference
 * and then an optional cached object, which it will get as
 * needed if the container is provided. The only other piece
 * of logic is that if the ID) is changed then any cached object
 * is cleared automatically.
 *
 * @author Tom Spencer
 */
class ObjexRef {
    /** The reference to the object */
    private ObjexID id;
    /** The (cached) object itself */
    private ObjexObj obj;
    
    public ObjexRef(ObjexID id) {
        this.id = id;
        this.obj = null;
    }
    
    /**
     * @return the id
     */
    public ObjexID getId() {
        return id;
    }
    
    /**
     * Setter for the id field
     *
     * @param ref the id to set
     */
    public void setId(ObjexID id) {
        this.id = id;
        this.obj = null;
    }
    
    /**
     * @return the obj
     */
    public ObjexObj getObj(Container container) {
        if( obj == null ) obj = container.getObject(id);
        return obj;
    }
    
    /**
     * Setter for the obj field
     *
     * @param obj the obj to set
     */
    public void setObj(ObjexObj obj) {
        this.obj = obj;
    }
}