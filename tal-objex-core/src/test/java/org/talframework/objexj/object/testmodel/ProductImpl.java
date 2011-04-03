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

package org.talframework.objexj.object.testmodel;

import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.object.testbeans.ProductBean;
import org.talframework.objexj.object.utils.SimpleFieldUtils;
import org.talframework.objexj.validation.object.SelfIntraObjectEnricher;
import org.talframework.objexj.validation.object.SelfIntraObjectValidator;

/**
 * A test objex object that allows us to test elements of 
 * Objex
 *
 * @author Tom Spencer
 */
public class ProductImpl extends BaseTestObject implements SelfIntraObjectValidator, SelfIntraObjectEnricher {

    private ProductBean bean;
    
    public ProductImpl(ProductBean bean) {
        this.bean = bean;
    }
    
    @Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }
    
    public void acceptReader(ObjexStateReader reader) {
        String name = bean.getName();
        String newName = reader.read("name", name, String.class, ObjexFieldType.STRING, true);
        if( name != newName ) setName(newName);
        
        String description = bean.getDescription();
        String newDescription = reader.read("description", description, String.class, ObjexFieldType.MEMO, true);
        if( description != newDescription ) setDescription(newDescription);
        
        int stockLevel = bean.getStockLevel();
        int newStockLevel = reader.read("stockLevel", stockLevel, int.class, ObjexFieldType.NUMBER, true);
        if( stockLevel != newStockLevel ) setStockLevel(newStockLevel);
        
        double price = bean.getPrice();
        double newPrice = reader.read("price", price, double.class, ObjexFieldType.NUMBER, true);
        if( price != newPrice ) setPrice(newPrice);
    }
    
    public void enrichObject() {
        if( bean.getDescription() == null ) bean.setDescription(bean.getName());
    }
    
    public boolean validateObject(ConstraintValidatorContext context) {
        if( bean.getPrice() == 0.0 && bean.getDescription().contains("*internal*") ) {
            return false;
        }

        return true;
    }
    
    public String getName() {
        return bean.getName();
    }
    
    public void setName(String name) {
        bean.setName(SimpleFieldUtils.setSimple(this, bean, "name", name, bean.getName()));
    }
    
    public String getDescription() {
        return bean.getDescription();
    }
    
    public void setDescription(String description) {
        bean.setDescription(SimpleFieldUtils.setSimple(this, bean, "description", description, bean.getDescription()));
    }
    
    public int getStockLevel() {
        return bean.getStockLevel();
    }
    
    public void setStockLevel(int stockLevel) {
        bean.setStockLevel(SimpleFieldUtils.setSimple(this, bean, "stockLevel", stockLevel, bean.getStockLevel()));
    }
    
    public double getPrice() {
        return bean.getPrice();
    }
    
    public void setPrice(double price) {
        bean.setPrice(SimpleFieldUtils.setSimple(this, bean, "price", price, bean.getPrice()));
    }
}
