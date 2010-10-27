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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.SimpleObjectStrategy;
import org.talframework.objexj.object.xml.XmlObjexObj;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.stock.CategoryBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link Category}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
@XmlType(name="Category")
@XmlAccessorType(XmlAccessType.NONE)
public class CategoryImpl extends BaseObjexObj implements Category {
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Category", CategoryImpl.class, CategoryBean.class);
    
    private final CategoryBean bean;
    
    public CategoryImpl() {
        throw new IllegalAccessError("Cannot create an ObjexObj instance except through the container");
    }

    public CategoryImpl(CategoryBean state) {
        this.bean = state;
    }
    
    @Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }

    public String getParentCategoryId() {
        ObjexID parentId = getParentId();
        return parentId != null ? parentId.toString() : null;
    }

    @XmlAttribute
    public String getName() {
        return bean.getName();
    }
    
    public void setName(String name) {
        if( name == bean.getName() ) return;
        if( name != null && name.equals(bean.getName()) ) return;
        
        ensureUpdateable(bean);
        bean.setName(name);
    }
    
    @XmlAttribute
    public String getDescription() {
        return bean.getDescription();
    }
    
    public void setDescription(String description) {
        if( description == bean.getDescription() ) return;
        if( description != null && description.equals(bean.getDescription()) ) return;
        
        ensureUpdateable(bean);
        bean.setDescription(description);
    }
    
    public List<String> getCategoryRefs() {
        return bean.getCategories();
    }
    
    @XmlElement(type=XmlObjexObj.class)
    public List<Category> getCategories() {
        return getContainer().getObjectList(bean.getCategories(), Category.class);
    }
    
    public void setCategories(List<String> categories) {
        ensureUpdateable(bean);
        bean.setCategories(categories);
    }
    
    public List<String> getProductRefs() {
        return bean.getProducts();
    }
    
    @XmlElement(type=ProductImpl.class)
    public List<Product> getProducts() {
        return getContainer().getObjectList(bean.getProducts(), Product.class);
    }
    
    public void setProducts(List<String> products) {
        ensureUpdateable(bean);
        bean.setProducts(products);
    }
    
    public Category createCategory() {
        ObjexObj newCat = getInternalContainer().newObject(this, bean, "Category");
        
        ensureUpdateable(bean);
        List<String> cats = bean.getCategories();
        if( cats == null ) cats = new ArrayList<String>();
        cats.add(newCat.getId().toString());
        bean.setCategories(cats);
        
        return newCat.getBehaviour(Category.class);
    }
    
    public Product createProduct() {
        ObjexObj newProduct = getInternalContainer().newObject(this, bean, "Product");
        
        ensureUpdateable(bean);
        List<String> prods = bean.getProducts();
        if( prods == null ) prods = new ArrayList<String>();
        prods.add(newProduct.getId().toString());
        bean.setProducts(prods);
        
        return newProduct.getBehaviour(Product.class);
    }
    
    public void validate(ValidationRequest request) {
    }
    
    public void acceptReader(ObjexStateReader reader) {
        String name = bean.getName();
        String newName = reader.read("name", name, java.lang.String.class, ObjexFieldType.OBJECT, true);
        if( name != newName ) setName(newName);
        
        String description = bean.getDescription();
        String newDescription = reader.read("description", description, java.lang.String.class, ObjexFieldType.OBJECT, true);
        if( description != newDescription ) setDescription(newDescription);
        
        List<String> products = bean.getProducts();
        List<String> newProducts = reader.readReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        if( products != newProducts ) setProducts(newProducts);
        
        List<String> categories = bean.getCategories();
        List<String> newCategories = reader.readReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
        if( categories != newCategories ) setCategories(newCategories);
    }
}
