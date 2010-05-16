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
	 * Called to get a new {@link TransactionMiddleware} instance for
	 * the given container.
	 * 
	 * @param id The ID of the container in question
	 * @param expectExists True if the container is expected to exist, false if it may or may not
	 * @return A new transaction middleware for the editable container to use
	 */
	public TransactionMiddleware createTransaction(String id, boolean expectExists);
	
	/**
	 * Called to get an existing transaction middleware.
	 * 
	 * @param id The ID of the container
	 * @param transactionId The ID of the transaction
	 * @return A new transaction middleware
	 */
	public TransactionMiddleware getTransaction(String id, String transactionId);
}
