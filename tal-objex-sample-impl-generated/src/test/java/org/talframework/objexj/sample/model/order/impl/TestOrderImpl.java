package org.talframework.objexj.sample.model.order.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.talframework.objexj.sample.beans.order.OrderBean;
import org.talframework.objexj.sample.model.order.impl.OrderImpl;

public class TestOrderImpl {

    @Test
    public void basic() {
        OrderBean bean = new OrderBean();
        bean.setAccount(123);
        
        OrderImpl impl = new OrderImpl(bean);
        Assert.assertEquals(123, impl.getAccount());
    }
}
