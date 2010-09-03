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

package org.talframework.objexj.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.DefaultObjexID;
import org.talframework.objexj.container.InternalContainer;

/**
 * This class contains various helper methods that can be
 * used to implement Objex Objects.
 * 
 * @author Tom Spencer
 */
public class ObjectUtils {
    
    /**
     * Given an object we expect to be a real ObjexObj
     * return its ID.
     * 
     * @param obj The object (either an ObjexObj or a obj)
     * @return The id of the object
     */
    public static ObjexID getObjectId(Object obj) {
        if( obj instanceof ObjexObj ) return ((ObjexObj)obj).getId();
        else if( obj instanceof ObjexID ) return ((ObjexID)obj);
        else return null; // TODO: Throw exception
    }
    
    /**
     * Given an object we expect to be a real ObjexObj
     * return its ID as a reference
     * 
     * @param obj The object (either an ObjexObj or a obj)
     * @return The id of the object
     */
    public static String getObjectRef(Object obj) {
        ObjexID ret = getObjectId(obj);
        return ret != null ? ret.toString() : null;
    }
    
    /**
     * Given an id that is either a stringified refernece or
     * an ObjexID directly return its real ID. The object is
     * treated as local to the provided objex obj
     * 
     * @param obj An ObjexObj that is requesting this service
     * @param id The ID/reference
     * @return The real objex ID
     */
    public static ObjexID getObjectId(ObjexObj obj, Object id) {
        return DefaultObjexID.getId(id);
    }
    
    /**
     * Given an id that is either a stringified refernece or
     * an ObjexID directly return its real ID. The object is
     * treated as local to the provided objex obj
     * 
     * @param obj An ObjexObj that is requesting this service
     * @param id The ID
     * @return The id as a stringified reference
     */
    public static String getObjectRef(ObjexObj obj, Object id) {
        ObjexID ret = getObjectId(obj, id);
        return ret != null ? ret.toString() : null;
    }
    
    /**
     * Call to get an object given its ID from the container
     * that holds the object.
     * 
     * @param obj The requesting object
     * @param id The ID of the object to get
     * @param expected The type of object expected
     * @return The object
     */
    public static <T> T getObject(ObjexObj obj, Object id, Class<T> expected) {
        return obj.getContainer().getObject(id, expected);
    }
    
    /**
     * Call to get a list of objects given their references
     * and the object which owns this list.
     * 
     * TODO: Inefficient, use container method!?!
     * 
     * @param obj The object that owns the list
     * @param refs The references
     * @param expected The expected base type of the references
     * @return The returned list
     */
    public static <T> List<T> getObjectList(ObjexObj obj, List<String> refs, Class<T> expected) {
        if( refs == null ) return null;
        
        ArrayList<T> ret = new ArrayList<T>();
        
        Container container = obj.getContainer();
        Iterator<String> it = refs.iterator(); 
        while( it.hasNext() ) {
            T t = container.getObject(it.next(), expected);
            if( t != null ) ret.add(t);
        }
        
        return ret;
    }
    
    /**
     * Test that an object is held by the list of references.
     * 
     * @param refs The refs that should hold ID
     * @param id The ID of the object we are after
     * @return True if the id is held
     * @throws IllegalArgumentException If not held
     */
    public static boolean testObjectHeld(List<String> refs, Object id) {
        boolean ret = refs != null ? refs.contains(id.toString()) : false;
        if( !ret ) throw new IllegalArgumentException("The object [" + id + "] is not held so cannot be returned");
        return ret;
    }
    
    /**
     * Call to create an object of the given type with a
     * parent of the given object.
     * 
     * @param obj The parent object
     * @param currentState The current state of the parent object
     * @param type The type of object to create
     * @return The new object
     */
    public static ObjexObj createObject(ObjexObj obj, ObjexObjStateBean currentState, String type) {
        Container container = obj.getContainer();
        if( container instanceof InternalContainer ) {
            return ((InternalContainer)container).newObject(obj, currentState, type);
        }
        else {
            // TODO: Throw an exception!!!
            return null;
        }
    }
    
    /**
     * Call to remove an object from the container.
     * 
     * @param obj The object we are removing from
     * @param currentState The current state of the parent object
     * @param id The ID of the object to remove
     */
    public static void removeObject(ObjexObj obj, ObjexObjStateBean currentState, String id) {
        Container container = obj.getContainer();
        
        ObjexObj objToRemove = container.getObject(id);
        // TODO: There should be a method on the object to do this!
        if( objToRemove != null && container instanceof InternalContainer ) {
            ((InternalContainer)container).removeObject(objToRemove, currentState);
        }
        else {
            // TODO: Throw an exception
        }
    }
    
    /**
     * Helper to return the true reference replacing any temp
     * reference with the new real ID.
     * 
     * @param ref The reference
     * @param tempRefs The temp references
     * @return The new reference (or old one if not a temp reference)
     */
    public static String updateTempReferences(String ref, Map<ObjexID, ObjexID> tempRefs) {
        if( ref == null ) return ref;
        if( tempRefs == null || tempRefs.size() == 0 ) return ref;
        else if( tempRefs.containsKey(ref) ) return tempRefs.get(ref).toString();
        else return ref;
    }
    
    /**
     * Helper to update any temp IDs in a list of objex object
     * references. 
     * 
     * @param refs The references
     * @param tempRefs The temp IDs and their real counterparts
     * @return The amended list
     */
    public static List<String> updateTempReferences(List<String> refs, Map<ObjexID, ObjexID> tempRefs) {
        if( refs == null ) return refs;
        if( tempRefs == null || tempRefs.size() == 0 ) return refs;
        
        int ln = refs.size();
        for( int i = 0 ; i < ln ; i++ ) {
            String ref = refs.get(i);
            if( tempRefs.containsKey(ref) ) refs.set(i, tempRefs.get(ref).toString());
        }
        
        return refs;
    }
    
    /**
     * Helper to replace all temporary references inside a map
     * with the new real references.
     * 
     * @param refs The reference map
     * @param tempRefs The temp references and their real ID counterpart
     * @return The amended map of references
     */
    public static Map<?, String> updateTempReferences(Map<String, String> refs, Map<ObjexID, ObjexID> tempRefs) {
        if( refs == null ) return refs;
        if( tempRefs == null || tempRefs.size() == 0 ) return refs;
        
        Iterator<String> it = refs.keySet().iterator();
        while( it.hasNext() ) {
            String key = it.next();
            String ref = refs.get(key);
            if( tempRefs.containsKey(ref) ) refs.put(key, tempRefs.get(ref).toString());
        }
        
        return refs;
    }
}
