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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.talframework.objexj.exceptions.ObjectIDInvalidException;

/**
 * Default implementation of ObjexID. This will be fine for most
 * containers and is the default for StandardContainer.
 * 
 * @author Tom Spencer
 */
public class DefaultObjexID implements ObjexID {
    private static final long serialVersionUID = 1L;
    
    /** The type of object */
    private final String type;
    /** The ID of the object */
    private final Object id;
    
    /**
     * Constructs an ObjexID given a string. The
     * constructor will split the string into its
     * constituent parts.
     * 
     * @param id The ID to split
     */
    public DefaultObjexID(String id) {
        int index = id.indexOf('/');
        if( index < 0 || index >= id.length() - 1 ) throw new IllegalArgumentException("Invalid string passed as ObjexID: " + id);
        
        this.type = id.substring(0, index);
        String idPart = id.substring(index + 1);
        
        // If its a numeric ID, convert it now
        long numericId = 0;
        try {
            numericId = Long.parseLong(idPart);
        }
        catch( NumberFormatException e ) {
        }
        
        if( numericId > 0 ) this.id = numericId;
        else this.id = idPart;
    }
    
    /**
     * Constructs a new ObjexID given the type of
     * object and its ID unique in the container.
     * 
     * @param type The type of object
     * @param id Its ID
     */
    public DefaultObjexID(String type, Object id) {
        this.type = type;
        this.id = id;
    }
    
    /**
     * This version of the constructor accepts the type
     * and ID as separate parameters, but will test the
     * id to see if it is numeric.
     * 
     * @param type The objects type
     * @param id The ID of the object
     * @param detectNumeric If true, detects if ID is numeric
     */
    public DefaultObjexID(String type, String id, boolean detectNumeric) {
        this.type = type;
        
        long numericId = 0;
        try {
            numericId = Long.parseLong(id);
        }
        catch( NumberFormatException e ) {
        }
        
        if( numericId > 0 ) this.id = numericId;
        else this.id = id;
    }
    
    /**
     * Constructs a new ObjexID given the type of
     * object and its ID unique in the container.
     * 
     * @param type The type of object
     * @param id Its ID
     */
    public DefaultObjexID(String type, long id) {
        this.type = type;
        this.id = id;
    }
    
    /**
     * Helper to extract the ObjexID from the 
     * 
     * @param id The id of the object as supplied
     * @return The ID
     */
    public static ObjexID getId(Object id) {
        if( id == null ) return new DefaultObjexID(null, 0);
        if( id instanceof ObjexID ) return (ObjexID)id;
        else if( id instanceof String ) return new DefaultObjexID((String)id);
        else throw new ObjectIDInvalidException(id);
    }
    
    /**
     * @return The type of object the ID represents
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return The unique ID within the container
     */
    public Object getId() {
        return id;
    }
    
    public boolean isNull() {
        return type == null;
    }
    
    /**
     * A GAEObjexID is never temp, once assigned it stays.
     */
    public boolean isTemp() {
        return false;
    }
    
    @Override
    public String toString() {
        return type + "/" + id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( !(obj instanceof ObjexID) ) return false;
        ObjexID other = (ObjexID)obj;
        if( id == null ) {
            if( other.getId() != null ) return false;
        }
        else if( !id.equals(other.getId()) ) return false;
        if( type == null ) {
            if( other.getId() != null ) return false;
        }
        else if( !type.equals(other.getType()) ) return false;
        return true;
    }
    
    /**
     * This JAXB adapter class takes care of adapting an ObjexID
     * into a String and vice versa
     *
     * @author Tom Spencer
     */
    public static class XmlObjexIDAdaptor extends XmlAdapter<String, ObjexID> {
        public ObjexID unmarshal(String val) throws Exception {
            return DefaultObjexID.getId(val);
        }
        
        public String marshal(ObjexID val) throws Exception {
            return val.toString();
        }
    }
}
