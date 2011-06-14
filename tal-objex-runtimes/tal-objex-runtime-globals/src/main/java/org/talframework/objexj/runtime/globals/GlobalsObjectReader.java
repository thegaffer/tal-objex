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
 * This class reads in all the values of an state of an object.
 * 
 * TODO: As with the writer, this should be changes so that simple properties and single references are stored in a list in the node
 *
 * @author Tom Spencer
 */
public class GlobalsObjectReader { // implements ObjexStateReader {

    /*private final NodeReference node;
    
    public GlobalsObjectReader(NodeReference node) {
        this.node = node;
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public <T> T read(String name, T current, Class<T> expected, ObjexObjStateBean.ObjexFieldType type, boolean persistent) {
        T ret = null;
        
        switch(type) {
        case BOOL:
            ret = expected.cast(node.getInt(name) == 1);
            break;
            
        case SHORT_BLOB:
        case BLOB:
            ret = expected.cast(node.getBytes(name));
            break;
            
        case DATE:
            ret = expected.cast(new Date(node.getLong(name)));
            break;
            
        case OBJECT:
            throw new GlobalsObjexException("Embedded objects not currently supported");
            
        default:
            ret = expected.cast(node.getObject(name));
        }
        
        return ret;
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public String readReference(String name, String current, ObjexFieldType type, boolean persistent) {
        return node.getString(name);
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public List<String> readReferenceList(String name, List<String> current, ObjexFieldType type, boolean persistent) {
        ValueList list = node.getList(name);
        
        List<String> ret = new ArrayList<String>();
        if( list != null ) {
            String val = list.getNextString();
            while( val != null ) {
                ret.add(val);
                val = list.getNextString();
            }
        }
        
        return ret;
    }
    
    *//**
     * {@inheritDoc}
     *//*
    public Map<String, String> readReferenceMap(String name, Map<String, String> current, ObjexFieldType type, boolean persistent) {
        Map<String, String> ret = new HashMap<String, String>();
        
        if( node.hasSubnodes(name) ) {
            String element = node.nextSubscript(name);
            while( element != null ) {
                ret.put(element, node.getString(name, element));
                element = node.nextSubscript(name);
            }
        }

        return ret;
    }*/
}
