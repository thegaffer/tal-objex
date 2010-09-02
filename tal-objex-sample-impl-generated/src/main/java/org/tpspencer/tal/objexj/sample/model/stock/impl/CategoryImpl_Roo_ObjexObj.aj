package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.object.StateBeanUtils;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

privileged aspect CategoryImpl_Roo_ObjexObj {
    
    declare parents: CategoryImpl extends BaseObjexObj;
    
    public CategoryBean CategoryImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean CategoryImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void CategoryImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String CategoryImpl.getName() {
        return cloneValue(bean.getName());
    }
    
    public void CategoryImpl.setName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getName(), val) ) return;
        ensureUpdateable(bean);
        bean.setName(val);
    }
    
    public String CategoryImpl.getDescription() {
        return cloneValue(bean.getDescription());
    }
    
    public void CategoryImpl.setDescription(String val) {
        if( !StateBeanUtils.hasChanged(bean.getDescription(), val) ) return;
        ensureUpdateable(bean);
        bean.setDescription(val);
    }
    
    public java.util.List<Product> CategoryImpl.getProducts() {
        return getContainer().getObjectList(bean.getProducts(), Product.class);
    }
    
    public Product CategoryImpl.getProductById(Object id) {
        ObjectUtils.testObjectHeld(bean.getProducts(), id);
        return ObjectUtils.getObject(this, id, org.tpspencer.tal.objexj.sample.api.stock.Product.class);
    }
    
    public List<String> CategoryImpl.getProductRefs() {
        return cloneValue(bean.getProducts());
    }
    
    public Product CategoryImpl.createProduct() {
        checkUpdateable();
        ObjexObj val = ObjectUtils.createObject(this, bean, "Product");
        ensureUpdateable(bean);
        List<String> refs = bean.getProducts();
        if( refs == null ) {
        	refs = new ArrayList<String>();
        	bean.setProducts(refs);
        }
        refs.add(val.getId().toString());
        return val.getBehaviour(Product.class);
    }
    
    public void CategoryImpl.removeProductById(Object id) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(this, id);
        List<String> refs = bean.getProducts();
        if( refs == null || refs.size() == 0 ) return;
        int size = refs.size();
        Iterator<String> it = refs.iterator();
        while( it.hasNext() ) {
        	if( ref.equals(it.next()) ) {
        		ensureUpdateable(bean);
        		it.remove();
        	}
        }
        if( refs.size() == size ) return;
        ObjectUtils.removeObject(this, bean, ref);
    }
    
    public void CategoryImpl.removeProduct(int index) {
        checkUpdateable();
        List<String> refs = bean.getProducts();
        if( refs == null || index < 0 || index >= refs.size() ) return;
        String ref = refs.get(index);
        ensureUpdateable(bean);
        refs.remove(index);
        ObjectUtils.removeObject(this, bean, ref);
    }
    
    public void CategoryImpl.removeProducts() {
        List<String> refs = bean.getProducts();
        if( refs == null || refs.size() == 0 ) return;
        checkUpdateable();
        Iterator<String> it = refs.iterator();
        while( it.hasNext() ) {
        	String ref = it.next();
        	ObjectUtils.removeObject(this, bean, ref);
        	it.remove();
        }
        ensureUpdateable(bean);
        bean.setProducts(null);
    }
    
    public java.util.List<Category> CategoryImpl.getCategories() {
        return getContainer().getObjectList(bean.getCategories(), Category.class);
    }
    
    public Category CategoryImpl.getCategoryById(Object id) {
        ObjectUtils.testObjectHeld(bean.getCategories(), id);
        return ObjectUtils.getObject(this, id, org.tpspencer.tal.objexj.sample.api.stock.Category.class);
    }
    
    public List<String> CategoryImpl.getCategoryRefs() {
        return cloneValue(bean.getCategories());
    }
    
    public Category CategoryImpl.createCategory() {
        checkUpdateable();
        ObjexObj val = ObjectUtils.createObject(this, bean, "Category");
        ensureUpdateable(bean);
        List<String> refs = bean.getCategories();
        if( refs == null ) {
        	refs = new ArrayList<String>();
        	bean.setCategories(refs);
        }
        refs.add(val.getId().toString());
        return val.getBehaviour(Category.class);
    }
    
    public void CategoryImpl.removeCategoryById(Object id) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(this, id);
        List<String> refs = bean.getCategories();
        if( refs == null || refs.size() == 0 ) return;
        int size = refs.size();
        Iterator<String> it = refs.iterator();
        while( it.hasNext() ) {
        	if( ref.equals(it.next()) ) {
        		ensureUpdateable(bean);
        		it.remove();
        	}
        }
        if( refs.size() == size ) return;
        ObjectUtils.removeObject(this, bean, ref);
    }
    
    public void CategoryImpl.removeCategory(int index) {
        checkUpdateable();
        List<String> refs = bean.getCategories();
        if( refs == null || index < 0 || index >= refs.size() ) return;
        String ref = refs.get(index);
        ensureUpdateable(bean);
        refs.remove(index);
        ObjectUtils.removeObject(this, bean, ref);
    }
    
    public void CategoryImpl.removeCategories() {
        List<String> refs = bean.getCategories();
        if( refs == null || refs.size() == 0 ) return;
        checkUpdateable();
        Iterator<String> it = refs.iterator();
        while( it.hasNext() ) {
        	String ref = it.next();
        	ObjectUtils.removeObject(this, bean, ref);
        	it.remove();
        }
        ensureUpdateable(bean);
        bean.setCategories(null);
    }
    
}
