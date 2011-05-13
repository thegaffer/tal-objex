package org.talframework.objexj.runtimes.gae;

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;

/**
 * Implements the {@link ContainerMiddlewareFactory} interface to
 * the Google App Engine backed runtime service.
 *
 * @author Tom Spencer
 */
public class GAEMiddlewareFactory implements ContainerMiddlewareFactory {

    /**
     * {@inheritDoc}
     * 
     * <p>This implementation creates a GAEMiddleware on the stratgies
     * type</p>
     */
    @Override
    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        return new GAEMiddleware(strategy.getContainerName());
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This implementation creates the middleware based on the ID. The
     * returned middleware may or may not be open depending on whether ID
     * is a transaction id or not</p>
     */
    @Override
    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        return new GAEMiddleware(strategy.getContainerName(), id);
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This implementation creates the middleware. If, having done this, 
     * the middleware is not open, it is opened.</p>
     */
    @Override
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String id) {
        GAEMiddleware middleware = new GAEMiddleware(strategy.getContainerName(), id);
        if( !middleware.isOpen() ) middleware.open();
        return middleware;
    }
}
