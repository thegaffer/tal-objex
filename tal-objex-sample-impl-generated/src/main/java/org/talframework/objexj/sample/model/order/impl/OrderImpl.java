package org.talframework.objexj.sample.model.order.impl;

import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.beans.order.OrderBean;

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
