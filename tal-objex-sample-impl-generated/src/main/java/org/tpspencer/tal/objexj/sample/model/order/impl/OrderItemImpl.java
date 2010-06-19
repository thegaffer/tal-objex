package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.annotations.ObjexObj;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

@ObjexObj(OrderItemBean.class)
public class OrderItemImpl {

    private final OrderItemBean bean;
    
    public OrderItemImpl(OrderItemBean bean) {
        this.bean = bean;
    }
}
