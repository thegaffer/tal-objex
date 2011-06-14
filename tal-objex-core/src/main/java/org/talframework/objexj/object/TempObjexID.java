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
package org.talframework.objexj.object;

import org.talframework.objexj.ObjexID;

/**
 * This class can be used to create an ID that is temporary. This is
 * when the container is saved any object with a temporary ID will be
 * updated with it's correct ID.
 *
 * @author Tom Spencer
 */
public final class TempObjexID implements ObjexID {
    private static final long serialVersionUID = 1L;
    
    /** The ID of the temp ID (relative to the transaction) */
    private String id;
    
    /**
     * Helper to determine if the given ID represents a temporary ID
     * and if so return it as an TempObjexID instance.
     * 
     * @param id The ID of the object
     * @return The temp ID
     */
    public static TempObjexID getTempId(String id) {
        if( id.startsWith("Temp/") ) return new TempObjexID(id.substring(5));
        else return null;
    }
    
    public TempObjexID(String id) {
        this.id = id;
    }

    /**
     * A temp ID is never null
     */
    public boolean isNull() {
        return false;
    }
    
    /**
     * A temp ID is always temp
     */
    public boolean isTemp() {
        return true;
    }
    
    public String getType() {
        return "Temp";
    }
    
    public Object getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "Temp/" + id; 
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( obj instanceof String && obj.toString().startsWith("Temp/") ) {
            return this.id.equals(obj.toString().substring(5));
        }
        if( getClass() != obj.getClass() ) {
            
            return false;
        }
        TempObjexID other = (TempObjexID)obj;
        if( id == null ) {
            if( other.id != null ) return false;
        }
        else if( !id.equals(other.id) ) return false;
        return true;
    }
}

