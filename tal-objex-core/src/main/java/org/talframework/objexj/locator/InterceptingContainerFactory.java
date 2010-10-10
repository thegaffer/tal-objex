package org.talframework.objexj.locator;

import org.talframework.objexj.Container;

/**
 * This version of the {@link ContainerFactory} interface allows
 * the client to specify an interceptor between the container and
 * the middleware. This should be used with care because it allows
 * access to the internals of Objex.
 * 
 * <p>The predicted usage for this is when we are remoting the
 * container over some boundary. On the client side we may want
 * to place an interceptor so we can cache objects. On the 
 * server side the interceptor allows the service access to the
 * state beans.</p>
 *
 * @author Tom Spencer
 */
public interface InterceptingContainerFactory {

    /**
     * Creates a new container of this type.
     * 
     * @param interceptor The intercepting middleware
     * @return The open container (the root object and any defaults have been set)
     */
    public Container create(InterceptingMiddleware interceptor);
    
    /**
     * Gets a container if this type given its ID. If the
     * ID represents a transaction then the transaction is
     * returned.
     * 
     * @param interceptor The intercepting middleware
     * @param id The ID of the container (or transaction)
     * @return The container
     */
    public Container get(InterceptingMiddleware interceptor, String id);
    
    /**
     * Either opens a container for editing for the first
     * time or re-opens a previously suspended transaction 
     * as determined by the type of ID.
     * 
     * @param interceptor The intercepting middleware
     * @param id The ID of the container or transaction to open
     * @return The opened container
     */
    public Container open(InterceptingMiddleware interceptor, String id);
}
