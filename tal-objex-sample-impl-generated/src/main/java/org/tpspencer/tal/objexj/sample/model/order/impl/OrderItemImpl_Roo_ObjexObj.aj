package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.lang.String;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.object.StateBeanUtils;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

privileged aspect OrderItemImpl_Roo_ObjexObj {
    
    declare parents: OrderItemImpl extends BaseObjexObj;
    
    public OrderItemBean OrderItemImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderItemImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void OrderItemImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String OrderItemImpl.getRef() {
        return cloneValue(bean.getRef());
    }
    
    public void OrderItemImpl.setRef(String val) {
        if( !StateBeanUtils.hasChanged(bean.getRef(), val) ) return;
        ensureUpdateable(bean);
        bean.setRef(val);
    }
    
    public String OrderItemImpl.getName() {
        return cloneValue(bean.getName());
    }
    
    public void OrderItemImpl.setName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getName(), val) ) return;
        ensureUpdateable(bean);
        bean.setName(val);
    }
    
    public String OrderItemImpl.getDescription() {
        return cloneValue(bean.getDescription());
    }
    
    public void OrderItemImpl.setDescription(String val) {
        if( !StateBeanUtils.hasChanged(bean.getDescription(), val) ) return;
        ensureUpdateable(bean);
        bean.setDescription(val);
    }
    
    public Product OrderItemImpl.getStockItem() {
        return ObjectUtils.getObject(this, bean.getStockItem(), Product.class);
    }
    
    public String OrderItemImpl.getStockItemRef() {
        return bean.getStockItem();
    }
    
    public void OrderItemImpl.setStockItem(Product val) {
        String ref = ObjectUtils.getObjectRef(val);
        if( !StateBeanUtils.hasChanged(bean.getStockItem(), ref) ) return;
        ensureUpdateable(bean);
        bean.setStockItem(ref);
    }
    
    public void OrderItemImpl.setStockItemRef(String val) {
        if( !StateBeanUtils.hasChanged(bean.getStockItem(), val) ) return;
        ensureUpdateable(bean);
        bean.setStockItem(val);
    }
    
    public double OrderItemImpl.getQuantity() {
        return bean.getQuantity();
    }
    
    public void OrderItemImpl.setQuantity(double val) {
        if( !StateBeanUtils.hasChanged(bean.getQuantity(), val) ) return;
        ensureUpdateable(bean);
        bean.setQuantity(val);
    }
    
    public String OrderItemImpl.getMeasure() {
        return cloneValue(bean.getMeasure());
    }
    
    public void OrderItemImpl.setMeasure(String val) {
        if( !StateBeanUtils.hasChanged(bean.getMeasure(), val) ) return;
        ensureUpdateable(bean);
        bean.setMeasure(val);
    }
    
    public double OrderItemImpl.getPrice() {
        return bean.getPrice();
    }
    
    public void OrderItemImpl.setPrice(double val) {
        if( !StateBeanUtils.hasChanged(bean.getPrice(), val) ) return;
        ensureUpdateable(bean);
        bean.setPrice(val);
    }
    
    public String OrderItemImpl.getCurrency() {
        return cloneValue(bean.getCurrency());
    }
    
    public void OrderItemImpl.setCurrency(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCurrency(), val) ) return;
        ensureUpdateable(bean);
        bean.setCurrency(val);
    }
    
}
