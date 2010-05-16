package org.tpspencer.tal.objexj.gae;

import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerMiddlewareFactory;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;

/**
 * The base Google App Engine middleware factory. 
 * 
 * @author Tom Spencer
 */
public class GAEMiddlewareFactory implements ContainerMiddlewareFactory {
	
	public ContainerMiddleware getMiddleware(String id) {
		return new GAEMiddleware(true);
	}
	
	public TransactionMiddleware createTransaction(String id, boolean expectExists) {
		return new GAEMiddleware(expectExists);
	}
	
	public TransactionMiddleware getTransaction(String id, String transactionId) {
		return new GAEMiddleware(true);
	}
}
