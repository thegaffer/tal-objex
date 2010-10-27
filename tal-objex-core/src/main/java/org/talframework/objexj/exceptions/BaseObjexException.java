package org.talframework.objexj.exceptions;

/**
 * This is the base exception for all Objex exception types.
 *
 * @author Tom Spencer
 */
public abstract class BaseObjexException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public BaseObjexException() {
        super();
    }
    
    /**
     * Constructor that passes cause to the base
     * 
     * @param cause The cause of this exception
     */
    public BaseObjexException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Call to have the derived exception visit the visitor as
     * appropriate.
     * 
     * @param visitor The visitor to visit
     */
    public abstract <T> T accept(ObjexExceptionVisitor visitor, Class<T> expected);
}
