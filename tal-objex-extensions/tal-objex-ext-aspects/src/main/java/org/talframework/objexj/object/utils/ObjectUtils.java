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
