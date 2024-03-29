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
 * reference fields. It can be used without using 
 * the generators if required.
 *
 * @author Tom Spencer
 */
public final class ReferenceFieldUtils extends FieldUtils {
    
    /**
     * Helper method to get a reference as the expected type
     *//*
    public static <T> T getReference(BaseObjexObj obj, Class<T> expected, String val) {
        T ret = null;
        
        if( val != null ) {
            ObjexObj ref = obj.getContainer().getObject(val);
            ret = ref != null ? ref.getBehaviour(expected) : null;
        }
        
        return ret;
    }

    *//**
     * Helper method that sets a reference field to the incoming
     * ObjexObj field. 
     *//*
    public static <T> String setReference(BaseObjexObj obj, ObjexObjStateBean state, String current, T val, boolean owned, String type) {
        String ret = null;
        
        // If owned ref and not passing ObjexObj create new instance
        if( owned && !(val instanceof ObjexObj) ) {
            if( current == null ) {
                ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
                copyObjects(newObj, val);
                ret = newObj.getId().toString();
            }
            else {
                if( type == null || type.length() == 0 ) throw new IllegalArgumentException("Cannot set an owned reference with no default type to a non-ObjexObj object");
                ObjexObj currentObj = obj.getContainer().getObject(current);
                copyObjects(currentObj, val);
                ret = current;
            }
        }
        
        // Setting to an ObjexObj ref, must be same one
        else if( owned ) {
            if( ((ObjexObj)val).getId().toString().equals(current) ) ret = current;
            throw new IllegalArgumentException("Attempt to change the ObjexObj instance on a owned reference field: " + val);
        }
        
        // If val is an ObjexObj, set ret
        else {
            if( val instanceof ObjexObj ) ret = ((ObjexObj)val).getId().toString();
            else throw new IllegalArgumentException("Cannot accept object in reference field that is not an ObjexObj: " + val); 
        }
        
        // Test if it has changed, if so make editable
        if( current == null || !current.equals(ret) ) {
            if( owned && current != null ) removeObject(obj, current);
            setEditable(obj, state);
        }
        
        return ret;
    }
    
    public static <T> T createReference(BaseObjexObj obj, ObjexObjStateBean state, Class<T> expected, String type) {
        ObjexObj newObj = obj.getInternalContainer().newObject(obj, state, type);
        return newObj.getBehaviour(expected);
    }*/
}
