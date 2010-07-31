package org.tpspencer.tal.objexj.sample.beans.order;

import junit.framework.Assert;

import org.junit.Test;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
import org.tpspencer.tal.objexj.sample.beans.BeanTestCase;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

public class TestOrderBean extends BeanTestCase {
    
    public TestOrderBean() {
        super(OrderBean.class);
    }

    @Test
    public void basic() {
        testAccessorsAndMutators();
        testEquals();
        testHashCode();
        testToString();
    }
    
    @Test
    public void testConstructors() {
        OrderBean bean = new OrderBean(new DefaultObjexID("Test/456"));
        bean.init("123");
        Assert.assertEquals("123", bean.getId());
        Assert.assertEquals("Test/456", bean.getParentId());
        // Assert.assertEquals(123, bean.getAccount());
        
        bean.setAccount(987);
        
        OrderBean bean2 = new OrderBean(bean);
        Assert.assertEquals("123", bean2.getId());
        Assert.assertEquals("Test/456", bean2.getParentId());
        Assert.assertEquals(987, bean2.getAccount());
    }
}
