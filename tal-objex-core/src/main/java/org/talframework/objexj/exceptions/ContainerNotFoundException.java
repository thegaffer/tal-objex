package org.talframework.objexj.exceptions;

/**
 * Indicates that an attempt to load a known container failed
 * because it was not found by the runtime environment. This
 * generally indicates some programmatic error.
 * 
 * @author Tom Spencer
 */
public class ContainerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container that was not found */
    private final String id;
    
    public ContainerNotFoundException(String id) {
        super();
        this.id = id;
    }
    
    public ContainerNotFoundException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Container [" + id + "] not found";
    }
}
