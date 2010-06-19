package org.tpspencer.tal.objexj.sample.beans.order;

import org.junit.Test;
import org.tpspencer.tal.objexj.sample.beans.BeanTestCase;

/**
 * Basic bean test for the order bean
 * 
 * @author Tom Spencer
 */
public class TestOrderItemBean extends BeanTestCase {

    public TestOrderItemBean() {
        super(OrderItemBean.class);
    }
    
    @Test
    public void basic() {
        testAccessorsAndMutators();
        testEquals();
        testHashCode();
        testToString();
    }
}
