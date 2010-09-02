package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.Date;

import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link Product}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
public class ProductImpl extends BaseObjexObj implements Product { 
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Product", ProductImpl.class, ProductBean.class);
    
    private final ProductBean bean;

    public ProductImpl(ProductBean state) {
        this.bean = state;
    }
    
    public String getName() {
        return bean.getName();
    }
    
    public void setName(String name) {
        if( name == bean.getName() ) return;
        if( name != null && name.equals(bean.getName()) ) return;
        
        ensureUpdateable(bean);
        bean.setName(name);
    }
    
    public String getDescription() {
        return bean.getDescription();
    }
    
    public void setDescription(String description) {
        if( description == bean.getDescription() ) return;
        if( description != null && description.equals(bean.getDescription()) ) return;
        
        ensureUpdateable(bean);
        bean.setDescription(description);
    }
    
    public double getPrice() {
        return bean.getPrice();
    }
    
    public void setPrice(double price) {
        if( price == bean.getPrice() ) return;
        
        ensureUpdateable(bean);
        bean.setPrice(price);
    }
    
    public String getCurrency() {
        return bean.getCurrency();
    }
    
    public void setCurrency(String currency) {
        if( currency == bean.getCurrency() ) return;
        if( currency != null && currency.equals(bean.getCurrency()) ) return;
        
        ensureUpdateable(bean);
        bean.setCurrency(currency);
    }
    
    public Date getEffectiveFrom() {
        return bean.getEffectiveFrom();
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        if( effectiveFrom == bean.getEffectiveFrom() ) return;
        if( effectiveFrom != null && effectiveFrom.equals(bean.getEffectiveFrom()) ) return;
        
        ensureUpdateable(bean);
        bean.setEffectiveFrom(effectiveFrom);
    }
    
    public Date getEffectiveTo() {
        return bean.getEffectiveTo();
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        if( effectiveTo == bean.getEffectiveTo() ) return;
        if( effectiveTo != null && effectiveTo.equals(bean.getEffectiveTo()) ) return;
        
        ensureUpdateable(bean);
        bean.setEffectiveTo(effectiveTo);
    }
    
    public void validate(ValidationRequest request) {
    }
}
