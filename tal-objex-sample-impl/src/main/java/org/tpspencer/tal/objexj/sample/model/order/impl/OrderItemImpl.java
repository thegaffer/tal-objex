package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

public class OrderItemImpl extends SimpleObjexObj implements OrderItem {
    
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("OrderItem", OrderItemImpl.class, OrderItemBean.class);

    public OrderItemImpl(ObjexObjStateBean state) {
        super(STRATEGY, state);
    }

	public String getName() {
		return getLocalState(OrderItemBean.class).getName();
	}
	
	public void setName(String name) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setName(name);
	}
	
	public String getDescription() {
		return getLocalState(OrderItemBean.class).getDescription();
	}
	
	public void setDescription(String description) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setDescription(description);
	}
	
	public Product getStockItem() {
		String ref = getLocalState(OrderItemBean.class).getStockItem();
		return ref != null ? getContainer().getObject(ref, Product.class) : null;
	}
	
	public String getStockItemRef() {
        return getLocalState(OrderItemBean.class).getStockItem();
    }
	
	public void setStockItem(Product item) {
		String ref = ObjectUtils.getObjectRef(item);
		setStockItemRef(ref);
	}
	
	public void setStockItemRef(String item) {
        checkUpdateable();
        getLocalState(OrderItemBean.class).setStockItem(item);
    }
	
	public String getRef() {
		return getLocalState(OrderItemBean.class).getRef();
	}
	
	public void setRef(String ref) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setRef(ref);
	}
	
	public double getQuantity() {
		return getLocalState(OrderItemBean.class).getQuantity();
	}
	
	public void setQuantity(double quantity) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setQuantity(quantity);
	}
	
	public String getMeasure() {
		return getLocalState(OrderItemBean.class).getMeasure();
	}
	
	public void setMeasure(String measure) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setMeasure(measure);
	}
	
	public double getPrice() {
		return getLocalState(OrderItemBean.class).getPrice();
	}
	
	public void setPrice(double price) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setPrice(price);
	}
	
	public String getCurrency() {
		return getLocalState(OrderItemBean.class).getCurrency();
	}
	
	public void setCurrency(String currency) {
		checkUpdateable();
		getLocalState(OrderItemBean.class).setCurrency(currency);
	}
}
