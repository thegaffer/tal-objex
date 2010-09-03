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

package org.talframework.objexj.container;

/**
 * This interface represents a class that can create 
 * ContainerMiddleware instances.
 * 
 * @author Tom Spencer
 */
public interface ContainerMiddlewareFactory {
    
    /**
     * Called to get a new {@link ContainerMiddleware} instance for
     * a new container of the given type. The container will not be 
     * persisted as a result of this call.
     * 
     * @param strategy The strategy for the container
     * @return A new transaction middleware for the editable container to use
     */
    public ContainerMiddleware createContainer(ContainerStrategy strategy);
    
    /**
	 * Called to get a new {@link ContainerMiddleware} instance for
	 * the given container.
	 * 
	 * @param strategy The strategy for the container
	 * @param id The ID of the container in question
	 * @return A new middleware for a container to use
	 */
	public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id);
	
	/**
	 * Called to get an existing transaction middleware.
	 * 
	 * @param strategy The strategy for the container
     * @param id The ID of the existing transaction or container for a new transaction
	 * @return A new transaction middleware
	 */
	public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId);
}
