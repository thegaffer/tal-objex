package org.tpspencer.tal.objexj.exceptions;

/**
 * Indicates that an attempt to load a known container failed
 * because it was not found by the runtime environment. This
 * generally indicates some programmatic error.
 * 
 * @author Tom Spencer
 */
public class ObjectIDInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the object that is invalid */
    private final Object id;
    
    public ObjectIDInvalidException(Object id) {
        super();
        this.id = id;
    }
    
    public ObjectIDInvalidException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Object ID [" + id + "] appears invalid for the container";
    }
}
