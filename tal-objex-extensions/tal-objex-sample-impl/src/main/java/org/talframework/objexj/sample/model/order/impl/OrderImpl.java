/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.model.order.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.DefaultObjectStrategy;
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
@XmlType(name="Order")
@XmlAccessorType(XmlAccessType.NONE)
public class OrderImpl extends BaseObjexObj implements Order {
    public static final ObjectStrategy STRATEGY = new DefaultObjectStrategy("Order", OrderImpl.class, OrderBean.class);
    
    private final OrderBean bean;
    
    public OrderImpl() {
        throw new IllegalAccessError("Cannot create an ObjexObj instance except through the container");
    }

	public OrderImpl(OrderBean bean) {
		this.bean = bean;
	}
	
	@Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }
	
	public void acceptReader(ObjexStateReader reader) {
	    long account = bean.getAccount();
        long newAccount = reader.read("account", account, Long.class, ObjexFieldType.OBJECT, true);
        if( account != newAccount ) setAccount(newAccount);
        
        List<String> items = bean.getItems();
        List<String> newItems = reader.readReferenceList("items", items, ObjexFieldType.OWNED_REFERENCE, true);
        if( items != newItems ) setItemRefs(newItems);
    }

	@XmlAttribute
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
	
	@XmlElement(type=OrderItemImpl.class)
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
