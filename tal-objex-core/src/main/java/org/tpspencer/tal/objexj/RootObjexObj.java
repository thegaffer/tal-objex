package org.tpspencer.tal.objexj;

import java.util.Map;

/**
 * This interface is implemented by any object that is
 * capable of being the root object inside a container.
 * A root object is the primary object and has a special
 * role to obtain determine the current status of the
 * container as a whole and to obtain the header that
 * is used when notifying any listeners of a new
 * container. If the root object does not support this
 * interface it simply means the container will have
 * no status and no header is sent out with events when
 * the container is saved.
 * 
 * @author Tom Spencer
 */
public interface RootObjexObj {

    /**
     * @return The current status of the container
     */
    public String getStatus();
    
    /**
     * The header is sent out in any event to listeners
     * of this container. The header should contain only
     * the key details of this container - and further
     * these details can only be string values. Whilst
     * the values of the header can be calculated unless
     * this is non-trivial we recommend always saving 
     * the header elements in the root object.
     * 
     * @return The set of header items for the container
     */
    public Map<String, String> getHeader();
    
    /**
     * This method is called on the root object whenever
     * we are about to save a transaction whether or not
     * the root object is part of transaction or not.
     * 
     * TODO: Should return errors (what format??) not a boolean
     * 
     * @return Indicates if there are any errors in the container
     */
    public boolean validate();
}
