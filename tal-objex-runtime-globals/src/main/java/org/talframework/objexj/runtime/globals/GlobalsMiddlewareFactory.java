package org.talframework.objexj.runtime.globals;

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;

/**
 * This factory class serves up instances of the globals middleware
 * to connect to container.
 *
 * @author Tom Spencer
 */
public class GlobalsMiddlewareFactory implements ContainerMiddlewareFactory {

    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        return null; //new GlobalsMiddleware();
    }
    
    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        return null; //new GlobalsMiddleware(id);
    }
    
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId) {
        return null; //new GlobalsMiddleware(transactionId, true);
    }
}
