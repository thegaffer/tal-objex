package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

public class OrderImpl extends SimpleObjexObj implements Order {
    
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Order", OrderImpl.class, OrderBean.class);

	public OrderImpl(ObjexObjStateBean state) {
		super(STRATEGY, state);
	}
	
	public long getAccount() {
		return getLocalState(OrderBean.class).getAccount();
	}
	
	public void setAccount(long account) {
		checkUpdateable();
		getLocalState(OrderBean.class).setAccount(account);
	}
	
	public List<String> getItemRefs() {
		return getLocalState(OrderBean.class).getItems();
	}
	
	public void setItemRefs(List<String> items) {
		checkUpdateable();
		getLocalState(OrderBean.class).setItems(items);
	}
	
	public List<OrderItem> getItems() {
        List<OrderItem> itemList = null;
        List<String> items = getItemRefs();
        if( items != null && items.size() > 0 ) {
            Iterator<String> it = items.iterator();
            while( it.hasNext() ) {
                OrderItem item = getContainer().getObject(it.next(), OrderItem.class);
                if( item != null ) {
                    if( itemList == null ) itemList = new ArrayList<OrderItem>();
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }
	
	public OrderItem createItem() {
		checkUpdateable();
		
		EditableContainer container = (EditableContainer)getContainer();
		ObjexObj item = container.newObject("OrderItem", this);
		
		// Add to list of items
		List<String> items = getItemRefs();
		if( items == null ) items = new ArrayList<String>();
		items.add(item.getId().toString());
		setItemRefs(items);
		
		String ref = "#" + items.size();
        item.getBehaviour(OrderItem.class).setRef(ref);
		
		return item.getBehaviour(OrderItem.class);
	}
	
	/**
	 * Simply uses the container to get the item
	 */
	public OrderItem getItemById(Object id) {
	    // TODO: Check that is is owned by this order!!!
	    return getContainer().getObject(id, OrderItem.class);
	}
	
	public void removeItemById(Object id) {
	    checkUpdateable();
	    // TODO:
	}
}
