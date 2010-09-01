package org.tpspencer.tal.objexj;

/**
 * This interface represents a validation error against
 * an object.
 *
 * @author Tom Spencer
 */
public interface ValidationError {

    /**
     * @return The ID of the object in error
     */
    public ObjexID getObjectId();
    
    /**
     * @return The field (relative to object) that caused the error
     */
    public String getError();
    
    /**
     * @return The name of the field (if any) that has the error
     */
    public String getField();
    
    /**
     * @return The parameters to the error code (if any)
     */
    public Object[] getParams();
    
    public static enum ErrorSeverity {
        ERROR,
        WARNING,
        INFO
    }
}
