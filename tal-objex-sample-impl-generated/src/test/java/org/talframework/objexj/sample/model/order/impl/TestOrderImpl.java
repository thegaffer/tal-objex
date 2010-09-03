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
