package org.tpspencer.tal.objexj.container;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
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
	
	public ObjexObj newObject(Object state) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		// a. Create new ID and add it to state object
		String type = state.getClass().getSimpleName();
		ObjexID newId = middleware.createNewId(type);
		ObjectStrategy objectStrategy = getContainerStrategy().getObjectStrategies().get(type);
		BeanWrapper wrapper = new BeanWrapperImpl(state);
		wrapper.setPropertyValue(objectStrategy.getIdProp(), newId.toString());
		
		// b. Add to cache
		cache.addNewObject(newId, state);
		
		return createObjexObj(newId, state);
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
