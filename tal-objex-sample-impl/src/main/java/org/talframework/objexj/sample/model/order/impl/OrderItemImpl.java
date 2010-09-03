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

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.locator.SingletonContainerLocator;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ObjectStrategy;
import org.talframework.objexj.object.ObjectUtils;
import org.talframework.objexj.object.SimpleObjectStrategy;
import org.talframework.objexj.sample.api.order.OrderItem;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.order.OrderItemBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link OrderItem}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
public class OrderItemImpl extends BaseObjexObj implements OrderItem {
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("OrderItem", OrderItemImpl.class, OrderItemBean.class);
    
    private final OrderItemBean bean;
    
    public OrderItemImpl(OrderItemBean bean) {
        this.bean = bean;
    }

	public String getName() {
		return bean.getName();
	}
	
	public void setName(String name) {
	    if( name == bean.getName() ) return;
	    if( name != null && name.equals(bean.getName()) ) return;
	    
		ensureUpdateable(bean);
		bean.setName(name);
	}
	
	public String getDescription() {
		return bean.getDescription();
	}
	
	public void setDescription(String description) {
	    if( description == bean.getDescription() ) return;
	    if( description != null && description.equals(description) ) return;
	    
		checkUpdateable();
		bean.setDescription(description);
	}
	
	public Product getStockItem() {
		String ref = bean.getStockItem();
		if( ref == null ) return null;
		
		Container stock = SingletonContainerLocator.getInstance().get("Stock");
		return stock.getObject(ref).getBehaviour(Product.class);
	}
	
	public String getStockItemRef() {
        return bean.getStockItem();
    }
	
	public void setStockItem(Product item) {
	    String ref = ObjectUtils.getObjectRef(item);
		setStockItemRef(ref);
	}
	
	public void setStockItemRef(String item) {
	    if( item == bean.getStockItem() ) return;
	    if( item != null && item.equals(bean.getStockItem()) ) return;
	    
	    ensureUpdateable(bean);
        bean.setStockItem(item);
    }
	
	public String getRef() {
		return bean.getRef();
	}
	
	public void setRef(String ref) {
	    if( ref == bean.getRef() ) return;
	    if( ref != null && ref.equals(bean.getRef()) ) return;
	    
	    ensureUpdateable(bean);
        bean.setRef(ref);
	}
	
	public double getQuantity() {
		return bean.getQuantity();
	}
	
	public void setQuantity(double quantity) {
	    if( quantity == bean.getQuantity() ) return;
	    
	    ensureUpdateable(bean);
        bean.setQuantity(quantity);
	}
	
	public String getMeasure() {
		return bean.getMeasure();
	}
	
	public void setMeasure(String measure) {
	    if( measure == bean.getMeasure() ) return;
	    if( measure != null && measure.equals(bean.getMeasure()) ) return;
	    
	    ensureUpdateable(bean);
        bean.setMeasure(measure);
	}
	
	public double getPrice() {
		return bean.getPrice();
	}
	
	public void setPrice(double price) {
	    if( price == bean.getPrice() ) return;
	    
	    ensureUpdateable(bean);
        bean.setPrice(price);
	}
	
	public String getCurrency() {
		return bean.getCurrency();
	}
	
	public void setCurrency(String currency) {
	    if( currency == bean.getCurrency() ) return;
	    if( currency != null && currency.equals(bean.getCurrency()) ) return;
	    
	    ensureUpdateable(bean);
        bean.setCurrency(currency);
	}
	
	public void validate(ValidationRequest request) {
	}
}
