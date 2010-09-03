/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.sample.model.order.impl;

import java.util.ArrayList;
import java.util.List;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.SimpleObjectStrategy;
import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.api.order.OrderItem;
import org.talframework.objexj.sample.beans.order.OrderBean;

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
