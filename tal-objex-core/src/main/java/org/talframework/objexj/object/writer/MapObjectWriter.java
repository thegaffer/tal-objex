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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;

/**
 * Writes away a single object into a map. The object is flattened
 * such that reference properties are stored as stringified IDs, 
 * not as embedded complex objects.
 * 
 * <p>Note that this in this version of the class we only deal with
 * scalar values and complex properties are written to the map as
 * is. In the future we might want to break this down further??</p>
 *
 * @author Tom Spencer
 */
public class MapObjectWriter extends BaseObjectWriter {
    
    /** An optional prefix to add to each entry in the map */
    private String prefix;
    /** The map we are reading from */
    private Map<String, Object> properties;
    
    public MapObjectWriter(ObjectWriterBehaviour... behaviours) {
        super(behaviours);
    }
    
    /**
     * Call to write the object into map. This method will also write in the
     * objects type, ID and parentId into the map
     * 
     * @param properties The map to write into
     * @param prefix The prefix to use
     * @param target
     */
    public void writeObject(Map<String, Object> properties, String prefix, ObjexObj target) {
        this.prefix = prefix;
        this.properties = properties;
        this.properties.put(formKey("objexType"), target.getType());
        this.properties.put(formKey("id"), target.getId().toString());
        if( target.getParentId() != null ) this.properties.put(formKey("parentId"), target.getParentId().toString());
        super.write(target);
        this.properties = null;
    }
    
    private String formKey(String base) {
        if( prefix != null ) return prefix + base;
        return base;
    }

    /**
     * In this version of the writer there are no additional reasons
     * for not writing beyond those checked in the base classs.
     */
    @Override
    protected boolean shouldWrite(String name, Object val) {
        return true;
    }

    /**
     * Simply adds the value to the map as-is
     */
    @Override
    protected void writeProperty(String name, Object val, ObjexFieldType type, boolean persistent) {
        properties.put(formKey(name), val);
    }

    /**
     * Simply adds the value to the map as-is
     */
    @Override
    protected void writeListProperty(String name, List<String> refs, ObjexFieldType type, boolean persistent) {
        properties.put(formKey(name), refs);
    }

    /**
     * Simply adds the value to the map as-is
     */
    @Override
    protected void writeSetProperty(String name, Set<String> refs, ObjexFieldType type, boolean persistent) {
        properties.put(formKey(name), refs);
    }

    /**
     * Simply adds the value to the map as-is
     */
    @Override
    protected void writeMapProperty(String name, Map<String, String> refs, ObjexFieldType type, boolean persistent) {
        properties.put(formKey(name), refs);
    }

}
