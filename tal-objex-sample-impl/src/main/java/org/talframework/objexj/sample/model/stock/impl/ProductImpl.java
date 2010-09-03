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

package org.talframework.objexj.sample.model.stock.impl;

import java.util.Date;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ObjectStrategy;
import org.talframework.objexj.object.SimpleObjectStrategy;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.stock.ProductBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link Product}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
public class ProductImpl extends BaseObjexObj implements Product { 
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Product", ProductImpl.class, ProductBean.class);
    
    private final ProductBean bean;

    public ProductImpl(ProductBean state) {
        this.bean = state;
    }
    
    public String getName() {
        return bean.getName();
    }
    
    public void setName(String name) {
        if( name == bean.getName() ) return;
        if( name != null && name.equals(bean.getName()) ) return;
        
        ensureUpdateable(bean);
        bean.setName(name);
    }
    
    public String getDescription() {
        return bean.getDescription();
    }
    
    public void setDescription(String description) {
        if( description == bean.getDescription() ) return;
        if( description != null && description.equals(bean.getDescription()) ) return;
        
        ensureUpdateable(bean);
        bean.setDescription(description);
    }
    
    public double getPrice() {
        return bean.getPrice();
    }
    
    public void setPrice(double price) {
        if( price == bean.getPrice() ) return;
        
        ensureUpdateable(bean);
        bean.setPrice(price);
    }
    
    public String getCurrency() {
        return bean.getCurrency();
    }
    
    public void setCurrency(String currency) {
        if( currency == bean.getCurrency() ) return;
        if( currency != null && currency.equals(bean.getCurrency()) ) return;
        
        ensureUpdateable(bean);
        bean.setCurrency(currency);
    }
    
    public Date getEffectiveFrom() {
        return bean.getEffectiveFrom();
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        if( effectiveFrom == bean.getEffectiveFrom() ) return;
        if( effectiveFrom != null && effectiveFrom.equals(bean.getEffectiveFrom()) ) return;
        
        ensureUpdateable(bean);
        bean.setEffectiveFrom(effectiveFrom);
    }
    
    public Date getEffectiveTo() {
        return bean.getEffectiveTo();
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        if( effectiveTo == bean.getEffectiveTo() ) return;
        if( effectiveTo != null && effectiveTo.equals(bean.getEffectiveTo()) ) return;
        
        ensureUpdateable(bean);
        bean.setEffectiveTo(effectiveTo);
    }
    
    public void validate(ValidationRequest request) {
    }
}
