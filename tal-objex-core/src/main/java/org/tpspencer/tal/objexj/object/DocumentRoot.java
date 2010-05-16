package org.tpspencer.tal.objexj.object;


/**
 * This interface represents the object at the root of a
 * container.
 * 
 * @author Tom Spencer
 */
public interface DocumentRoot {

    /**
     * @return The next available internal ID for this container
     */
	public long getNextId();
}
