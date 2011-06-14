/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.beans;

import org.junit.Test;
import org.talframework.objexj.sample.beans.order.OrderBean;
import org.talframework.objexj.sample.beans.order.OrderItemBean;
import org.talframework.objexj.sample.beans.stock.CategoryBean;
import org.talframework.objexj.sample.beans.stock.ProductBean;
import org.talframework.util.test.bean.BeanTester;

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
