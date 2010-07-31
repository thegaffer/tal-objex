package org.tpspencer.tal.objexj.container;




/**
 * This interface represents a class that can create 
 * ContainerMiddleware instances.
 * 
 * @author Tom Spencer
 */
public interface ContainerMiddlewareFactory {
    
    /**
     * Called to get a new {@link TransactionMiddleware} instance for
     * a new container of the given type. The container will not be 
     * persisted as a result of this call.
     * 
     * @param strategy The strategy for the container
     * @return A new transaction middleware for the editable container to use
     */
    public TransactionMiddleware createContainer(ContainerStrategy strategy);
    
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
	public TransactionMiddleware getTransaction(ContainerStrategy strategy, String transactionId);
}
