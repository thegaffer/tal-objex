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
package org.talframework.objexj.object.writer;

import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;

/**
 * The root class for the reader/writer classes. In particular this class
 * provides the logic for conversions between object and storage types.
 *
 * @author Tom Spencer
 */
public class BaseReaderWriter {
    /** Member holds a map of primitives to their wrappers */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = new HashMap<Class<?>, Class<?>>();
    
    static {
        PRIMITIVE_WRAPPERS.put(double.class, Double.class);
        PRIMITIVE_WRAPPERS.put(float.class, Float.class);
        PRIMITIVE_WRAPPERS.put(long.class, Long.class);
        PRIMITIVE_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVE_WRAPPERS.put(short.class, Short.class);
        PRIMITIVE_WRAPPERS.put(char.class, Character.class);
        PRIMITIVE_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPERS.put(boolean.class, Boolean.class);
    }

    /** Map holds all of the stored convertors */
    private Map<Class<?>, TypeConvertor> convertors;
    
    /**
     * Add a convertor to this reader/writer
     * 
     * @param type The class as held in memory we want to perform conversions for
     * @param convertor The convertor to use
     */
    public void addConvertor(Class<?> type, TypeConvertor convertor) {
        if( convertors == null ) convertors = new HashMap<Class<?>, TypeConvertor>();
        convertors.put(type, convertor);
    }
    
    /**
     * Obtains the requred value given the stored value. This method will cast
     * or use a convertor as required.
     * 
     * @param requiredType The type we require to hold the value as
     * @param val The value as stored
     * @return The value in the requred type
     */
    @SuppressWarnings("unchecked")
    protected <T> T fromStorage(Class<T> requiredType, Object val) {
        T ret = null;
        
        if( val == null ) ret = null;
        else if( canCast(requiredType, val) ) ret = (T)val;
        else if( ObjexID.class.equals(requiredType) ) ret = requiredType.cast(DefaultObjexID.getId(val));
        else if( convertors != null && convertors.containsKey(requiredType) ) {
            ret = convertors.get(requiredType).fromStorage(requiredType, val);
        }
        else {
            throw new IllegalArgumentException("Unable to convert between storage [" + val + "] and required [" + requiredType + "] types");
        }
        
        return ret;
    }
        
    /**
     * Stores the value in the appropriate storage type. Typically
     * the appropriate storage type is the native type, but if a
     * convertor is registered we use that.
     * 
     * @param val The value held
     * @return The value to actually store
     */
    protected Object toStorage(Object val) {
        Object ret = null;
        
        if( val == null ) {
            ret = null;
        }
        
        else if( val instanceof ObjexID ) {
            ret = val.toString();
        }
        
        else if( convertors != null && convertors.containsKey(val.getClass()) ) {
            ret = convertors.get(val.getClass()).toStorage(val);
        }
        
        else {
            ret = val;
        }
        
        return ret;
    }
    
    /**
     * Helper method to see if we can safely dynamically cast the value
     * 
     * @param fromType The type of the value
     * @param toType The type we want
     * @param val The current value
     * @return True if val can be cast, false otherwise
     */
    private boolean canCast(Class<?> toType, Object val) {
        boolean ret = false;
        
        if( val == null ) ret = true;
        else if( val.getClass().equals(toType) ) ret = true;
        else if( toType.isAssignableFrom(val.getClass()) ) ret = true;
        else if( toType.isPrimitive() && val.getClass().equals(PRIMITIVE_WRAPPERS.get(toType)) ) ret = true;
        else if( val.getClass().isPrimitive() && toType.equals(PRIMITIVE_WRAPPERS.get(val.getClass())) ) ret = true;
        
        return ret;
    }
}
