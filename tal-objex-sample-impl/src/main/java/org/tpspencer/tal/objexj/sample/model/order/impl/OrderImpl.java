package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.util.ArrayList;
import java.util.List;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.order.OrderState;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

public class OrderImpl extends SimpleObjexObj implements Order, OrderState {
    
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Order", OrderImpl.class, OrderBean.class);

	public OrderImpl(ObjexObjStateBean state) {
		super(STRATEGY, state);
	}
	
	public OrderState getOrderState() {
		return this;
	}
	
	public void setOrderState(OrderState state) {
		checkUpdateable();
		// TODO: obj.setStateObject(state);
		EditableContainer container = (EditableContainer)getContainer();
		container.updateObject(super.getId(), this);
	}
	
	public void setId(Object id) {
		throw new IllegalStateException("Cannot set the ID on an object that already has an ID!");
	}
	
	public void setParentId(Object id) {
		checkUpdateable();
		// TODO: Not sure here!?!
	}
	
	public long getAccount() {
		return getLocalState(OrderBean.class).getAccount();
	}
	
	public void setAccount(long account) {
		checkUpdateable();
		getLocalState(OrderBean.class).setAccount(account);
	}
	
	public String[] getItems() {
		return getLocalState(OrderBean.class).getItems();
	}
	
	public void setItems(String[] items) {
		checkUpdateable();
		getLocalState(OrderBean.class).setItems(items);
	}
	
	public List<OrderItem> getItemsList() {
        List<OrderItem> itemList = null;
        String[] items = getItems();
        if( items != null && items.length > 0 ) {
            for( int i = 0 ; i < items.length ; i++ ) {
                OrderItem item = getContainer().getObject(items[i], OrderItem.class);
                if( item != null ) {
                    if( itemList == null ) itemList = new ArrayList<OrderItem>();
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }
	
	public OrderItem createNewItem() {
		checkUpdateable();
		
		EditableContainer container = (EditableContainer)getContainer();
		ObjexObj item = container.newObject("OrderItem", getId());
		
		String ref = null;
		
		// Add to list of items
		String[] items = getItems();
		if( items == null || items.length == 0 ) {
		    ref = "#1";
		    items = new String[]{item.getId().toString()};
		}
		else {
		    ref = "#" + Integer.toString(items.length);
		    String[] temp = new String[items.length + 1];
		    System.arraycopy(items, 0, temp, 0, items.length);
		    temp[items.length] = item.getId().toString();
		}
		
		item.getBehaviour(OrderItem.class).setRef(ref);
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
	    checkUpdateable();
	}
}
