package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.lang.String;
import java.util.Date;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.StateBeanUtils;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

privileged aspect ProductImpl_Roo_ObjexObj {
    
    declare parents: ProductImpl extends BaseObjexObj;
    
    public ProductBean ProductImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean ProductImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void ProductImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String ProductImpl.getName() {
        return cloneValue(bean.getName());
    }
    
    public void ProductImpl.setName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getName(), val) ) return;
        ensureUpdateable(bean);
        bean.setName(val);
    }
    
    public String ProductImpl.getDescription() {
        return cloneValue(bean.getDescription());
    }
    
    public void ProductImpl.setDescription(String val) {
        if( !StateBeanUtils.hasChanged(bean.getDescription(), val) ) return;
        ensureUpdateable(bean);
        bean.setDescription(val);
    }
    
    public Date ProductImpl.getEffectiveFrom() {
        return cloneValue(bean.getEffectiveFrom());
    }
    
    public void ProductImpl.setEffectiveFrom(Date val) {
        if( !StateBeanUtils.hasChanged(bean.getEffectiveFrom(), val) ) return;
        ensureUpdateable(bean);
        bean.setEffectiveFrom(val);
    }
    
    public Date ProductImpl.getEffectiveTo() {
        return cloneValue(bean.getEffectiveTo());
    }
    
    public void ProductImpl.setEffectiveTo(Date val) {
        if( !StateBeanUtils.hasChanged(bean.getEffectiveTo(), val) ) return;
        ensureUpdateable(bean);
        bean.setEffectiveTo(val);
    }
    
    public double ProductImpl.getPrice() {
        return bean.getPrice();
    }
    
    public void ProductImpl.setPrice(double val) {
        if( !StateBeanUtils.hasChanged(bean.getPrice(), val) ) return;
        ensureUpdateable(bean);
        bean.setPrice(val);
    }
    
    public String ProductImpl.getCurrency() {
        return cloneValue(bean.getCurrency());
    }
    
    public void ProductImpl.setCurrency(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCurrency(), val) ) return;
        ensureUpdateable(bean);
        bean.setCurrency(val);
    }
    
}
