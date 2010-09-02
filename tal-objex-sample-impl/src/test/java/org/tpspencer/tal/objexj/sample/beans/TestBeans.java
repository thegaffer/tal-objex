package org.tpspencer.tal.objexj.sample.beans;

import org.junit.Test;
import org.talframework.util.test.bean.BeanTester;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

/**
 * This class runs through the basic tests for the beans.
 * 
 * TODO: Test for Objex Specifics
 * 
 * @author Tom Spencer
 */
public class TestBeans {

    @Test
    public void orderBean() {
        BeanTester.testBean(OrderBean.class);
    }
    
    @Test
    public void orderItemBean() {
        BeanTester.testBean(OrderItemBean.class);
    }
    
    @Test
    public void categoryBean() {
        BeanTester.testBean(CategoryBean.class);
    }
    
    @Test
    public void productBean() {
        BeanTester.testBean(ProductBean.class);
    }
}
