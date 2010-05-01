package org.tpspencer.tal.objexj.gae;

import javax.jdo.PersistenceManager;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.gae.persistence.PersistenceManagerFactorySingleton;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Implements the Container by interacting with the 
 * Google App Engine data store.
 * 
 * @author Tom Spencer
 */
public class GAEContainerMiddleware implements ContainerMiddleware {
	
	/** The container */
	private Container container;
	/** The key for the root object representing container */
	private Key rootKey;
	
	/**
	 * @return The container
	 */
	public Container getContainer() {
		return container;
	}
	
	/**
	 * @return The root key for this container
	 */
	public Key getRootKey() {
		return rootKey;
	}
	
	/**
	 * Initialises the middleware to point at the container
	 */
	public void init(Container container) {
		this.container = container;
		
		// Seperate container type and raw gae id!!
		String id = container.getId();
		int index = id.indexOf('/');
		if( index < 0 ) index = id.indexOf('\\');
		if( index > 0 ) id = id.substring(index + 1);
		
		rootKey = KeyFactory.stringToKey(id);
	}
	
	/**
	 * Casts the GAEObjexID or converts as necc
	 */
	public ObjexID convertId(Object id) {
		return GAEObjexID.getId(id);
	}
	
	/**
	 * Simply casts to a GAEObjexID and returns the type
	 */
	public String getObjectType(ObjexID id) {
		return GAEObjexID.getId(id).getType();
	}
	
	/**
	 * Helper to physically load an object from the
	 * datastore given its ID
	 * 
	 * @param type The type of class expected
	 * @param id The ID to perform
	 * @return The persisted state of the object
	 */
	public Object loadObject(Class<?> type, ObjexID id) {
		if( container == null ) throw new IllegalStateException("Cannot load an object from an uninitialised middleware");
		
		Object ret = null;
		GAEObjexID gaeId = GAEObjexID.getId(id);
		
		PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
		try {
			String k = KeyFactory.createKeyString(rootKey, gaeId.getType(), gaeId.getId());
			ret = pm.getObjectById(type, k);
			if( ret != null ) pm.detachCopy(ret); // Detach so any changes are not persisted
		}
		finally {
			pm.close();
		}
		
		return ret;
	}
}
