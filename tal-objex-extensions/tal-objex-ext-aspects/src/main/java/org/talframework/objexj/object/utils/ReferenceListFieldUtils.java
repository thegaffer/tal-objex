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
 * reference list fields. It can be used without using 
 * the generators if required.
 *
 * @author Tom Spencer
 */
public final class ReferenceListFieldUtils extends FieldUtils {
    
    /**
     * Call to get a reference property
     *//*
    public static <T> List<T> getList(BaseObjexObj obj, Class<T> expected, List<String> refs) {
        return obj.getContainer().getObjectList(refs, expected);
    }
    
    *//**
     * Helper to get a list element by index
     *//*
    public static <T> T getElementByIndex(BaseObjexObj obj, Class<T> expected, List<String> current, int index) {
        T ret = null;
        
        String id = null;
        if( current != null && index >= 0 && index < current.size() ) id = current.get(index);
        
        if( id != null ) {
            ObjexObj refObj = obj.getContainer().getObject(id);
            if( refObj == null ) ret = null;
            else if( !ObjexObj.class.equals(expected) ) ret = refObj.getBehaviour(expected);
            else ret = expected.cast(refObj);
        }
        
        return ret;
    }

    *//**
     * Helper to set a reference list against a set of incoming objects 
     *//*
    public static <T> List<String> setList(BaseObjexObj obj, ObjexObjStateBean state, List<String> current, List<T> val, boolean owned) {
        List<String> ret = current;
        
        // If owned
        if( owned ) {
            // FUTURE: Allow setting to non-ObjexObj and copying in
            throw new IllegalArgumentException("Cannot set an owned list currently");
        }
        
        // If not owned, each val must be an ObjexObj instances
        else {
            ret = new ArrayList<String>(val.size());
            Iterator<T> it = val.iterator();
            while( it.hasNext() ) {
                T ref = it.next();
                if( !(ref instanceof ObjexObj) ) throw new IllegalArgumentException("Attempt to set reference list using non-ObjexObj properties: " + ref);
                ret.add(((ObjexObj)ref).getId().toString());
            }
        }
        
        if( (ret != null && !ret.equals(current)) || current != null ) setEditable(obj, state);
        else ret = current;
        
        return ret;
    }
    
    *//**
     * Helper to create a new element - note this method does not add
     * the element to the list, the caller should call addListElement
     * to do this.
     *//*
    public static <T> T createElement(BaseObjexObj obj, ObjexObjStateBean state, Class<T> expected, String type) {
        ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
        return newObj.getBehaviour(expected);
    }
    
    *//**
     * Helper to add a given element to a list prop
     *//*
    public static <T> List<String> addElement(BaseObjexObj obj, ObjexObjStateBean state, List<String> current, T val) {
        List<String> ret = null;
        
        ObjexID id = null;
        if( val instanceof ObjexObj ) id = ((ObjexObj)val).getId();
        else if( val instanceof ObjexID ) id = ((ObjexID)val);
        else throw new IllegalArgumentException("Attempt to set a reference to a non-ObjexObj object: " + val); 
        
        if( current != null ) ret = new ArrayList<String>(current);
        else ret = new ArrayList<String>();
        
        setEditable(obj, state);
        ret.add(id.toString());
        
        return ret;
    }

    public static List<String> removeElementByIndex(BaseObjexObj obj, ObjexObjStateBean state, List<String> current, boolean owned, int index) {
        List<String> ret = current;
        
        String id = null;
        if( current != null && index >= 0 && index < current.size() ) id = current.get(index);
        
        // Remove all by the ID if owned
        if( id != null && owned ) {
            removeElementById(obj, state, current, owned, id);
        }
        
        // Remove just that 1 index
        else if( id != null && !owned ) {
            setEditable(obj, state);
            ret = new ArrayList<String>(current);
            ret.remove(index);
        }
        
        return ret;
    }
    
    *//**
     * Helper to remove from a list an object by its ID
     *//*
    public static List<String> removeElementById(BaseObjexObj obj, ObjexObjStateBean state, List<String> current, boolean owned, Object id) {
        if( current == null || current.size() == 0 ) return current;
        
        ObjexID realId = DefaultObjexID.getId(id);
        
        List<String> ret = new ArrayList<String>(current);
        boolean removed = false;
        Iterator<String> it = current.iterator();
        while( it.hasNext() ) {
            String ref = it.next();
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
     * Helper to remove all objects in a list
     *//*
    public static List<String> removeAll(BaseObjexObj obj, ObjexObjStateBean state, List<String> current, boolean owned) {
        if( current == null || current.size() == 0 ) return current;
        
        if( owned ) removeObjects(current);
        
        setEditable(obj, state);
        return null;
    }
    
    *//**
     * Helper to remove all objects from a list
     *//*
    private static void removeObjects(List<String> ids) {
       // TODO: Remove at the container level 
    }*/
}
