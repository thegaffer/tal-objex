package org.talframework.objexj.sample.beans.stock;

import java.lang.String;
import java.util.Date;

privileged aspect ProductBean_Roo_JavaBean {
    
    public String ProductBean.getName() {
        return this.name;
    }
    
    public void ProductBean.setName(String name) {
        this.name = name;
    }
    
    public String ProductBean.getDescription() {
        return this.description;
    }
    
    public void ProductBean.setDescription(String description) {
        this.description = description;
    }
    
    public Date ProductBean.getEffectiveFrom() {
        return this.effectiveFrom;
    }
    
    public void ProductBean.setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public Date ProductBean.getEffectiveTo() {
        return this.effectiveTo;
    }
    
    public void ProductBean.setEffectiveTo(Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
    
    public double ProductBean.getPrice() {
        return this.price;
    }
    
    public void ProductBean.setPrice(double price) {
        this.price = price;
    }
    
    public String ProductBean.getCurrency() {
        return this.currency;
    }
    
    public void ProductBean.setCurrency(String currency) {
        this.currency = currency;
    }
    
}
