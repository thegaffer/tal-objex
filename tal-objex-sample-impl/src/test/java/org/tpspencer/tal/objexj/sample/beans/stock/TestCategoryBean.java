package org.tpspencer.tal.objexj.sample.beans.stock;

import org.junit.Test;
import org.tpspencer.tal.objexj.sample.beans.BeanTestCase;

/**
 * Basic bean test for the order bean
 * 
 * @author Tom Spencer
 */
public class TestCategoryBean extends BeanTestCase {

    public TestCategoryBean() {
        super(CategoryBean.class);
    }
    
    @Test
    public void basic() {
        testAccessorsAndMutators();
        // TODO: testEquals();
        // TODO: testHashCode();
        // TODO: testToString();
    }
}
