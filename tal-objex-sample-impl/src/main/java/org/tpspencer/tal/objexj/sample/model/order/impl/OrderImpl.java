package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.util.ArrayList;
import java.util.List;

import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link Order}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
public class OrderImpl extends BaseObjexObj implements Order {
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Order", OrderImpl.class, OrderBean.class);
    
    private final OrderBean bean;

	public OrderImpl(OrderBean bean) {
		this.bean = bean;
	}
	
	public long getAccount() {
		return bean.getAccount();
	}
	
	public void setAccount(long account) {
	    if( account == bean.getAccount() ) return;
	    
		ensureUpdateable(bean);
		bean.setAccount(account);
	}
	
	public List<String> getItemRefs() {
		return bean.getItems();
	}
	
	public void setItemRefs(List<String> items) {
		ensureUpdateable(bean);
		bean.setItems(items);
	}
	
	public List<OrderItem> getItems() {
	    return getContainer().getObjectList(bean.getItems(), OrderItem.class);
    }
	
	public OrderItem createItem() {
	    ensureUpdateable(bean);
		
		ObjexObj item = getInternalContainer().newObject(this, bean, "OrderItem");
		
		// Add to list of items
		List<String> items = bean.getItems();
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
	
	/**
	 * Currently no validation
	 */
	public void validate(ValidationRequest request) {
	}
}
