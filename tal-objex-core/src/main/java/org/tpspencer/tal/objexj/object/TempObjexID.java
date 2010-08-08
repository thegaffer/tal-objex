package org.tpspencer.tal.objexj.object;

import org.tpspencer.tal.objexj.ObjexID;

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
