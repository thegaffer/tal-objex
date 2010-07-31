package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.lang.String;
import java.util.Date;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

privileged aspect ProductImpl_Roo_ObjexObj {
    
    declare parents: ProductImpl extends BaseObjexObj;
    
    public ProductBean ProductImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean ProductImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new ProductBean(bean);
    }
    
    public String ProductImpl.getName() {
        return bean.getName();
    }
    
    public void ProductImpl.setName(String val) {
        checkUpdateable();
        bean.setName(val);
    }
    
    public String ProductImpl.getDescription() {
        return bean.getDescription();
    }
    
    public void ProductImpl.setDescription(String val) {
        checkUpdateable();
        bean.setDescription(val);
    }
    
    public Date ProductImpl.getEffectiveFrom() {
        return bean.getEffectiveFrom();
    }
    
    public void ProductImpl.setEffectiveFrom(Date val) {
        checkUpdateable();
        bean.setEffectiveFrom(val);
    }
    
    public Date ProductImpl.getEffectiveTo() {
        return bean.getEffectiveTo();
    }
    
    public void ProductImpl.setEffectiveTo(Date val) {
        checkUpdateable();
        bean.setEffectiveTo(val);
    }
    
    public double ProductImpl.getPrice() {
        return bean.getPrice();
    }
    
    public void ProductImpl.setPrice(double val) {
        checkUpdateable();
        bean.setPrice(val);
    }
    
    public String ProductImpl.getCurrency() {
        return bean.getCurrency();
    }
    
    public void ProductImpl.setCurrency(String val) {
        checkUpdateable();
        bean.setCurrency(val);
    }
    
}
