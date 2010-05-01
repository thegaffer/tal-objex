package org.tpspencer.tal.objexj.gae;

import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;

/**
 * The base Google App Engine middleware factory. 
 * 
 * @author Tom Spencer
 */
public class GAEMiddlewareFactory implements ContainerMiddlewareFactory {
	
	public ContainerMiddleware getMiddleware(String id) {
		return new GAEContainerMiddleware();
	}
	
	public String createContainer(ContainerStrategy strategy, Object state) {
		return GAETransactionMiddleware.createContainer(strategy, state);
	}
	
	public TransactionMiddleware createTransaction(String id) {
		return new GAETransactionMiddleware();
	}
	
	public TransactionMiddleware getTransaction(String id, String transactionId) {
		return new GAETransactionMiddleware();
	}
}
