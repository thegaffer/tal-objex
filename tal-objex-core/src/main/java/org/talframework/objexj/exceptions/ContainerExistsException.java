package org.talframework.objexj.exceptions;

/**
 * Indicates that an attempt to create a container that 
 * already exists has been made. If this occurs as part
 * of the initialisation and you are checking that
 * known stores exist you probably want to catch this
 * and move on.
 * 
 * @author Tom Spencer
 */
public class ContainerExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container that was not found */
    private final String id;
    
    public ContainerExistsException(String id) {
        super();
        this.id = id;
    }
    
    public ContainerExistsException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Container [" + id + "] not found";
    }
}
