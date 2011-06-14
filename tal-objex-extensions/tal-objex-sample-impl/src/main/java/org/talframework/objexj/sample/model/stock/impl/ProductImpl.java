/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.model.stock.impl;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.DefaultObjectStrategy;
import org.talframework.objexj.sample.api.stock.Product;
import org.talframework.objexj.sample.beans.stock.ProductBean;

/**
 * Manual simple {@link ObjexObj} implementation of the {@link Product}
 * domain object interface. Note in here how the set's are protected
 * against re-setting the same value and how they call ensureUpdateable,
 * which makes sure the object is in the transaction.
 *
 * @author Tom Spencer
 */
@XmlType(name="Product")
@XmlAccessorType(XmlAccessType.NONE)
public class ProductImpl extends BaseObjexObj implements Product { 
    public static final ObjectStrategy STRATEGY = new DefaultObjectStrategy("Product", ProductImpl.class, ProductBean.class);
    
    private final ProductBean bean;
    
    public ProductImpl() {
        throw new IllegalAccessError("Cannot create an ObjexObj instance except through the container");
    }

    public ProductImpl(ProductBean state) {
        this.bean = state;
    }
    
    @Override
    protected ObjexObjStateBean getStateBean() {
        return bean;
    }

    @XmlAttribute
    public String getName() {
        return bean.getName();
    }
    
    public void setName(String name) {
        if( name == bean.getName() ) return;
        if( name != null && name.equals(bean.getName()) ) return;
        
        ensureUpdateable(bean);
        bean.setName(name);
    }
    
    @XmlAttribute
    public String getDescription() {
        return bean.getDescription();
    }
    
    public void setDescription(String description) {
        if( description == bean.getDescription() ) return;
        if( description != null && description.equals(bean.getDescription()) ) return;
        
        ensureUpdateable(bean);
        bean.setDescription(description);
    }
    
    @XmlAttribute
    public double getPrice() {
        return bean.getPrice();
    }
    
    public void setPrice(double price) {
        if( price == bean.getPrice() ) return;
        
        ensureUpdateable(bean);
        bean.setPrice(price);
    }
    
    @XmlAttribute
    public String getCurrency() {
        return bean.getCurrency();
    }
    
    public void setCurrency(String currency) {
        if( currency == bean.getCurrency() ) return;
        if( currency != null && currency.equals(bean.getCurrency()) ) return;
        
        ensureUpdateable(bean);
        bean.setCurrency(currency);
    }
    
    @XmlAttribute
    public Date getEffectiveFrom() {
        return bean.getEffectiveFrom();
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        if( effectiveFrom == bean.getEffectiveFrom() ) return;
        if( effectiveFrom != null && effectiveFrom.equals(bean.getEffectiveFrom()) ) return;
        
        ensureUpdateable(bean);
        bean.setEffectiveFrom(effectiveFrom);
    }
    
    @XmlAttribute
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
    
    public void acceptReader(ObjexStateReader reader) {
        String name = bean.getName();
        String newName = reader.read("name", name, java.lang.String.class, ObjexFieldType.OBJECT, true);
        if( name != newName ) setName(newName);
        
        String description = bean.getDescription();
        String newDescription = reader.read("description", description, java.lang.String.class, ObjexFieldType.OBJECT, true);
        if( description != newDescription ) setDescription(newDescription);
        
        Date effectiveFrom = bean.getEffectiveFrom();
        Date newEffectiveFrom = reader.read("effectiveFrom", effectiveFrom, java.util.Date.class, ObjexFieldType.OBJECT, true);
        if( effectiveFrom != newEffectiveFrom ) setEffectiveFrom(newEffectiveFrom);
        
        Date effectiveTo = bean.getEffectiveTo();
        Date newEffectiveTo = reader.read("effectiveTo", effectiveTo, java.util.Date.class, ObjexFieldType.OBJECT, true);
        if( effectiveTo != newEffectiveTo ) setEffectiveTo(newEffectiveTo);
        
        double price = bean.getPrice();
        double newPrice = reader.read("price", price, Double.class, ObjexFieldType.OBJECT, true);
        if( price != newPrice ) setPrice(newPrice);
        
        String currency = bean.getCurrency();
        String newCurrency = reader.read("currency", currency, java.lang.String.class, ObjexFieldType.OBJECT, true);
        if( currency != newCurrency ) setCurrency(newCurrency);
    }
}
