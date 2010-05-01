package org.tpspencer.tal.objexj.sample.api.order;

public interface OrderItem {

	/**
	 * @return The state of the order item
	 */
	public OrderItemState getOrderItemState();
	
	/**
	 * Call to set the basic state of the order item.
	 * 
	 * @param state The new state of the order item
	 */
	public void setOrderItemState(OrderItemState state); 
}
