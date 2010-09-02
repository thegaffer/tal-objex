package org.tpspencer.tal.objexj.exceptions;

/**
 * Indicates that the object being changed/validated is
 * invalid. This exception is only thrown if a validation
 * request has not been set on the current thread. If you
 * receive these exceptions it is because you are not
 * setting the current validation request object for the
 * objects to find within their set methods.
 * 
 * @author Tom Spencer
 */
public class ObjectErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the error code */
    private final String error;
    /** The parameters of the error */
    private final Object[] params;
    
    public ObjectErrorException(String error, Object... params) {
        super();
        this.error = error;
        this.params = params;
    }
    
    @Override
    public String getMessage() {
        return "ObjectError [" + error + "]: " + params;
    }
}
