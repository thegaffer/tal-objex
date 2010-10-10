package org.talframework.objexj.locator;

import org.talframework.objexj.container.ContainerMiddleware;

/**
 * This interface represents an intercepting middleware that can sit
 * between the real middleware and the container.
 *
 * @author Tom Spencer
 */
public interface InterceptingMiddleware extends ContainerMiddleware {

    /**
     * Called to set the real middleware on the interceptor. This is
     * called by passing the intercepting middleware to the container.
     * 
     * @param middleware The read middleware for the container
     */
    public void setRealMiddleware(ContainerMiddleware middleware);
}
