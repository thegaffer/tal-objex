// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.model.stock.impl;

import java.lang.Object;
import java.lang.String;
import java.util.List;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ReferenceListFieldUtils;
import org.talframework.objexj.object.SimpleFieldUtils;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.stock.CategoryBean;

privileged aspect CategoryImpl_Roo_ObjexObj {
    
    declare parents: CategoryImpl extends BaseObjexObj;
    
    public CategoryBean CategoryImpl.getLocalState() {
        return bean;
    }
    
    public String CategoryImpl.getName() {
        String rawValue = bean.getName();
        String val = rawValue;
        return val;
    }
    
    public void CategoryImpl.setName(String val) {
        String rawValue = val;
        bean.setName(SimpleFieldUtils.setSimple(this, bean, rawValue, bean.getName()));
    }
    
    public String CategoryImpl.getDescription() {
        String rawValue = bean.getDescription();
        String val = rawValue;
        return val;
    }
    
    public void CategoryImpl.setDescription(String val) {
        String rawValue = val;
        bean.setDescription(SimpleFieldUtils.setSimple(this, bean, rawValue, bean.getDescription()));
    }
    
    public java.util.List<Product> CategoryImpl.getProducts() {
        return ReferenceListFieldUtils.getList(this, Product.class, bean.getProducts());
    }
    
    public void CategoryImpl.setProducts(java.util.List<Product> val) {
        bean.setProducts(ReferenceListFieldUtils.setList(this, bean, bean.getProducts(), val, true));
    }
    
    public Product CategoryImpl.getProductByIndex(int index) {
        return ReferenceListFieldUtils.getElementByIndex(this, Product.class, bean.getProducts(), index);
    }
    
    public Product CategoryImpl.createProduct() {
        String type = "Product";
        Product val = ReferenceListFieldUtils.createElement(this, bean, Product.class, type);
        bean.setProducts(ReferenceListFieldUtils.addElement(this, bean, bean.getProducts(), val));
        return val;
    }
    
    public void CategoryImpl.removeProductByIndex(int index) {
        bean.setProducts(ReferenceListFieldUtils.removeElementByIndex(this, bean, bean.getProducts(), true, index));
    }
    
    public void CategoryImpl.removeProductById(Object id) {
        bean.setProducts(ReferenceListFieldUtils.removeElementById(this, bean, bean.getProducts(), true, id));
    }
    
    public void CategoryImpl.removeAllProducts() {
        bean.setProducts(ReferenceListFieldUtils.removeAll(this, bean, bean.getProducts(), true));
    }
    
    public List<String> CategoryImpl.getProductRefs() {
        List<String> rawValue = bean.getProducts();
        List<String> val = rawValue;
        return val;
    }
    
    public void CategoryImpl.setProductRefs(List<String> val) {
        List<String> rawValue = val;
        bean.setProducts(SimpleFieldUtils.setSimple(this, bean, rawValue, bean.getProducts()));
    }
    
    public java.util.List<Category> CategoryImpl.getCategories() {
        return ReferenceListFieldUtils.getList(this, Category.class, bean.getCategories());
    }
    
    public void CategoryImpl.setCategories(java.util.List<Category> val) {
        bean.setCategories(ReferenceListFieldUtils.setList(this, bean, bean.getCategories(), val, true));
    }
    
    public Category CategoryImpl.getCategoryByIndex(int index) {
        return ReferenceListFieldUtils.getElementByIndex(this, Category.class, bean.getCategories(), index);
    }
    
    public Category CategoryImpl.createCategory() {
        String type = "Category";
        Category val = ReferenceListFieldUtils.createElement(this, bean, Category.class, type);
        bean.setCategories(ReferenceListFieldUtils.addElement(this, bean, bean.getCategories(), val));
        return val;
    }
    
    public void CategoryImpl.removeCategoryByIndex(int index) {
        bean.setCategories(ReferenceListFieldUtils.removeElementByIndex(this, bean, bean.getCategories(), true, index));
    }
    
    public void CategoryImpl.removeCategoryById(Object id) {
        bean.setCategories(ReferenceListFieldUtils.removeElementById(this, bean, bean.getCategories(), true, id));
    }
    
    public void CategoryImpl.removeAllCategories() {
        bean.setCategories(ReferenceListFieldUtils.removeAll(this, bean, bean.getCategories(), true));
    }
    
    public List<String> CategoryImpl.getCategoryRefs() {
        List<String> rawValue = bean.getCategories();
        List<String> val = rawValue;
        return val;
    }
    
    public void CategoryImpl.setCategoryRefs(List<String> val) {
        List<String> rawValue = val;
        bean.setCategories(SimpleFieldUtils.setSimple(this, bean, rawValue, bean.getCategories()));
    }
    
}
