/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
