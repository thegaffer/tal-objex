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
package org.talframework.objexj.object.utils;


/**
 * This static class contains various methods that the
 * generated code makes use of when dealing with
 * reference map fields. It can be used without using 
 * the generators if required.
 *
 * @author Tom Spencer
 */
public final class ReferenceMapFieldUtils extends FieldUtils {
    
    /**
     * Call to get a reference property
     *//*
    public static <T> Map<String, T> getMap(BaseObjexObj obj, Class<T> expected, Map<String, String> refs) {
        return obj.getContainer().getObjectMap(refs, expected);
    }

    *//**
     * Helper to set a reference list against a set of incoming objects 
     *//*
    public static <T> Map<String, String> setMap(BaseObjexObj obj, ObjexObjStateBean state, Map<String, String> current, Map<String, T> val, boolean owned) {
        Map<String, String> ret = current;
        
        // If owned
        if( owned ) {
         // FUTURE: Allow setting to non-ObjexObj and copying in
            throw new IllegalArgumentException("Cannot set an owned map currently");
        }
        
        // If not owned, each val must be an ObjexObj instances
        else {
            ret = new HashMap<String, String>();
            Iterator<String> it = val.keySet().iterator();
            while( it.hasNext() ) {
                String key = it.next();
                T ref = val.get(key);
                if( !(ref instanceof ObjexObj) ) throw new IllegalArgumentException("Attempt to set reference list using non-ObjexObj properties: " + ref);
                ret.put(key, ((ObjexObj)ref).getId().toString());
            }
        }
        
        if( (ret != null && !ret.equals(current)) || current != null ) setEditable(obj, state);
        else ret = current;
        
        return ret;
    }
    
    *//**
     * Helper to add a new element to a map
     *//*
    public static Map<String, String> addMap(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, String type, String key, Map<String, String> current) {
        Map<String, String> ret = null;
        
        if( current != null ) {
            if( owned && current.containsKey(key) ) removeObject(obj, current.get(key)); 
            ret = new HashMap<String, String>(current);
        }
        else {
            ret = new HashMap<String, String>();
        }
        
        ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
        setEditable(obj, state);
        ret.put(key, newObj.getId().toString());
        
        return ret;
    }

    *//**
     * Helper to get a map element by key
     *//*
    public static <T> T getMapByKey(BaseObjexObj obj, ObjexObjStateBean state, String key, Map<String, String> current, Class<T> expected) {
        T ret = null;
        
        String id = null;
        if( current != null && current.containsKey(key) ) id = current.get(key);
        
        if( id != null ) {
            ObjexObj refObj = obj.getContainer().getObject(id);
            if( refObj == null ) ret = null;
            else if( !ObjexObj.class.equals(expected) ) ret = refObj.getBehaviour(expected);
            else ret = expected.cast(refObj);
        }
        
        return ret;
    }
    
    *//**
     * Helper to remove from a map by key
     *//*
    public static Map<String, String> removeMapByKey(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, String key, Map<String, String> current) {
        Map<String, String> ret = current;
        
        String id = null;
        if( current != null && current.containsKey(key) ) {
            if( owned ) removeMapById(obj, state, owned, id, current);
            setEditable(obj, state);
            
            ret = new HashMap<String, String>(current);
            ret.remove(key);
        }
        
        return ret;
    }
    
    *//**
     * Helper to remove from a list an object by its ID
     *//*
    public static Map<String, String> removeMapById(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, Object id, Map<String, String> current) {
        if( current == null || current.size() == 0 ) return current;
        
        ObjexID realId = DefaultObjexID.getId(id);
        
        HashMap<String, String> ret = new HashMap<String, String>(current);
        boolean removed = false;
        Iterator<String> it = current.keySet().iterator();
        while( it.hasNext() ) {
            String ref = current.get(it.next());
            if( realId.equals(ref) ) {
                setEditable(obj, state);
                it.remove();
                removed = true;
            }
        }
        
        if( removed ) {
            removeObject(obj, realId);
        }
        
        return ret;
    }

    *//**
     * Helper to remove all objects in a map
     *//*
    public static Map<String, String> removeMapAll(BaseObjexObj obj, ObjexObjStateBean state, boolean owned, Map<String, String> current) {
        if( current == null || current.size() == 0 ) return current;
        
        if( owned ) removeObjects(current);
        
        setEditable(obj, state);
        return null;
    }
    
    *//**
     * Helper to remove all objects from a list
     *//*
    private static void removeObjects(Map<String, String> ids) {
       // TODO: Remove at the container level 
    }*/
}
