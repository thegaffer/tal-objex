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

package org.talframework.objexj.object.writer;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

/**
 * This class writes out an object into Property File notation.
 * Each property is listed as a name=value pair. This class
 * handles both objects and state beans and can handle just
 * the persisted fields or all fields.
 * 
 * <p>When writing out objects ({@link ObjexObj} instances) this
 * class has the option to write out the objects recursively,
 * which means owned children will automatically be written.</p>
 *
 * @author Tom Spencer
 */
public final class PropertyWriter implements ObjexStateWriter {
    
    /** Holds the writer we are writing into */
    private final PrintWriter writer;
    /** Determines if we should also write out non-persistent fields */
    private boolean includeNonPersisted = false;
    /** Determines if owned objects should be considered, default it true */
    private boolean includeOwned = true;
    /** Determines if referenced objects should be considered, default is false */
    private boolean includeReferenced = true;
    
    /** A temp string builder to use when writing out */
    private final StringBuilder tempBuilder;
    /** The prefix for the object we are writing out */
    private String currentPrefix;
    
    public PropertyWriter(PrintWriter writer) {
        this.writer = writer;
        tempBuilder = new StringBuilder();
    }
    
    /**
     * The default is to not included non-persistent fields, call
     * this to include those fields.
     */
    public void setIncludeNonPersistent() {
        includeNonPersisted = true;
    }
    
    /**
     * The default is to include all referenced objects, call
     * this to ignore these.
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
     * @return The temporary string builder with length set to 0
     */
    protected StringBuilder getTempBuffer() {
        tempBuilder.setLength(0);
        return tempBuilder;
    }
    
    public void writeObject(String prefix, ObjexObj obj) {
        String oldPrefix = currentPrefix;
        currentPrefix = prefix;
    
        // Write out the id, parent and objType
        write("id", obj.getId().toString());
        if( obj.getParentId() != null ) write("parentId", obj.getParentId().toString());
        write("objType", obj.getType());
        
        obj.acceptWriter(this, includeNonPersisted);
        writer.flush();
        
        currentPrefix = oldPrefix;
    }
    
    public void writeState(String prefix, String id, ObjexObjStateBean bean) {
        String oldPrefix = currentPrefix;
        currentPrefix = prefix;
        
        // Write out the id, parent and objType
        write("id", id);
        if( bean.getParentId() != null ) write("parentId", bean.getParentId().toString());
        write("objType", bean.getObjexObjType());
        
        bean.acceptWriter(this, includeNonPersisted);
        writer.flush();
        
        currentPrefix = oldPrefix;
    }

    /**
     * Writes out the value
     */
    public void write(String name, Object obj, ObjexFieldType type, boolean persisted) {
        if( !persisted && !includeNonPersisted ) return;
        if( obj == null ) return;
        
        // TODO: Get value for different types based on field
        write(name, obj.toString());
    }
    
    /**
     * Writes out the reference
     */
    public void writeReference(String name, String ref, ObjexFieldType type, boolean persisted) {
        if( !persisted && !includeNonPersisted ) return;
        if( !includeReferenced && type != ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeOwned && type != ObjexFieldType.REFERENCE ) return;
        if( ref == null ) return;
        
        write(name, ref);
    }
    
    /**
     * Writes out multiple entries as in name[index]=ref
     */
    public void writeReferenceList(String name, List<String> refs, ObjexFieldType type, boolean persisted) {
        if( !persisted && !includeNonPersisted ) return;
        if( !includeReferenced && type != ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeOwned && type != ObjexFieldType.REFERENCE ) return;
        if( refs == null ) return;
        
        int index = 0;
        for( String ref : refs ) {
            String propertyName = getTempBuffer().append(name).append("[").append(index++).append("]").toString();
            write(propertyName, ref);
        }
    }
    
    /**
     * Writes out each entry as in name[key]=ref
     */
    public void writeReferenceMap(String name, Map<String, String> refs, ObjexFieldType type, boolean persisted) {
        if( !persisted && !includeNonPersisted ) return;
        if( !includeReferenced && type != ObjexFieldType.OWNED_REFERENCE ) return;
        if( !includeOwned && type != ObjexFieldType.REFERENCE ) return;
        if( refs == null ) return;
        
        for( String key : refs.keySet() ) {
            String ref = refs.get(key);
            String propertyName = getTempBuffer().append(name).append("[").append(key).append("]").toString();
            write(propertyName, ref);
        }
    }
    
    /**
     * Helper to write out a value. Note the temp string
     * builder is used during this operation.
     * 
     * @param name The name
     * @param value The value
     */
    private void write(String name, String value) {
        StringBuilder builder = getTempBuffer();
        builder.append(currentPrefix);
        builder.append('.');
        builder.append(name);
        builder.append('=');
        builder.append(value);
        builder.append('\n');
        
        writer.write(tempBuilder.toString());
    }
}
