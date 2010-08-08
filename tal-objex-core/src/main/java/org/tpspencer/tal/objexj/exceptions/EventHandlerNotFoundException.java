package org.tpspencer.tal.objexj.exceptions;

/**
 * Indicates that an attempt to obtain a named handler failed
 * because it was not found by the runtime environment. This
 * generally indicates some configuration error.
 * 
 * @author Tom Spencer
 */
public class EventHandlerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /** Holds the name of the event that was not found */
    private final String name;
    
    public EventHandlerNotFoundException(String name) {
        super();
        this.name = name;
    }
    
    public EventHandlerNotFoundException(String name, Exception cause) {
        super(cause);
        this.name = name;
    }
    
    @Override
    public String getMessage() {
        return "Event Handler [" + name + "] not found";
    }
}
