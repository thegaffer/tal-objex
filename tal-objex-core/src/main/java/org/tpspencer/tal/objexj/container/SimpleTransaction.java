package org.tpspencer.tal.objexj.container;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class SimpleTransaction extends StandardContainer implements EditableContainer {
	
	/** Open when initially created */
	private boolean open = true;
	/** Member holds the ID of this transaction */
	private final String transactionId;
	/** Member holds the transaction cache */
	private final TransactionMiddleware middleware;
	/** Member holds the cache of everything in the transaction */
	private TransactionCache cache;
	
	public SimpleTransaction(
			ContainerStrategy strategy, 
			TransactionMiddleware middleware,
			String id,
			String transactionId) {
		super(strategy, middleware, id);
		this.transactionId = transactionId;
		this.middleware = middleware;
		
		// Find existing cache or load from memory
		cache = middleware.getCache();
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public boolean isNew() {
	    return middleware.isNew();
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void closeContainer() {
		open = false;
		middleware.clear(cache);
	}
	
	public void saveContainer() {
		// TODO: Create real new IDs!?!
		middleware.save(cache);
		open = false;
	}
	
	public String suspend() {
		open = false; // No more changes in this thread!
		return middleware.suspend(cache);
	}
	
	/**
	 * Overridden to get from the cache if it exists there.
	 */
	@Override
	public ObjexObj getObject(Object id) {
	    ObjexID objexId = middleware.convertId(id);
	    
	    if( cache.isDeleted(objexId) ) return null;
	    
	    Object obj = cache.findObject(objexId);
	    if( obj != null ) return createObjexObj(objexId, obj);
	    
	    return super.getObject(id);
	}
	
	public ObjexObj newObject(String type, Object parent) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		ObjectStrategy strategy = getContainerStrategy().getObjectStrategies().get(type);
		ObjexObjStateBean state = strategy.getNewStateInstance();
		
		// a. Create new ID and add it to state object
		ObjexID newId = middleware.createNewId(type, state);
		ObjexID parentId = parent != null ? middleware.convertId(parent) : null;
		state.setParentId(parentId);
		
		// b. Add to cache
		cache.addNewObject(newId, state);
		
		return strategy.getObjexObjInstance(this, parentId, newId, state);
	}
	
	public void updateObject(ObjexID id, ObjexObj obj) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		cache.addUpdatedObject(id, obj.getStateObject());
		
		// return createObjexObj(id, obj.getStateObject());
	}
	
	public void removeObject(ObjexID id) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		cache.addRemovedObject(id, id);
	}
}
