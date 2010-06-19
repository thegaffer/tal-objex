package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.annotations.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.order.Order;
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
    
    
}
