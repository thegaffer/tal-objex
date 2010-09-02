package org.talframework.objexj.exceptions;

/**
 * Indicates that an attempt to load an object failed
 * because it was not found by the runtime environment. 
 * 
 * @author Tom Spencer
 */
public class ObjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container */
    private final String containerId;
    /** Holds the ID of the object that was not found */
    private final String id;
    
    public ObjectNotFoundException(String containerId, String id) {
        super();
        this.containerId = containerId;
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Object [" + id + "] not found in container [" + containerId + "]";
    }
}
