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

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.object.testbeans.CategoryBean;
import org.talframework.objexj.object.utils.ReferenceFieldUtils;
import org.talframework.objexj.object.utils.ReferenceListFieldUtils;
import org.talframework.objexj.object.utils.ReferenceMapFieldUtils;
import org.talframework.objexj.object.utils.SimpleFieldUtils;
import org.talframework.objexj.validation.object.SelfInterObjectEnricher;
import org.talframework.objexj.validation.object.SelfInterObjectValidator;

/**
 * A test objex object that allows us to test elements of 
 * Objex
 *
 * @author Tom Spencer
 */
public class CategoryImpl extends BaseTestObject implements SelfInterObjectValidator, SelfInterObjectEnricher {

    private CategoryBean bean;
    
    public CategoryImpl(CategoryBean bean) {
        this.bean = bean;
    }
    
    public void enrichObjectAgainstOthers() {
        // TODO: Perform some cross object enrichment
    }
    
    public boolean validateObjectAgainstOthers(ConstraintValidatorContext context) {
        // TODO: Perform some cross object validation
        return true;
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
        
        String mainProduct = bean.getMainProduct();
        String newMainProduct = reader.readReference("mainProduct", mainProduct, ObjexFieldType.REFERENCE, true);
        if( mainProduct != newMainProduct ) setMainProduct(getContainer().getObject(newMainProduct));
        
        List<String> products = bean.getProducts();
        List<String> newProducts = reader.readReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        if( products != newProducts ) setProducts(getContainer().getObjectList(newProducts));
        
        Map<String, String> categories = bean.getCategories();
        Map<String, String> newCategories = reader.readReferenceMap("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
        if( categories != newCategories ) setCategories(getContainer().getObjectMap(newCategories));
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
    
    public ObjexObj getMainProduct() {
        return ReferenceFieldUtils.getReference(this, ObjexObj.class, bean.getMainProduct());
    }
    
    public void setMainProduct(ObjexObj mainProduct) {
        bean.setMainProduct(ReferenceFieldUtils.setReference(this, bean, bean.getMainProduct(), mainProduct, true, "Product"));
    }
    
    public List<ObjexObj> getProducts() {
        return ReferenceListFieldUtils.getList(this, ObjexObj.class, bean.getProducts());
    }
    
    public void setProducts(List<ObjexObj> products) {
        bean.setProducts(ReferenceListFieldUtils.setList(this, bean, bean.getProducts(), products, true));
    }
    
    public Map<String, ObjexObj> getCategories() {
        return ReferenceMapFieldUtils.getMap(this, ObjexObj.class, bean.getCategories());
    }
    
    public void setCategories(Map<String, ObjexObj> categories) {
        bean.setCategories(ReferenceMapFieldUtils.setMap(this, bean, bean.getCategories(), categories, true));
    }
}
