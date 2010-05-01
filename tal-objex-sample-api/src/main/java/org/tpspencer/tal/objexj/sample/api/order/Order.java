package org.tpspencer.tal.objexj.sample.api.order;

/**
 * The business interface to an order
 * 
 * @author Tom Spencer
 */
public interface Order {

	public OrderState getOrderState();
	
	public void setOrderState(OrderState state);
	
	public void addItem(OrderItem item);
}
