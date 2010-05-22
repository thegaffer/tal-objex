package org.tpspencer.tal.objexj.container;

import org.tpspencer.tal.objexj.ObjexObjStateBean;



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
	 * @param strategy The strategy for the container
	 * @param id The ID of the container in question
	 * @return A new middleware for a container to use
	 */
	public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id);
	
	/**
     * Called to get a new {@link TransactionMiddleware} instance for
     * the given container. The container must exist already.
     * 
     * @param strategy The strategy for the container
     * @param id The ID of the container in question
     * @return A new transaction middleware for the editable container to use
     */
    public TransactionMiddleware createTransaction(ContainerStrategy strategy, String id);
	
	/**
	 * Called to get a new {@link TransactionMiddleware} instance for
	 * a new container with the given ID. The container must not already
	 * exist.
	 * 
	 * @param strategy The strategy for the container
     * @param id The ID of the container in question
	 * @param rootBean The root object for the container
	 * @return A new transaction middleware for the editable container to use
	 */
	public TransactionMiddleware createTransaction(ContainerStrategy strategy, String id, ObjexObjStateBean rootBean);
	
	/**
	 * Called to get an existing transaction middleware.
	 * 
	 * @param strategy The strategy for the container
     * @param id The ID of the container
	 * @param transactionId The ID of the transaction
	 * @return A new transaction middleware
	 */
	public TransactionMiddleware getTransaction(ContainerStrategy strategy, String id, String transactionId);
}
