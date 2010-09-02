package org.talframework.objexj.sample.model.order.impl;

import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.sample.api.order.OrderItem;
import org.talframework.objexj.sample.beans.order.OrderItemBean;

@ObjexObj(OrderItemBean.class)
public class OrderItemImpl implements OrderItem {

    private final OrderItemBean bean;
    
    public OrderItemImpl(OrderItemBean bean) {
        this.bean = bean;
    }
}
