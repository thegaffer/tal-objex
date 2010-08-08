package org.tpspencer.tal.objexj.exceptions;

/**
 * Indicates that an attempt to obtain a named query failed
 * because it was not found by the runtime environment. This
 * generally indicates some configuration error.
 * 
 * @author Tom Spencer
 */
public class QueryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the name of the query that was not found */
    private final String name;
    
    public QueryNotFoundException(String name) {
        super();
        this.name = name;
    }
    
    public QueryNotFoundException(String name, Exception cause) {
        super(cause);
        this.name = name;
    }
    
    @Override
    public String getMessage() {
        return "Query [" + name + "] not found";
    }
}
