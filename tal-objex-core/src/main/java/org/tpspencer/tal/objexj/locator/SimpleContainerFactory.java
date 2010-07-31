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
	
	public EditableContainer create() {
        TransactionMiddleware middleware = middlewareFactory.createContainer(strategy);
        
        // Add in the root object
        ObjectStrategy rootStrategy = strategy.getObjectStrategy(strategy.getRootObjectName());
        ObjexID rootId = strategy.getRootObjectID();
        ObjexObjStateBean rootBean = rootStrategy.getNewStateInstance(null);
        middleware.getCache().addNewObject(rootId, rootBean);
        
        return new SimpleTransaction(strategy, middleware, null);
    }
    
    /**
	 * Simply constructs an instance of the standard container
	 * with plugins configured.
	 */
	public Container get(String id) {
	    ContainerMiddleware middleware = middlewareFactory.getMiddleware(strategy, id);
	    return new StandardContainer(strategy, middleware, middleware.getContainerId());
	}
	
	/**
	 * Simply constructs an instance of the basic editable 
	 * container.
	 */
	public EditableContainer open(String id) {
	    TransactionMiddleware middleware = middlewareFactory.getTransaction(strategy, id);
	    return new SimpleTransaction(strategy, middleware, middleware.getContainerId());
	}
}
