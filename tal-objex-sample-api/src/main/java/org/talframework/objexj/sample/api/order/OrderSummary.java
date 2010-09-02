package org.talframework.objexj.sample.api.order;

/**
 * This interface represents a summary of an order. This
 * is effectively an index record into the order.
 * 
 * @author Tom Spencer
 */
public interface OrderSummary {

	public String getOrderId();
	
	public String getAccount();
	
	public int getNosItems(); 
}
