package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.lang.String;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
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
    
    public String[] CategoryImpl.getProducts() {
        return bean.getProducts();
    }
    
    public void CategoryImpl.setProducts(String[] val) {
        checkUpdateable();
        bean.setProducts(val);
    }
    
    public String[] CategoryImpl.getCategories() {
        return bean.getCategories();
    }
    
    public void CategoryImpl.setCategories(String[] val) {
        checkUpdateable();
        bean.setCategories(val);
    }
    
}
