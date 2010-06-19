package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.Date;

import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

public class ProductImpl extends SimpleObjexObj implements Product { 

    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Product", ProductImpl.class, ProductBean.class);

    public ProductImpl(ObjexObjStateBean state) {
        super(STRATEGY, state);
    }
    
    public String getName() {
        return getLocalState(ProductBean.class).getName();
    }
    
    public void setName(String name) {
        checkUpdateable();
        getLocalState(ProductBean.class).setName(name);
    }
    
    public String getDescription() {
        return getLocalState(ProductBean.class).getDescription();
    }
    
    public void setDescription(String description) {
        checkUpdateable();
        getLocalState(ProductBean.class).setDescription(description);
    }
    
    public double getPrice() {
        return getLocalState(ProductBean.class).getPrice();
    }
    
    public void setPrice(double price) {
        checkUpdateable();
        getLocalState(ProductBean.class).setPrice(price);
    }
    
    public String getCurrency() {
        return getLocalState(ProductBean.class).getCurrency();
    }
    
    public void setCurrency(String currency) {
        checkUpdateable();
        getLocalState(ProductBean.class).setCurrency(currency);
    }
    
    public Date getEffectiveFrom() {
        return getLocalState(ProductBean.class).getEffectiveFrom();
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        checkUpdateable();
        getLocalState(ProductBean.class).setEffectiveFrom(effectiveFrom);
    }
    
    public Date getEffectiveTo() {
        return getLocalState(ProductBean.class).getEffectiveTo();
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        checkUpdateable();
        getLocalState(ProductBean.class).setEffectiveTo(effectiveTo);
    }
}
