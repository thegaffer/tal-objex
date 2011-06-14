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

import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;

/**
 * This class contains various helper methods that can be
 * used to work with Objex objects.
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
        else return null;
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
    
    
}
