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
