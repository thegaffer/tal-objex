// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.model.stock.impl;

import java.lang.String;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.SimpleFieldUtils;

privileged aspect ProductImpl_Roo_ObjexObj {
    
    declare parents: ProductImpl extends BaseObjexObj;
    
    declare @type: ProductImpl: @XmlType(name = "Product");
    
    declare @type: ProductImpl: @XmlAccessorType(XmlAccessType.NONE);
    
    public String ProductImpl.getType() {
        return "Product";
    }
    
    @XmlAttribute
    public String ProductImpl.getName() {
        String rawValue = bean.getName();
        String val = rawValue;
        return val;
    }
    
    public void ProductImpl.setName(String val) {
        String rawValue = val;
        bean.setName(SimpleFieldUtils.setSimple(this, bean, "name", rawValue, bean.getName()));
    }
    
    @XmlAttribute
    public String ProductImpl.getDescription() {
        String rawValue = bean.getDescription();
        String val = rawValue;
        return val;
    }
    
    public void ProductImpl.setDescription(String val) {
        String rawValue = val;
        bean.setDescription(SimpleFieldUtils.setSimple(this, bean, "description", rawValue, bean.getDescription()));
    }
    
    @XmlAttribute
    public Date ProductImpl.getEffectiveFrom() {
        Date rawValue = bean.getEffectiveFrom();
        Date val = rawValue;
        return val;
    }
    
    public void ProductImpl.setEffectiveFrom(Date val) {
        Date rawValue = val;
        bean.setEffectiveFrom(SimpleFieldUtils.setSimple(this, bean, "effectiveFrom", rawValue, bean.getEffectiveFrom()));
    }
    
    @XmlAttribute
    public Date ProductImpl.getEffectiveTo() {
        Date rawValue = bean.getEffectiveTo();
        Date val = rawValue;
        return val;
    }
    
    public void ProductImpl.setEffectiveTo(Date val) {
        Date rawValue = val;
        bean.setEffectiveTo(SimpleFieldUtils.setSimple(this, bean, "effectiveTo", rawValue, bean.getEffectiveTo()));
    }
    
    @XmlAttribute
    public double ProductImpl.getPrice() {
        double rawValue = bean.getPrice();
        double val = cloneValue(rawValue);
        return val;
    }
    
    public void ProductImpl.setPrice(double val) {
        double rawValue = val;
        bean.setPrice(SimpleFieldUtils.setSimple(this, bean, "price", rawValue, bean.getPrice()));
    }
    
    @XmlAttribute
    public String ProductImpl.getCurrency() {
        String rawValue = bean.getCurrency();
        String val = rawValue;
        return val;
    }
    
    public void ProductImpl.setCurrency(String val) {
        String rawValue = val;
        bean.setCurrency(SimpleFieldUtils.setSimple(this, bean, "currency", rawValue, bean.getCurrency()));
    }
    
}
