package org.tpspencer.tal.objexj.sample.beans.order;

import org.junit.Test;
import org.tpspencer.tal.objexj.sample.beans.BeanTestCase;

/**
 * Basic bean test for the order bean
 * 
 * @author Tom Spencer
 */
public class TestOrderBean extends BeanTestCase {

    public TestOrderBean() {
        super(OrderBean.class);
    }
    
    @Test
    public void basic() {
        testAccessorsAndMutators();
        // TODO: testEquals();
        // TODO: testHashCode();
        // TODO: testToString();
    }
}
