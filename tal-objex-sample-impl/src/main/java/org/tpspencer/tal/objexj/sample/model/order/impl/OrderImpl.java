package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.order.OrderState;

public class OrderImpl implements Order {

	private ObjexObj obj;
	
	public OrderImpl(ObjexObj obj) {
		this.obj = obj;
	}
	
	public OrderState getOrderState() {
		return obj.getStateObject(OrderState.class);
	}
	
	public void setOrderState(OrderState state) {
		//obj.checkUpdateable();
		//obj.setStateObject(state);
		EditableContainer container = (EditableContainer)obj.getContainer();
		container.updateObject(obj.getId(), obj);
	}
	
	public void addItem(OrderItem item) {
		//obj.checkUpdateable();
	}
}
