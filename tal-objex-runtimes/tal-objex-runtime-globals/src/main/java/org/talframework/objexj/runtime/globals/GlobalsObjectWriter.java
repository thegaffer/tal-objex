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
package org.talframework.objexj.runtime.globals;


/**
 * This class writes the state of an Objex object into the globals
 * storage. It holds a node which is expected to be set as the root
 * of the object.
 * 
 * TODO: As with the reader, this should be changes so that simple properties and single references are stored in a list in the node
 *
 * @author Tom Spencer
 */
public class GlobalsObjectWriter { // implements ObjexStateWriter {
    
    /** The connection to the DB so we can create lists *//*
    private final Connection connection;
    *//** The top level node we are referencing *//*
    private final NodeReference node;
    
    public GlobalsObjectWriter(Connection connection, NodeReference node) {
        this.connection = connection;
        this.node = node;
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public void write(String name, Object obj, ObjexFieldType type, boolean persistent) {
        if( !persistent ) return;
        
        if( obj == null ) {
            if( node.exists(name) ) node.kill(name);
            return;
        }
        
        switch(type) {
        case STRING:
        case MEMO:
        case BLOB_REFERENCE:
        case USER:
            node.set(obj.toString(), name);
            break;
            
        case NUMBER:
            if( obj instanceof Long ) node.set(((Number)obj).longValue(), name);
            else if( obj instanceof Integer ) node.set(((Number)obj).intValue(), name);
            else if( obj instanceof Double ) node.set(((Number)obj).doubleValue(), name);
            else if( obj instanceof Float ) node.set(((Number)obj).doubleValue(), name);
            else node.set(((Number)obj).intValue(), name);
            break;
         
        case BOOL:
            node.set(((Boolean)obj).booleanValue() ? 1 : 0, name);
            break;
            
        case SHORT_BLOB:
        case BLOB:
            throw new GlobalsObjexException("BLOBS not currently supported due to my lazyness");
            
        case DATE:
            node.set(((Date)obj).getTime(), name);
            break;
            
        case OBJECT:
            throw new GlobalsObjexException("Embedded objects not currently supported");
            
        default:
            throw new GlobalsObjexException("Unexpected type");
        }
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public void writeReference(String name, String ref, ObjexFieldType type, boolean persistent) {
        if( !persistent ) return;
        
        if( ref == null ) {
            if( node.exists(name) ) node.kill(name);
            return;
        }
        
        node.set(ref, name);
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public void writeReferenceList(String name, List<String> ref, ObjexFieldType type, boolean persistent) {
        if( !persistent ) return;
        
        if( ref == null || ref.size() == 0 ) {
            if( node.exists(name) ) node.kill(name);
            return;
        }
        
        ValueList list = connection.createList();
        for( int i = 0 ; i < ref.size() ; i++ ) list.append(ref.get(i));
        node.set(list, name);
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public void writeReferenceMap(String name, Map<String, String> ref, ObjexFieldType type, boolean persistent) {
        if( !persistent ) return;
        
        if( ref == null || ref.size() == 0 ) {
            if( node.exists(name) ) node.kill(name);
            return;
        }
        
        // TODO: Clear existing or more smart approach??
        for( String key : ref.keySet() ) {
            node.set(ref.get(key), name, key);
        }
    }*/
}
