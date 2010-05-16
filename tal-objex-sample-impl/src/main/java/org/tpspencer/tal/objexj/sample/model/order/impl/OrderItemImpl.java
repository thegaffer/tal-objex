package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.order.OrderItemState;

public class OrderItemImpl extends SimpleObjexObj implements OrderItem, OrderItemState {

	public OrderItemImpl(ObjectStrategy strategy, Container container, ObjexID id, ObjexID parent, Object state) {
		super(strategy, container, id, parent, state);
	}
	
	public OrderItemState getOrderItemState() {
		return getStateObject(OrderItemState.class);
	}
	
	public void setOrderItemState(OrderItemState state) {
		// TODO Auto-generated method stub
	}
	
	public void setId(Object id) {
		throw new IllegalStateException("Cannot set the ID on an object that already has an ID!");
	}
	
	public void setParentId(Object id) {
		checkUpdateable(true);
		// TODO: Not sure here!?!
	}
	
	public String getName() {
		return getLocalState(OrderItemState.class).getName();
	}
	
	public void setName(String name) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setName(name);
	}
	
	public String getDescription() {
		return getLocalState(OrderItemState.class).getDescription();
	}
	
	public void setDescription(String description) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setDescription(description);
	}
	
	public String getStockItem() {
		return getLocalState(OrderItemState.class).getStockItem();
	}
	
	public void setStockItem(String item) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setStockItem(item);
	}
	
	public String getRef() {
		return getLocalState(OrderItemState.class).getRef();
	}
	
	public void setRef(String ref) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setRef(ref);
	}
	
	public double getQuantity() {
		return getLocalState(OrderItemState.class).getQuantity();
	}
	
	public void setQuantity(double quantity) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setQuantity(quantity);
	}
	
	public String getMeasure() {
		return getLocalState(OrderItemState.class).getMeasure();
	}
	
	public void setMeasure(String measure) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setMeasure(measure);
	}
	
	public double getPrice() {
		return getLocalState(OrderItemState.class).getPrice();
	}
	
	public void setPrice(double price) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setPrice(price);
	}
	
	public String getCurrency() {
		return getLocalState(OrderItemState.class).getCurrency();
	}
	
	public void setCurrency(String currency) {
		checkUpdateable(true);
		getLocalState(OrderItemState.class).setCurrency(currency);
	}
}
