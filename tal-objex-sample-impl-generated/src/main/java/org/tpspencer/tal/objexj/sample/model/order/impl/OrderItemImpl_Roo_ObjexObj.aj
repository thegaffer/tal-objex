package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.lang.String;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

privileged aspect OrderItemImpl_Roo_ObjexObj {
    
    declare parents: OrderItemImpl extends BaseObjexObj;
    
    public OrderItemBean OrderItemImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderItemImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new OrderItemBean(bean);
    }
    
    public String OrderItemImpl.getRef() {
        return bean.getRef();
    }
    
    public void OrderItemImpl.setRef(String val) {
        checkUpdateable();
        bean.setRef(val);
    }
    
    public String OrderItemImpl.getName() {
        return bean.getName();
    }
    
    public void OrderItemImpl.setName(String val) {
        checkUpdateable();
        bean.setName(val);
    }
    
    public String OrderItemImpl.getDescription() {
        return bean.getDescription();
    }
    
    public void OrderItemImpl.setDescription(String val) {
        checkUpdateable();
        bean.setDescription(val);
    }
    
    public Product OrderItemImpl.getStockItem() {
        return ObjectUtils.getObject(this, bean.getStockItem(), Product.class);
    }
    
    public String OrderItemImpl.getStockItemRef() {
        return bean.getStockItem();
    }
    
    public void OrderItemImpl.setStockItem(Product val) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(val);
        bean.setStockItem(ref);
    }
    
    public void OrderItemImpl.setStockItemRef(String val) {
        checkUpdateable();
        bean.setStockItem(val);
    }
    
    public double OrderItemImpl.getQuantity() {
        return bean.getQuantity();
    }
    
    public void OrderItemImpl.setQuantity(double val) {
        checkUpdateable();
        bean.setQuantity(val);
    }
    
    public String OrderItemImpl.getMeasure() {
        return bean.getMeasure();
    }
    
    public void OrderItemImpl.setMeasure(String val) {
        checkUpdateable();
        bean.setMeasure(val);
    }
    
    public double OrderItemImpl.getPrice() {
        return bean.getPrice();
    }
    
    public void OrderItemImpl.setPrice(double val) {
        checkUpdateable();
        bean.setPrice(val);
    }
    
    public String OrderItemImpl.getCurrency() {
        return bean.getCurrency();
    }
    
    public void OrderItemImpl.setCurrency(String val) {
        checkUpdateable();
        bean.setCurrency(val);
    }
    
}
