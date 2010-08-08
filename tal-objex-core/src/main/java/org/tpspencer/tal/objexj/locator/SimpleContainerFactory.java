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
import org.tpspencer.tal.objexj.exceptions.ContainerNotFoundException;
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
	private ContainerStrategy strategy;
	/** Holds the class for the object cache */
	private ContainerMiddlewareFactory middlewareFactory;
	
	/**
	 * Default constructor for spring based construction
	 */
	public SimpleContainerFactory() {
	}
	
	/**
	 * Helepr constructor for one line construction
	 * 
	 * @param strategy The container strategy
	 * @param middlewareFactory The middleware factory
	 */
	public SimpleContainerFactory(
			ContainerStrategy strategy, 
			ContainerMiddlewareFactory middlewareFactory) {
		Assert.notNull(strategy, "You must provide a valid ContainerStrategy for the factory");
		Assert.notNull(middlewareFactory, "You must provide a valid ContainerMiddlewareFactry for the factory");
		
		this.strategy = strategy;
		this.middlewareFactory = middlewareFactory;
	}
	
	/**
     * @return the strategy
     */
    public ContainerStrategy getStrategy() {
        return strategy;
    }

    /**
     * Setter for the strategy field
     *
     * @param strategy the strategy to set
     */
    public void setStrategy(ContainerStrategy strategy) {
        Assert.notNull(strategy, "You must provide a valid ContainerStrategy for the factory");
        this.strategy = strategy;
    }

    /**
     * @return the middlewareFactory
     */
    public ContainerMiddlewareFactory getMiddlewareFactory() {
        return middlewareFactory;
    }

    /**
     * Setter for the middlewareFactory field
     *
     * @param middlewareFactory the middlewareFactory to set
     */
    public void setMiddlewareFactory(ContainerMiddlewareFactory middlewareFactory) {
        Assert.notNull(middlewareFactory, "You must provide a valid ContainerMiddlewareFactry for the factory");
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
	
	/**
	 * Determines if the factory represents a store and if
	 * so determines if it exists. If not it creates it.
	 */
	public void createStore() {
	    if( strategy.getContainerId() != null ) {
	        try {
	            get(strategy.getContainerId());
	        }
	        catch( ContainerNotFoundException e ) {
	            // Does not exist so create
	            EditableContainer c = create();
	            c.saveContainer();
	        }
	    }
	}
}
