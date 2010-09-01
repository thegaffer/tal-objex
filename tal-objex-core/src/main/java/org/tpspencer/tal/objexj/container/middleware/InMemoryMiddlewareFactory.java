package org.tpspencer.tal.objexj.container.middleware;

import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;

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
        return new InMemoryMiddleware(null, true);
    }
}
