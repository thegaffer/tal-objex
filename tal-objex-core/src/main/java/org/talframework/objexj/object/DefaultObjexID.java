package org.tpspencer.tal.objexj.object;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.exceptions.ObjectIDInvalidException;

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
}
