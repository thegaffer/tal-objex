package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

privileged aspect CategoryImpl_Roo_ObjexObj {
    
    declare parents: CategoryImpl extends BaseObjexObj;
    
    public CategoryBean CategoryImpl.getLocalState() {
        return bean;
    }
    
    public CategoryBean CategoryImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new CategoryBean(bean);
    }
    
    public String CategoryImpl.getName() {
        return bean.getName();
    }
    
    public void CategoryImpl.setName(String val) {
        checkUpdateable();
        bean.setName(val);
    }
    
    public String CategoryImpl.getDescription() {
        return bean.getDescription();
    }
    
    public void CategoryImpl.setDescription(String val) {
        checkUpdateable();
        bean.setDescription(val);
    }
    
    public java.util.List<Product> CategoryImpl.getProducts() {
        return ObjectUtils.getObjectList(this, bean.getProducts(), Product.class);
    }
    
    public Product CategoryImpl.getProductById(Object id) {
        ObjectUtils.testObjectHeld(bean.getProducts(), id);
        return ObjectUtils.getObject(this, id, org.tpspencer.tal.objexj.sample.api.stock.Product.class);
    }
    
    public List<String> CategoryImpl.getProductRefs() {
        return bean.getProducts();
    }
    
    public Product CategoryImpl.createProduct() {
        checkUpdateable();
        ObjexObj val = ObjectUtils.createObject(this, "ProductBean");
        if( bean.getProducts() == null ) bean.setProducts(new ArrayList<String>());
        bean.getProducts().add(val.getId().toString());
        return val.getBehaviour(Product.class);
    }
    
    public void CategoryImpl.removeProductById(Object id) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(this, id);
        Iterator<String> it = bean.getProducts().iterator();
        while( it.hasNext() ) {
        	if( ref.equals(it.next()) ) it.remove();
        }
        ObjectUtils.removeObject(this, ref);
    }
    
    public void CategoryImpl.removeProduct(int index) {
        checkUpdateable();
        List<String> refs = bean.getProducts();
        if( refs != null && index >= 0 && index < refs.size() ) {
        	String ref = refs.get(index);
        	ObjectUtils.removeObject(this, ref);
        	refs.remove(index);
        }
    }
    
    public void CategoryImpl.removeProducts() {
        checkUpdateable();
        Iterator<String> it = bean.getProducts().iterator();
        while( it.hasNext() ) {
        	String ref = it.next();
        	ObjectUtils.removeObject(this, ref);
        	it.remove();
        }
        bean.setProducts(null);
    }
    
    public java.util.List<Category> CategoryImpl.getCategories() {
        return ObjectUtils.getObjectList(this, bean.getCategories(), Category.class);
    }
    
    public Category CategoryImpl.getCategoryById(Object id) {
        ObjectUtils.testObjectHeld(bean.getCategories(), id);
        return ObjectUtils.getObject(this, id, org.tpspencer.tal.objexj.sample.api.stock.Category.class);
    }
    
    public List<String> CategoryImpl.getCategoryRefs() {
        return bean.getCategories();
    }
    
    public Category CategoryImpl.createCategory() {
        checkUpdateable();
        ObjexObj val = ObjectUtils.createObject(this, "CategoryBean");
        if( bean.getCategories() == null ) bean.setCategories(new ArrayList<String>());
        bean.getCategories().add(val.getId().toString());
        return val.getBehaviour(Category.class);
    }
    
    public void CategoryImpl.removeCategoryById(Object id) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(this, id);
        Iterator<String> it = bean.getCategories().iterator();
        while( it.hasNext() ) {
        	if( ref.equals(it.next()) ) it.remove();
        }
        ObjectUtils.removeObject(this, ref);
    }
    
    public void CategoryImpl.removeCategory(int index) {
        checkUpdateable();
        List<String> refs = bean.getCategories();
        if( refs != null && index >= 0 && index < refs.size() ) {
        	String ref = refs.get(index);
        	ObjectUtils.removeObject(this, ref);
        	refs.remove(index);
        }
    }
    
    public void CategoryImpl.removeCategories() {
        checkUpdateable();
        Iterator<String> it = bean.getCategories().iterator();
        while( it.hasNext() ) {
        	String ref = it.next();
        	ObjectUtils.removeObject(this, ref);
        	it.remove();
        }
        bean.setCategories(null);
    }
    
}
