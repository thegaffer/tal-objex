package org.tpspencer.tal.objexj.exceptions;

import org.tpspencer.tal.objexj.ValidationRequest;

/**
 * Indicates that an attempt to save a container failed 
 * because it has errors that must be resolved.
 * 
 * @author Tom Spencer
 */
public class ContainerInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the ID of the container that is invalid */
    private final String id;
    /** Holds the ValidationRequest with all errors on it */
    private final ValidationRequest request;
    
    public ContainerInvalidException(String id, ValidationRequest request) {
        super();
        this.id = id;
        this.request = request;
    }
    
    @Override
    public String getMessage() {
        return "Container [" + id + "] invalid: " + request;
    }
}
