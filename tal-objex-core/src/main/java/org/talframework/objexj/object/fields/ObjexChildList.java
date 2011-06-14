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

import java.util.List;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.exceptions.ObjectInvalidException;

public class ObjexChildList<E> extends ObjexRefList<E> {

    public ObjexChildList(ObjexObj owningObject) {
        super(owningObject);
    }
    
    /**
     * Constructor for using with an existing set of references,
     * i.e. has been loaded from store.
     * 
     * @param owner The object that owns this set
     * @param refs The IDs of the objects already held
     */
    public ObjexChildList(ObjexObj owner, List<ObjexID> refs) {
        super(owner, refs);
    }
    
    /**
     * Overridden to remove any pre-exising object (if updating) and
     * to create the new object in the container.
     * 
     * FUTURE: If replace, could consider actually copy from obj to existing??
     */
    @Override
    protected ObjexRef setupRef(ObjexRef existing, E obj) {
        // If existing, remove it!
        if( existing != null ) {
            getInternalContainer().removeObject(existing.getObj(getContainer()));
        }
        
        // Ensure we are not dealing with an initialised objex obj
        if( obj instanceof ObjexObj && ((ObjexObj)obj).getContainer() != null ) {
            throw new ObjectInvalidException("Cannot add initialised ObjexObj as child");
        }
        
        // Turn the object into a real object
        ObjexObj newObject = getInternalContainer().createObject(getOwningObject(), obj);
        
        // Set on reference
        if( existing == null ) existing = new ObjexRef(newObject.getId());
        else existing.setId(newObject.getId());
        existing.setObj(newObject);
        return existing;
    }
    
    /**
     * Overridden to physically remove the object from the container
     */
    @Override
    public E remove(int index) {
        ObjexRef ref = getRef(index, true);
        getInternalContainer().removeObject(ref.getObj(getContainer()));
        return super.remove(index);
    }
}
