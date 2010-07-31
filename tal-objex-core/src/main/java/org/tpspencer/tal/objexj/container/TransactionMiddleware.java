package org.tpspencer.tal.objexj.container;

import org.tpspencer.tal.objexj.ObjexIDStrategy;

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
     * @return The default strategy for the middleware
     */
    public ObjexIDStrategy getIdStrategy();
    
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
	 * @return The ID of the transaction
	 */
	public String suspend();
	
	/**
	 * Call to effectively abort the operation.
	 * 
	 * @param cache The cache of objects inside transaction
	 */
	public void clear();
	
	/**
	 * Call to save or commit the transaction.
	 * 
	 * @param cache The cache of objects inside transaction
	 * @return The ID of the container
	 */
	public String save();
}
