package org.tpspencer.tal.objexj.gae;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleTransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.gae.object.RootContainerObject;
import org.tpspencer.tal.objexj.gae.persistence.PersistenceManagerFactorySingleton;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GAETransactionMiddleware extends GAEContainerMiddleware implements TransactionMiddleware{

	/** Holds the transaction ID to save constantly casting the container */
	private String transactionId = null;
	
	@Override
	public void init(Container container) {
		Assert.isInstanceOf(EditableContainer.class, container, "Cannot use the GAETransactionMiddleware on a normal Container, must be editable");
		
		transactionId = ((EditableContainer)container).getTransactionId();
		super.init(container);
	}
	
	/**
	 * Creates a new ID by loading the doc object, extract
	 * last used ID, incrementing it and saving root object.
	 */
	public ObjexID createNewId(String type) {
		PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
		Transaction tx = pm.currentTransaction();
		long id = 0;
		try {
			tx.begin();
			
			RootContainerObject root = pm.getObjectById(RootContainerObject.class, getRootKey());
			id = root.getLastId();
			id++;
			root.setLastId(id);
			
			tx.commit();
			
			return new GAEObjexID(type, id);
		}
		finally {
			if( tx.isActive() ) tx.rollback();
			pm.close();
		}
	}
	
	/**
	 * Gets the existing transaction cache from memory, or
	 * creates a new one
	 */
	public TransactionCache getCache() {
		TransactionCache ret = null;
		
		if( transactionId != null ) {
			MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
			ret = (TransactionCache)cache.get(transactionId);
			
			if( ret == null ) {
				// TODO: This is probably an error to return back to say cache expired!!
				ret = new SimpleTransactionCache();
			}
		}
		
		else {
			ret = new SimpleTransactionCache();
		}
		
		return ret;
	}
	
	/**
	 * Puts the TransactionCache into the memcache service, assigning
	 * a new ID as neccessary.
	 */
	public String suspend(TransactionCache cache) {
		if( transactionId == null ) {
			// TODO: Create a new transaction ID !?!
			transactionId = getContainer().getId();
		}
		
		MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
		cacheService.put(transactionId, cache);
		
		return transactionId;
	}
	
	/**
	 * Removes transaction as appropriate from cache and
	 * committs nothing to the DB
	 */
	public void clear(TransactionCache cache) {
		if( transactionId != null ) {
			MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
			cacheService.delete(transactionId);
		}
	}
	
	/**
	 * Commits all changes inside a transaction
	 */
	public void save(TransactionCache cache) {
		PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			
			if( cache.getNewObjects() != null ) pm.makePersistentAll(cache.getNewObjects().values());
			if( cache.getUpdatedObjects() != null ) pm.makePersistentAll(cache.getUpdatedObjects().values());
			if( cache.getRemovedObjects() != null ) pm.deletePersistentAll(cache.getRemovedObjects().values());
			
			tx.commit();
		}
		finally {
			if( tx.isActive() ) tx.rollback();
			pm.close();
		}
	}
	
	public static String createContainer(ContainerStrategy strategy, Object state) {
		RootContainerObject root = new RootContainerObject();
		root.setType(state.getClass().getSimpleName());
		root.setCreated(new Date());
		
		UserService userService = UserServiceFactory.getUserService();
		if( userService.isUserLoggedIn() ) {
			// TODO: Or should this be userId??
			root.setCreator(userService.getCurrentUser().getEmail());
		}
		
		// Bind ID to root object
		GAEObjexID rootId = new GAEObjexID(state.getClass().getSimpleName(), 1);
		
		PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			
			pm.makePersistent(root);
			
			String pureRootId = KeyFactory.createKeyString(root.getId(), rootId.getType(), rootId.getId());
			System.out.println("*** " + pureRootId);
			GAETransactionMiddleware.bindId(strategy, pureRootId, state);
			
			pm.makePersistent(state);
			
			tx.commit();
		}
		finally {
			if( tx.isActive() ) tx.rollback();
			pm.close();
		}
		
		return strategy.getContainerName() + "/" + KeyFactory.keyToString(root.getId());
	}
	
	private static void bindId(ContainerStrategy strategy, String id, Object bean) {
		String type = bean.getClass().getSimpleName();
		ObjectStrategy objectStrategy = strategy.getObjectStrategies().get(type);
		BeanWrapper wrapper = new BeanWrapperImpl(bean);
		wrapper.setPropertyValue(objectStrategy.getIdProp(), id);
	}
}
