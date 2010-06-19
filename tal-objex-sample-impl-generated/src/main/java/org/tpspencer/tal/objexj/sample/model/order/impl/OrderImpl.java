package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.annotations.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.api.order.OrderState;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

@ObjexObj(OrderBean.class)
public class OrderImpl implements Order {
    
    private final OrderBean bean;
    
    public OrderImpl(OrderBean bean) {
        this.bean = bean;
    }
    
    // TODO: Add on transactional so we must be in a container
    public void confirmOrder() {
        bean.getAccount();
    }
    
    public OrderItem createNewItem() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void removeItem(Object id) {
        // TODO Auto-generated method stub
        
    }
    
    public OrderState getOrderState() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void setOrderState(OrderState state) {
        // TODO Auto-generated method stub
        
    }
    
    public OrderItem getItem(Object id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    ///////////////////////////////////
    // Should not be here!
    
    public void setId(Object id) {
        // TODO Auto-generated method stub
        
    }
    
    public void setParentId(Object id) {
        // TODO Auto-generated method stub
        
    }
}
