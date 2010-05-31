package org.tpspencer.tal.objexj.container;

import org.tpspencer.tal.objexj.ObjexID;

/**
 * Extends the ContainerMiddleware to provide the runtime environment
 * services for a transaction.
 * 
 * @author Tom Spencer
 */
public interface TransactionMiddleware extends ContainerMiddleware {

    /**
     * @return True if the container is a new container (not yet persisted)
     */
    public boolean isNew();
    
	/**
	 * Call to get an existing {@link TransactionCache} for the
	 * transaction.
	 * 
	 * @return The transaction cache, or null
	 */
	public TransactionCache getCache();
	
	/**
	 * Call to suspend the transaction. This is not supported by
	 * all environments. The idea is the cache of objects is 
	 * stored away for later retreival using the ID returned.
	 * 
	 * @param cache The cache of objects inside transaction
	 * @return The ID of the transaction
	 */
	public String suspend(TransactionCache cache);
	
	/**
	 * Call to effectively abort the operation.
	 * 
	 * @param cache The cache of objects inside transaction
	 */
	public void clear(TransactionCache cache);
	
	/**
	 * Call to save or commit the transaction.
	 * 
	 * @param cache The cache of objects inside transaction
	 */
	public void save(TransactionCache cache);
	
	/**
	 * This method should create a new ObjexID for the given
	 * type of object. This may be temporary until the editable 
	 * container is saved. 
	 * 
	 * @param type The type of object we are creating id for
	 * @return The new ID
	 */
	public ObjexID createNewId(String type);
	
	/**
	 * This is called during object constructor to get the raw
	 * runtime environment ID of an object. It is used to set
	 * in the state bean class.
	 * 
	 * @param id The ObjexID (typically obtained via createNewId)
	 * @return The raw ID
	 */
	public Object getRawId(ObjexID id);
}
