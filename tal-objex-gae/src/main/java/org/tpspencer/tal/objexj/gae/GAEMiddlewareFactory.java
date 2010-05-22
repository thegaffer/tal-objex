package org.tpspencer.tal.objexj.gae;

import org.tpspencer.tal.objexj.ObjexObjStateBean;
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
	
	public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
	    return new GAEMiddleware(strategy, id);
	}
	
	public TransactionMiddleware createTransaction(ContainerStrategy strategy, String id) {
	    return new GAEMiddleware(strategy, id, (String)null);
	}
	
	public TransactionMiddleware createTransaction(ContainerStrategy strategy, String id, ObjexObjStateBean rootBean) {
	    return new GAEMiddleware(strategy, id, rootBean);
	}
	
	public TransactionMiddleware getTransaction(ContainerStrategy strategy, String id, String transactionId) {
	    return new GAEMiddleware(strategy, id, transactionId);
	}
}
