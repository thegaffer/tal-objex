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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.locator.SingletonContainerLocator;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.DefaultObjectStrategy;
import org.talframework.objexj.object.utils.ObjectUtils;
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
@XmlType(name="OrderItem")
@XmlAccessorType(XmlAccessType.NONE)
public class OrderItemImpl extends BaseObjexObj implements OrderItem {
    public static final ObjectStrategy STRATEGY = new DefaultObjectStrategy("OrderItem", OrderItemImpl.class, OrderItemBean.class);
    
    private final OrderItemBean bean;
    
    public OrderItemImpl() {
        throw new IllegalAccessError("Cannot create an ObjexObj instance except through the container");
    }
    
    public OrderItemImpl(OrderItemBean bean) {
        this.bean = bean;
    }
    
    @Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }

    @XmlAttribute
	public String getName() {
		return bean.getName();
	}
	
	public void setName(String name) {
	    if( name == bean.getName() ) return;
	    if( name != null && name.equals(bean.getName()) ) return;
	    
		ensureUpdateable(bean);
		bean.setName(name);
	}
	
	@XmlAttribute
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
	
	@XmlAttribute(name="stockItem")
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
	
	@XmlAttribute
    public String getRef() {
		return bean.getRef();
	}
	
	public void setRef(String ref) {
	    if( ref == bean.getRef() ) return;
	    if( ref != null && ref.equals(bean.getRef()) ) return;
	    
	    ensureUpdateable(bean);
        bean.setRef(ref);
	}
	
	@XmlAttribute
    public double getQuantity() {
		return bean.getQuantity();
	}
	
	public void setQuantity(double quantity) {
	    if( quantity == bean.getQuantity() ) return;
	    
	    ensureUpdateable(bean);
        bean.setQuantity(quantity);
	}
	
	@XmlAttribute
    public String getMeasure() {
		return bean.getMeasure();
	}
	
	public void setMeasure(String measure) {
	    if( measure == bean.getMeasure() ) return;
	    if( measure != null && measure.equals(bean.getMeasure()) ) return;
	    
	    ensureUpdateable(bean);
        bean.setMeasure(measure);
	}
	
	@XmlAttribute
    public double getPrice() {
		return bean.getPrice();
	}
	
	public void setPrice(double price) {
	    if( price == bean.getPrice() ) return;
	    
	    ensureUpdateable(bean);
        bean.setPrice(price);
	}
	
	@XmlAttribute
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
	
	public void acceptReader(ObjexStateReader reader) {
	    String ref = bean.getRef();
        String newRef = reader.read("ref", ref, String.class, ObjexFieldType.OBJECT, true);
        if( newRef != ref ) setRef(newRef);
        
        String name = bean.getName();
        String newName = reader.read("name", name, String.class, ObjexFieldType.OBJECT, true);
        if( name != newName ) setName(newName);
        
        String description = bean.getDescription();
        String newDescription = reader.read("description", description, String.class, ObjexFieldType.OBJECT, true);
        if( description != newDescription ) setDescription(newDescription);
        
        String stockItem = bean.getStockItem();
        String newStockItem = reader.readReference("stockItem", stockItem, ObjexFieldType.REFERENCE, true);
        if( stockItem != newStockItem ) setStockItemRef(stockItem);
        
        double quantity = bean.getQuantity(); 
        double newQuantity = reader.read("quantity", quantity, Double.class, ObjexFieldType.OBJECT, true);
        if( quantity != newQuantity ) setQuantity(newQuantity);
        
        String measure = bean.getMeasure();
        String newMeasure = reader.read("measure", measure, String.class, ObjexFieldType.OBJECT, true);
        if( measure != newMeasure ) setMeasure(newMeasure);
        
        double price = bean.getPrice();
        double newPrice = reader.read("price", price, Double.class, ObjexFieldType.OBJECT, true);
        if( price != newPrice ) setPrice(newPrice);
        
        String currency = bean.getCurrency();
        String newCurrency = reader.read("currency", currency, String.class, ObjexFieldType.OBJECT, true);
        if( currency != newCurrency ) setCurrency(newCurrency);
        
    }
}
