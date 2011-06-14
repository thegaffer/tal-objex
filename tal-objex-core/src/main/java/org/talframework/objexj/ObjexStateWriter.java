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
package org.talframework.objexj;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj.ObjexFieldType;

/**
 * This interface is intended to all a client to get the objects
 * to write out their state as it currently stands. The class
 * implementing this interface may be writing the state to a
 * stream, an XML file, a custom file or some other persistence
 * mechanism. 
 * 
 * TODO: Not sure about this?!?
 *
 * @author Tom Spencer
 */
public interface ObjexStateWriter {

    /**
     * Writes out the given property as a simple property
     * 
     * @param name The name of the proeprty
     * @param obj The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void write(String name, Object obj, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReference(String name, Object ref, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference list property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReferenceList(String name, List<?> refs, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference list property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReferenceSet(String name, Set<?> refs, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference map property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReferenceMap(String name, Map<String, ?> refs, ObjexFieldType type, boolean persistent);
}
