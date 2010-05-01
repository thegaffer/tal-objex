package org.tpspencer.tal.objexj.container;


/**
 * This interface represents a class that can create 
 * ContainerMiddleware instances.
 * 
 * @author Tom Spencer
 */
public interface ContainerMiddlewareFactory {
	
	/**
	 * Called to get a new {@link ContainerMiddleware} instance for
	 * the given container.
	 * 
	 * @param id The ID of the container in question
	 * @return A new middleware for a container to use
	 */
	public ContainerMiddleware getMiddleware(String id);
	
	/**
	 * Called to create a new container and returns it's ID. Typically
	 * this ID is then used inside a new transaction and returned to the
	 * client. Whether the container is persisted at this point or 
	 * whether it waits for the first transaction to commit is runtime
	 * environment specific.
	 * 
	 * @param strategy The strategy of the container
	 * @param state The root objects state
	 * @return The ID of the new container
	 */
	public String createContainer(ContainerStrategy strategy, Object state);
	
	/**
	 * Called to get a new {@link TransactionMiddleware} instance for
	 * the given container.
	 * 
	 * @param id The ID of the container in question
	 * @return A new transaction middleware for the editable container to use
	 */
	public TransactionMiddleware createTransaction(String id);
	
	/**
	 * Called to get an existing transaction middleware.
	 * 
	 * @param id The ID of the container
	 * @param transactionId The ID of the transaction
	 * @return A new transaction middleware
	 */
	public TransactionMiddleware getTransaction(String id, String transactionId);
}
