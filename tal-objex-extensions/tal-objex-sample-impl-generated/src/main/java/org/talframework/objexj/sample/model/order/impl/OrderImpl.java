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
package org.talframework.objexj.sample.model.order.impl;

import org.talframework.objexj.annotations.source.ObjexCheck;
import org.talframework.objexj.annotations.source.ObjexObj;
import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.beans.order.OrderBean;

@ObjexObj(OrderBean.class)
public class OrderImpl implements Order {
    
    private final OrderBean bean;
    
    public OrderImpl() {
        throw new IllegalAccessError("Cannot construct ObjexObj directly, must use the container");
    }
    
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
