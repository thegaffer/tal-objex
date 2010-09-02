package org.talframework.objexj.exceptions;

/**
 * This exception is thrown a type of object is not found.
 * This can be because we try and create of a type the
 * container has no knowledge of, or it may indicate a
 * configuration error.
 * 
 * @author Tom Spencer
 */
public class ObjectTypeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the type of object that was not found */
    private final String type;
    
    public ObjectTypeNotFoundException(String type) {
        super();
        this.type = type;
    }
    
    public ObjectTypeNotFoundException(String type, Exception cause) {
        super(cause);
        this.type = type;
    }
    
    @Override
    public String getMessage() {
        return "ObjectType [" + type + "] not found";
    }
}
