package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.order.OrderState;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

public class OrderImpl extends SimpleObjexObj implements Order, OrderState {

	public OrderImpl(ObjectStrategy strategy, Container container, ObjexID id, ObjexID parent, Object state) {
		super(strategy, container, id, parent, state);
	}
	
	public OrderState getOrderState() {
		return this;
	}
	
	public void setOrderState(OrderState state) {
		checkUpdateable(true);
		// TODO: obj.setStateObject(state);
		EditableContainer container = (EditableContainer)getContainer();
		container.updateObject(super.getId(), this);
	}
	
	public void setId(Object id) {
		throw new IllegalStateException("Cannot set the ID on an object that already has an ID!");
	}
	
	public void setParentId(Object id) {
		checkUpdateable(true);
		// TODO: Not sure here!?!
	}
	
	public long getAccount() {
		return getLocalState(OrderBean.class).getAccount();
	}
	
	public void setAccount(long account) {
		checkUpdateable(true);
		getLocalState(OrderBean.class).setAccount(account);
	}
	
	public String[] getItems() {
		return getLocalState(OrderBean.class).getItems();
	}
	
	public void setItems(String[] items) {
		checkUpdateable(true);
		getLocalState(OrderBean.class).setItems(items);
	}
	
	public OrderItem createNewItem() {
		checkUpdateable(true);
		
		EditableContainer container = (EditableContainer)getContainer();
		ObjexObj item = container.newObject("OrderItem", getId());
		
		// TODO: Set reference number??!
		
		// Add to list of items
		String[] items = getItems();
		if( items == null ) items = new String[]{item.getId().toString()};
		else {
		    String[] temp = new String[items.length];
		    System.arraycopy(items, 0, temp, 0, items.length);
		    temp[items.length] = item.getId().toString();
		}
		
		setItems(items);
		
		return item.getBehaviour(OrderItem.class);
	}
	
	/**
	 * Simply uses the container to get the item
	 */
	public OrderItem getItem(Object id) {
	    // TODO: Check that is is owned by this order!!!
	    return getContainer().getObject(id, OrderItem.class);
	}
	
	public void removeItem(Object id) {
	    checkUpdateable(true);
	}
}
