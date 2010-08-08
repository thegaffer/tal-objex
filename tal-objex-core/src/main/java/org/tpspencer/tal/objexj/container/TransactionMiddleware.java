package org.tpspencer.tal.objexj.container;

import java.util.Map;

import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.events.EventListener;

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
	 * @param status The current status of this container (post transaction)
	 * @param header The header information for this container
	 * @return The ID of the container
	 */
	public String save(String status, Map<String, String> header);
	
	/**
     * Causes the middleware to register an event listener
     * against the container. This listener will be persisted
     * and used from now on.
     * 
     * FUTURE: Should there be an unregister listener??
     * 
     * @param listener The listern to register
     */
    public void registerListener(EventListener listener);
    
    /**
     * Causes the middleware to register an event listener
     * against the current transaction only. The listener
     * will be invoked when the container is saved, but
     * will not be registered so it is invoked in the
     * future.
     * 
     * @param listener The listern to register
     */
    public void registerListenerForTransaction(EventListener listener);
}
