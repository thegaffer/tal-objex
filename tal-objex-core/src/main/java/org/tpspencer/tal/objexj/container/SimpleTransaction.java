package org.tpspencer.tal.objexj.container;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

public class SimpleTransaction extends StandardContainer implements EditableContainer {
	
	/** Open when initially created */
	private boolean open = true;
	/** Member holds the transaction middleware (done so we don't cast it from base all the time) */
	private final TransactionMiddleware middleware;
	/** Member holds the cache of everything in the transaction */
	private TransactionCache cache;
	
	public SimpleTransaction(
			ContainerStrategy strategy, 
			TransactionMiddleware middleware,
			String id) {
		super(strategy, middleware, id);
		this.middleware = middleware;
		
		// Find existing cache or load from memory
		cache = middleware.getCache();
	}
	
	public boolean isNew() {
	    return middleware.isNew();
	}
	
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Overridden to get from the cache if it exists there.
	 */
	@Override
	public ObjexObj getObject(Object id) {
	    if( id == null ) return null; // Just protect from a stupid call!!
	    
	    ObjexID objexId = DefaultObjexID.getId(id);
	    ObjectStrategy objectStrategy = getContainerStrategy().getObjectStrategy(objexId.getType());
	    
	    if( cache.isDeleted(objexId) ) return null;
	    
	    Object obj = cache.findObject(objexId);
	    if( obj == null ) {
    	    obj = middleware.loadObject(objectStrategy.getStateClass(), objexId);
    	    // TODO: Casting of obj is not nice, can't everything return ObjexObjStateBean?
    	    cache.addUpdatedObject(objexId, (ObjexObjStateBean)obj);
    	}
	    
	    if( obj != null ) {
	        if( obj instanceof ObjexObjStateBean ) return createObjexObj(objectStrategy, objexId, (ObjexObjStateBean)obj);
	        else throw new IllegalArgumentException("The simple transaction only supports ObjexObjStateBean instances: " + obj);
	    }
	    else return null;
	}
	
	public ObjexObj newObject(String type, ObjexObj parent) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		ObjectStrategy strategy = getContainerStrategy().getObjectStrategy(type);
		Class<? extends ObjexObjStateBean> stateClass = strategy.getStateClass();
		
		// a. Create the temp ID
		ObjexIDStrategy idStrategy = strategy.getIdStrategy();
		if( idStrategy == null ) idStrategy = middleware.getIdStrategy();
		ObjexID newId = idStrategy.createId(this, stateClass, type, null);
		ObjexID parentId = parent != null ? parent.getId() : null;
		
		// b. Create the state object
		ObjexObjStateBean state = strategy.getNewStateInstance(parent.getId());
		
		// c. Add to cache
		cache.addNewObject(newId, state);
		
		return strategy.getObjexObjInstance(this, parentId, newId, state);
	}
	
	public void updateObject(ObjexID id, ObjexObj obj) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		cache.addUpdatedObject(id, obj.getStateObject());
	}
	
	public void removeObject(ObjexID id) {
		if( !open ) throw new IllegalArgumentException("Cannot amend closed transaction");
		
		// FUTURE: Possibly get object for audit!?!
		cache.addRemovedObject(id, null);
	}
	
	public void closeContainer() {
        open = false;
        middleware.clear();
    }
    
	// TODO: Setting the container ID, which must be Type/ID (slightly different to GAE ID)
	// TODO: Refactor this into a save class as there are multiple steps (we still need to add validation)
    public String saveContainer() {
        // SUGGEST: Move this to the middleware so it is inside the transaction
        Map<ObjexID, ObjexObjStateBean> newObjects = cache.getNewObjects();
        if( newObjects != null ) {
            Map<ObjexID, ObjexID> tempRefs = null;
            Iterator<ObjexID> it = newObjects.keySet().iterator();
            while( it.hasNext() ) {
                ObjexID temp = it.next();
                if( temp.isTemp() ) {
                    if( tempRefs == null ) tempRefs = new HashMap<ObjexID, ObjexID>();
                    ObjexID realId = null; // TODO: Get the real ID
                    tempRefs.put(temp, realId);
                }
            }
            
            // Now clean up any references
            if( tempRefs != null ) {
                it = tempRefs.keySet().iterator();
                while( it.hasNext() ) {
                    ObjexID temp = it.next();
                    ObjexObjStateBean bean = newObjects.get(temp);
                    newObjects.remove(temp);
                    newObjects.put(tempRefs.get(temp), bean);
                }
                
                it = newObjects.keySet().iterator();
                while( it.hasNext() ) {
                    newObjects.get(it.next()).updateTemporaryReferences(tempRefs);
                }
                
                Map<ObjexID, ObjexObjStateBean> updatedObjects = cache.getUpdatedObjects();
                if( updatedObjects != null ) {
                    it = updatedObjects.keySet().iterator();
                    while( it.hasNext() ) {
                        updatedObjects.get(it.next()).updateTemporaryReferences(tempRefs);
                    }
                }
            }
        }
        
        this.setId(middleware.save());
        open = false;
        return getId();
    }
    
    public String suspend() {
        open = false; // No more changes in this thread!
        return middleware.suspend();
    }
}
