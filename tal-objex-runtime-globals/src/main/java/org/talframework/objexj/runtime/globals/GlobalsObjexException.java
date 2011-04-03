package org.talframework.objexj.runtime.globals;

/**
 * Simple exception class representing a problem in the globals runtime.
 *
 * @author Tom Spencer
 */
public class GlobalsObjexException extends RuntimeException {
    private final static long serialVersionUID = 1L;

    public GlobalsObjexException(String cause) {
        super("Problem occured in the Globals Objex Runtime: " + cause);
    }
}
