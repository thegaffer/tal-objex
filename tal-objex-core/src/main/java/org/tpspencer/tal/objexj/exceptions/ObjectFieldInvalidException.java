package org.tpspencer.tal.objexj.exceptions;

/**
 * This exception represents an attempt to access an invalid
 * field - either because the field does not exist, is not
 * readable or is not writeable. It generally reflects a
 * programming or other error.
 *
 * @author Tom Spencer
 */
public class ObjectFieldInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the field name */
    private final String field;
    /** The reason it is invalid */
    private final String reason;
    
    public ObjectFieldInvalidException(String field, String reason) {
        super();
        this.field = field;
        this.reason = reason;
    }
    
    public ObjectFieldInvalidException(String field, String reason, Exception cause) {
        super(cause);
        this.field = field;
        this.reason = reason;
    }
    
    @Override
    public String getMessage() {
        return "ObjectFieldInvalid for field [" + field + "]: " + reason;
    }
}
