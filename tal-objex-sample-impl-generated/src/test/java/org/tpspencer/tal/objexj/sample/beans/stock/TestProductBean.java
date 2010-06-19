package org.tpspencer.tal.objexj.sample.beans.stock;

import org.junit.Test;
import org.tpspencer.tal.objexj.sample.beans.BeanTestCase;

/**
 * Basic bean test for the order bean
 * 
 * @author Tom Spencer
 */
public class TestProductBean extends BeanTestCase {

    public TestProductBean() {
        super(ProductBean.class);
    }
    
    @Test
    public void basic() {
        testAccessorsAndMutators();
        testEquals();
        testHashCode();
        testToString();
    }
}
