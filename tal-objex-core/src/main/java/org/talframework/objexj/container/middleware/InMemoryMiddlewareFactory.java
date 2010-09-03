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

package org.talframework.objexj.container.middleware;

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.exceptions.ContainerExistsException;

/**
 * Implements the middleware factory interface over the internal
 * InMemoryMiddleware implementation.
 *
 * @author Tom Spencer
 */
public class InMemoryMiddlewareFactory implements ContainerMiddlewareFactory {

    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        return new InMemoryMiddleware(id, false);
    }
    
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId) {
        return new InMemoryMiddleware(transactionId, true);
    }
    
    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        String id = strategy.getContainerId();
        if( id != null && SingletonContainerStore.getInstance().getObjects(id) != null ) {
            throw new ContainerExistsException(id);
        }
        
        return new InMemoryMiddleware(id, true);
    }
}
