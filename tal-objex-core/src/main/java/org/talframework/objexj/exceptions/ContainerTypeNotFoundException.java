package org.talframework.objexj.exceptions;

/**
 * Indicates that the container type is not known to the
 * container locator.
 * 
 * @author Tom Spencer
 */
public class ContainerTypeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the type of the container that was not found */
    private final String type;
    
    public ContainerTypeNotFoundException(String id) {
        super();
        this.type = id;
    }
    
    public ContainerTypeNotFoundException(String id, Exception cause) {
        super(cause);
        this.type = id;
    }
    
    @Override
    public String getMessage() {
        return "Container type [" + type + "] not found";
    }
}
