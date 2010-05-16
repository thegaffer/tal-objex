package org.tpspencer.tal.objexj;

/**
 * This interface represents a container that can be
 * edited. Options exist to create, update or remove
 * objects from the container. The updating and 
 * removing of objects from the container occurs as
 * you interact with the editable container.  
 * 
 * @author Tom Spencer
 */
public interface EditableContainer extends Container {
    
    /**
     * Call to determine if the editable container represents a
     * new container that has not yet been persisted. If so the
     * next object will be the root object.
     * 
     * @return Determines if the container if new (and not yet persisted)
     */
    public boolean isNew();
	
	/**
	 * Call to determine if the editable container can still
	 * accept changes.
	 * 
	 * @return True if it is open, false otherwise
	 */
	public boolean isOpen();
	
	/**
	 * @return The ID of this transaction - only present if its a long-lived edit
	 */
	public String getTransactionId();
	
	/**
	 * Called to update an existing object.
	 * 
	 * @param id The ID of the object to update
	 * @param obj The object we are updating
	 */
	public void updateObject(ObjexID id, ObjexObj obj);
	
	/**
	 * Called to add an object to the container 
	 * 
	 * @param type The type of the new object
	 * @param parent The ID of the parent object (if any)
	 */
	public ObjexObj newObject(String type, Object parent);
	
	/**
	 * Called to remove an object from the container.
	 * 
	 * @param id The ID of the object to remove
	 */
	public void removeObject(ObjexID id);

	/**
	 * Call to save the container. Once this is done the
	 * no further changes can be made via this editable
	 * container.
	 */
	public void saveContainer();
	
	/**
	 * Call to close the container without saving any
	 * changes. No further changes can be made via this
	 * editable container. 
	 */
	public void closeContainer();
	
	/**
	 * Call to suspend the transaction and come back to
	 * it later. This is not supported in all runtime
	 * environments.
	 * 
	 * @return The ID of the transaction 
	 */
	public String suspend();
}
