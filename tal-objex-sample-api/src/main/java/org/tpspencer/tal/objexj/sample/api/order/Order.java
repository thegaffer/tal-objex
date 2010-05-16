package org.tpspencer.tal.objexj.sample.api.order;

/**
 * The business interface to an order
 * 
 * @author Tom Spencer
 */
public interface Order extends OrderState {

	public OrderState getOrderState();
	
	public void setOrderState(OrderState state);
	
	public OrderItem createNewItem();
	
	public OrderItem getItem(Object id);
	
	public void removeItem(Object id);
}
