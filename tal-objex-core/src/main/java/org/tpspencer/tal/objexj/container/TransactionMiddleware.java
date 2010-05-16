package org.tpspencer.tal.objexj.container;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

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
	 * Create a new ID for the object. This may be temporary
	 * until the editable container is saved. The ID should
	 * be attached to the bean directly.
	 * 
	 * @param type The type of object we are creating id for
	 * @param bean The bean holding state of new object
	 * @return The new ID
	 */
	public ObjexID createNewId(String type, ObjexObjStateBean bean);
}
