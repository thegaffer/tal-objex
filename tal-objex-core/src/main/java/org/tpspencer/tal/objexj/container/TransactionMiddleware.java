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
	 * Create a new ID for the object. This may be temporary
	 * until the editable container is saved. Not that it is
	 * the containers responsibility to attach this ID to the
	 * actual object.
	 * 
	 * @param beanName The simple name of the bean we are creating the ID for
	 * @return The new ID
	 */
	public ObjexID createNewId(String beanName);
}
