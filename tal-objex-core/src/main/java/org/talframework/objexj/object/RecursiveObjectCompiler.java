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
package org.talframework.objexj.object;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.ObjexStateWriter;

/**
 * This class produces a list of navigable objects starting with
 * 1 object. This class uses the {@link ObjexStateWriter} to get
 * the objects to write out their state and then discards all
 * but the referenced objects.
 * 
 * <p>By default this class only considers persistent owned 
 * objects, but it can be set to include referenced objects and
 * those which are not persisted (i.e. calculated or derived).
 * The compiler can be set to walk to any depth, but the default
 * is 1 - this means add the initial object and all of its
 * direct children and/or referenced objects, but go no 
 * further.</p>
 * 
 * @author Tom Spencer
 */
public final class RecursiveObjectCompiler implements ObjexStateWriter {
    
    /** Holds the container we get child objects from */
    private final Container container;
    /** If true then passed to the objects to tell them to include non-persistent fields */
    private boolean includeNonPersistent = false;
    /** Determines if owned objects should be considered, default it true */
    private boolean includeOwned = true;
    /** Determines if referenced objects should be considered, default is true */
    private boolean includeReferenced = true;
    /** The depth to recurse through the objects, the default is 1 */
    private int recurseDepth = 1;
    
    /** Holds the objects we find */
    private Map<ObjexID, ObjexObj> referencedObjects = null;

    /**
     * Constructs the writer with given settings and container
     * 
     * @param container The container to get objects from
     */
    public RecursiveObjectCompiler(Container container) {
        this.container = container;
        referencedObjects = new HashMap<ObjexID, ObjexObj>();
    }
    
    /**
     * The default is to not included non-persistent fields, call
     * this to include those fields.
     */
    public void setIncludeNonPersistent() {
        includeNonPersistent = true;
    }
    
    /**
     * The default is to include all objects, call this
     * to ignore referenced objects from same container.
     */
    public void setIgnoreReferenced() {
        includeReferenced = false;
    }
    
    /**
     * The default is to include all owned child objects, call
     * this to ignore these.
     */
    public void setIgnoreOwned() {
        includeOwned = false;
    }
    
    /**
     * Call this to set a recursive depth. A depth of 0 means
     * the current object only, whilst a depth of 1 means the
     * current objects and it's child objects (and references).
     * A depth of -1 means all navigable objects. The default 
     * is a depth of 1. There is little point in setting the
     * depth of 0 and using this class.
     * 
     * @param depth The depth to set to
     */
    public void setRecurseDepth(int depth) {
        recurseDepth = depth;
    }
    
    /**
     * Call to add the passed in object to the map of objects
     * and all its referenced objects as configured. Then
     * returns that map.
     * 
     * @param obj The object to start at
     * @return The map of objects
     */
    public Map<ObjexID, ObjexObj> getObjects(ObjexObj obj) {
        referencedObjects.put(obj.getId(), obj);
        
        // Find its children if we are recursing
        if( recurseDepth != 0 ) obj.acceptWriter(this, includeNonPersistent);
        
        return referencedObjects;
    }
    
    private void writeNavigableObject(ObjexObj obj) {
        if( recurseDepth == 0 ) return; // Shouldn't happen, but just in case
        
        --recurseDepth;
        getObjects(obj);
        ++recurseDepth;
    }
    
    /**
     * Does nothing as only interested in ref props
     */
    public void write(String name, Object obj, ObjexFieldType type, boolean persistent) {
        // No-op
    }
    
    /**
     * Ensures referenced object is in map of objects if configured to be there
     */
    public void writeReference(String name, Object ref, ObjexFieldType type, boolean persistent) {
        if( !includeOwned && type == ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeReferenced && type == ObjexFieldType.REFERENCE ) return;
        if( ref == null ) return;
        if( recurseDepth == 0 ) return;
        
        ObjexObj innerObj = null;
        if( ref instanceof ObjexObj ) innerObj = (ObjexObj)ref;
        else innerObj = container.getObject(ref);
        
        if( innerObj != null && !referencedObjects.containsKey(innerObj.getId()) ) {
            writeNavigableObject(innerObj);
        }
    }
    
    /**
     * Ensures referenced objects are in map of objects if configured to be there
     */
    public void writeReferenceList(String name, java.util.List<?> refs, ObjexFieldType type, boolean persistent) {
        if( !includeOwned && type == ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeReferenced && type == ObjexFieldType.REFERENCE ) return;
        if( refs == null || refs.size() == 0 ) return;
        if( recurseDepth == 0 ) return;
        
        for( Object ref : refs ) {
            ObjexObj innerObj = null;
            if( ref instanceof ObjexObj ) innerObj = (ObjexObj)ref;
            else innerObj = container.getObject(ref);
            
            if( innerObj != null && !referencedObjects.containsKey(innerObj.getId()) ) {
                writeNavigableObject(innerObj);
            }
        }
    }
    
    @Override
    public void writeReferenceSet(String name, Set<?> refs, ObjexFieldType type, boolean persistent) {
        if( !includeOwned && type == ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeReferenced && type == ObjexFieldType.REFERENCE ) return;
        if( refs == null || refs.size() == 0 ) return;
        if( recurseDepth == 0 ) return;
        
        for( Object ref : refs ) {
            ObjexObj innerObj = null;
            if( ref instanceof ObjexObj ) innerObj = (ObjexObj)ref;
            else innerObj = container.getObject(ref);
            
            if( innerObj != null && !referencedObjects.containsKey(innerObj.getId()) ) {
                writeNavigableObject(innerObj);
            }
        }
    }
    
    /**
     * Ensures referenced objects are in map of objects if configured to be there
     */
    public void writeReferenceMap(String name, java.util.Map<String,?> refs, ObjexFieldType type, boolean persistent) {
        if( !includeOwned && type == ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeReferenced && type == ObjexFieldType.REFERENCE ) return;
        if( refs == null || refs.size() == 0 ) return;
        if( recurseDepth == 0 ) return;
        
        for( String ref : refs.keySet() ) {
            ObjexObj innerObj = null;
            Object refValue = refs.get(ref);
            if( refValue instanceof ObjexObj ) innerObj = (ObjexObj)refValue;
            else innerObj = container.getObject(refValue);
            
            if( innerObj != null && !referencedObjects.containsKey(innerObj.getId()) ) {
                writeNavigableObject(innerObj);
            }
        }
    }
    
    @Override
    public String toString() {
        return "FlatRecursiveWriter: includeChildren" + includeOwned + ", includeReferenced=" + includeReferenced;
    }
}
