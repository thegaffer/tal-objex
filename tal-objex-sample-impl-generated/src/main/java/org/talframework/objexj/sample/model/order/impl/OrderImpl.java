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

import org.talframework.objexj.annotations.ObjexCheck;
import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.beans.order.OrderBean;

@ObjexObj(OrderBean.class)
public class OrderImpl implements Order {
    
    private final OrderBean bean;
    
    public OrderImpl(OrderBean bean) {
        this.bean = bean;
    }
    
    // TODO: Add on transactional so we must be in a container
    public void confirmOrder() {
        bean.getAccount();
    }
    
    @ObjexCheck(message="no.item", postObjectCheck=true)
    public boolean ensureStillNoItems() {
        return true;
    }
    
    /*@ObjexEnrich
    public void enrichItems() {
        
    }*/
    
    /*@ObjexEnrich(postObjectCheck=true)
    public void enrichMoreItems() {
        
    }*/
 
    @ObjexCheck(message="item.invalid", childObjectCheck=true)
    public boolean ensureItemOk() {
        return true;
    }
    
    @ObjexCheck(message="no.items")
    public boolean ensureItems() {
        return true;
    }
    
    
}
