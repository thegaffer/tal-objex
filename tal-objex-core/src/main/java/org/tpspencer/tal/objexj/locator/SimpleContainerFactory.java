package org.tpspencer.tal.objexj.locator;

import org.springframework.util.Assert;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.SimpleTransaction;
import org.tpspencer.tal.objexj.container.StandardContainer;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

/**
 * This class can be configured to create instances of a
 * type of container. This configuration includes both the
 * strategy of the container and its runtime environment
 * hook-ins.
 * 
 * @author Tom Spencer
 */
public final class SimpleContainerFactory implements ContainerFactory {
	/** Describes the overall strategy for the container */
	private final ContainerStrategy strategy;
	/** Holds the class for the object cache */
	private final ContainerMiddlewareFactory middlewareFactory;
	
	public SimpleContainerFactory(
			ContainerStrategy strategy, 
			ContainerMiddlewareFactory middlewareFactory) {
		Assert.notNull(strategy, "You must provide a valid ContainerStrategy for the factory");
		Assert.notNull(middlewareFactory, "You must provide a valid ContainerMiddlewareFactry for the factory");
		
		this.strategy = strategy;
		this.middlewareFactory = middlewareFactory;
	}
	
	/**
	 * Simply constructs an instance of the standard container
	 * with plugins configured.
	 */
	public Container get(String id) {
		ContainerMiddleware middleware = middlewareFactory.getMiddleware(strategy, id);
		return new StandardContainer(strategy, middleware, id);
	}
	
	public EditableContainer create(String id) {
	    TransactionMiddleware middleware = middlewareFactory.createContainer(strategy, id);
	    
	    // Add in the root object
	    ObjectStrategy rootStrategy = strategy.getObjectStrategy(strategy.getRootObjectName());
        ObjexID rootId = middleware.createNewId(rootStrategy.getTypeName());
	    Object rawId = middleware.getRawId(rootId);
	    ObjexObjStateBean rootBean = rootStrategy.getNewStateInstance(rawId, null);
	    middleware.getCache().addNewObject(rootId, rootBean);
	    
	    return new SimpleTransaction(strategy, middleware, id, null);
	}
	
	public EditableContainer open(String id) {
	    TransactionMiddleware middleware = middlewareFactory.createTransaction(strategy, id);
        return new SimpleTransaction(strategy, middleware, id, null);
	}
	
	public EditableContainer getTransaction(String id, String transactionId) {
	    TransactionMiddleware middleware = middlewareFactory.getTransaction(strategy, id, transactionId);
        return new SimpleTransaction(strategy, middleware, id, transactionId);
	}
}
