package org.tpspencer.tal.objexj.container.middleware;

import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.exceptions.ContainerExistsException;

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
