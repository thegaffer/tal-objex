package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.order.OrderItemState;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

public class OrderItemImpl extends SimpleObjexObj implements OrderItem, OrderItemState {
    
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("OrderItem", OrderItemImpl.class, OrderItemBean.class);

    public OrderItemImpl(ObjexObjStateBean state) {
        super(STRATEGY, state);
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
		checkUpdateable();
		// TODO: Not sure here!?!
	}
	
	public String getName() {
		return getLocalState(OrderItemState.class).getName();
	}
	
	public void setName(String name) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setName(name);
	}
	
	public String getDescription() {
		return getLocalState(OrderItemState.class).getDescription();
	}
	
	public void setDescription(String description) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setDescription(description);
	}
	
	public String getStockItem() {
		return getLocalState(OrderItemState.class).getStockItem();
	}
	
	public void setStockItem(String item) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setStockItem(item);
	}
	
	public String getRef() {
		return getLocalState(OrderItemState.class).getRef();
	}
	
	public void setRef(String ref) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setRef(ref);
	}
	
	public double getQuantity() {
		return getLocalState(OrderItemState.class).getQuantity();
	}
	
	public void setQuantity(double quantity) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setQuantity(quantity);
	}
	
	public String getMeasure() {
		return getLocalState(OrderItemState.class).getMeasure();
	}
	
	public void setMeasure(String measure) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setMeasure(measure);
	}
	
	public double getPrice() {
		return getLocalState(OrderItemState.class).getPrice();
	}
	
	public void setPrice(double price) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setPrice(price);
	}
	
	public String getCurrency() {
		return getLocalState(OrderItemState.class).getCurrency();
	}
	
	public void setCurrency(String currency) {
		checkUpdateable();
		getLocalState(OrderItemState.class).setCurrency(currency);
	}
}
