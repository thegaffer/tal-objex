/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.locator;

import org.talframework.objexj.Container;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainer;
import org.talframework.objexj.exceptions.ContainerNotFoundException;

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
		if( strategy == null ) throw new IllegalArgumentException("You must provide a valid ContainerStrategy for the factory");
		if( middlewareFactory == null ) throw new IllegalArgumentException("You must provide a valid ContainerMiddlewareFactry for the factory");
		
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
        if( strategy == null ) throw new IllegalArgumentException("You must provide a valid ContainerStrategy for the factory");
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
        if( middlewareFactory == null ) throw new IllegalArgumentException("You must provide a valid ContainerMiddlewareFactry for the factory");
        this.middlewareFactory = middlewareFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Container create() {
        ContainerMiddleware middleware = middlewareFactory.createContainer(strategy);
        return new SimpleContainer(null, strategy, middleware, true);
    }
    
    /**
     * {@inheritDoc}
     * 
	 * Simply constructs an instance of the standard container
	 * with plugins configured.
	 */
    @Override
	public Container get(String id) {
	    ContainerMiddleware middleware = middlewareFactory.getMiddleware(strategy, id);
	    return new SimpleContainer(middleware.getContainerId(), strategy, middleware, false);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Simply constructs an instance of the basic editable 
	 * container.
	 */
    @Override
	public Container open(String id) {
	    ContainerMiddleware middleware = middlewareFactory.getTransaction(strategy, id);
	    return new SimpleContainer(middleware.getContainerId(), strategy, middleware, true);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Determines if the factory represents a store and if
	 * so determines if it exists. If not it creates it.
	 */
    @Override
	public void createStore() {
	    if( strategy.getContainerId() != null ) {
	        try {
	            get(strategy.getContainerId());
	        }
	        catch( ContainerNotFoundException e ) {
	            // Does not exist so create
	            Container c = create();
	            c.saveContainer();
	        }
	    }
	}
}
