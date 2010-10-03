// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.model.order.impl;

import java.lang.String;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ReferenceFieldUtils;
import org.talframework.objexj.object.SimpleFieldUtils;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.order.OrderItemBean;

privileged aspect OrderItemImpl_Roo_ObjexObj {
    
    declare parents: OrderItemImpl extends BaseObjexObj;
    
    public OrderItemBean OrderItemImpl.getLocalState() {
        return bean;
    }
    
    public String OrderItemImpl.getRef() {
        String rawValue = bean.getRef();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setRef(String val) {
        String rawValue = val;
        bean.setRef(SimpleFieldUtils.setSimple(this, bean, "ref", rawValue, bean.getRef()));
    }
    
    public String OrderItemImpl.getName() {
        String rawValue = bean.getName();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setName(String val) {
        String rawValue = val;
        bean.setName(SimpleFieldUtils.setSimple(this, bean, "name", rawValue, bean.getName()));
    }
    
    public String OrderItemImpl.getDescription() {
        String rawValue = bean.getDescription();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setDescription(String val) {
        String rawValue = val;
        bean.setDescription(SimpleFieldUtils.setSimple(this, bean, "description", rawValue, bean.getDescription()));
    }
    
    public Product OrderItemImpl.getStockItem() {
        return ReferenceFieldUtils.getReference(this, Product.class, bean.getStockItem());
    }
    
    public void OrderItemImpl.setStockItem(Product val) {
        bean.setStockItem(ReferenceFieldUtils.setReference(this, bean, bean.getStockItem(), val, false, "null"));
    }
    
    public String OrderItemImpl.getStockItemRef() {
        String rawValue = bean.getStockItem();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setStockItemRef(String val) {
        String rawValue = val;
        bean.setStockItem(SimpleFieldUtils.setSimple(this, bean, "StockItemRef", rawValue, bean.getStockItem()));
    }
    
    public double OrderItemImpl.getQuantity() {
        double rawValue = bean.getQuantity();
        double val = cloneValue(rawValue);
        return val;
    }
    
    public void OrderItemImpl.setQuantity(double val) {
        double rawValue = val;
        bean.setQuantity(SimpleFieldUtils.setSimple(this, bean, "quantity", rawValue, bean.getQuantity()));
    }
    
    public String OrderItemImpl.getMeasure() {
        String rawValue = bean.getMeasure();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setMeasure(String val) {
        String rawValue = val;
        bean.setMeasure(SimpleFieldUtils.setSimple(this, bean, "measure", rawValue, bean.getMeasure()));
    }
    
    public double OrderItemImpl.getPrice() {
        double rawValue = bean.getPrice();
        double val = cloneValue(rawValue);
        return val;
    }
    
    public void OrderItemImpl.setPrice(double val) {
        double rawValue = val;
        bean.setPrice(SimpleFieldUtils.setSimple(this, bean, "price", rawValue, bean.getPrice()));
    }
    
    public String OrderItemImpl.getCurrency() {
        String rawValue = bean.getCurrency();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setCurrency(String val) {
        String rawValue = val;
        bean.setCurrency(SimpleFieldUtils.setSimple(this, bean, "currency", rawValue, bean.getCurrency()));
    }
    
}
