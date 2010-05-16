package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.Date;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.api.stock.ProductState;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

public class ProductImpl extends SimpleObjexObj implements Product { 

    public ProductImpl(ObjectStrategy strategy, Container container, ObjexID id, ObjexID parent, Object state) {
        super(strategy, container, id, parent, state);
    }
    
    public ProductState getProductState() {
        return this;
    }
    
    public void setProductState(ProductState category) {
        checkUpdateable(true);
        
        // TODO: Update the fields that have changed!?!
    }
    
    public void setId(Object id) {
        throw new IllegalStateException("Cannot set the ID on an object that already has an ID!");
    }
    
    public void setParentId(Object id) {
        checkUpdateable(true);
        // TODO: Not sure here!?!
    }
    
    public String getName() {
        return getLocalState(ProductBean.class).getName();
    }
    
    public void setName(String name) {
        checkUpdateable(true);
        getLocalState(ProductBean.class).setName(name);
    }
    
    public String getDescription() {
        return getLocalState(ProductBean.class).getDescription();
    }
    
    public void setDescription(String description) {
        checkUpdateable(true);
        getLocalState(ProductBean.class).setDescription(description);
    }
    
    public double getPrice() {
        return getLocalState(ProductBean.class).getPrice();
    }
    
    public void setPrice(double price) {
        checkUpdateable(true);
        getLocalState(ProductBean.class).setPrice(price);
    }
    
    public String getCurrency() {
        return getLocalState(ProductBean.class).getCurrency();
    }
    
    public void setCurrency(String currency) {
        checkUpdateable(true);
        getLocalState(ProductBean.class).setCurrency(currency);
    }
    
    public Date getEffectiveFrom() {
        return getLocalState(ProductBean.class).getEffectiveFrom();
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        checkUpdateable(true);
        getLocalState(ProductBean.class).setEffectiveFrom(effectiveFrom);
    }
    
    public Date getEffectiveTo() {
        return getLocalState(ProductBean.class).getEffectiveTo();
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        checkUpdateable(true);
        getLocalState(ProductBean.class).setEffectiveTo(effectiveTo);
    }
}
