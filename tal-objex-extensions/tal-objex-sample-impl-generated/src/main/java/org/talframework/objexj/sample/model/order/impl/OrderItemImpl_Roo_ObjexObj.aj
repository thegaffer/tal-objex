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
// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.model.order.impl;

import java.lang.String;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.utils.ReferenceFieldUtils;
import org.talframework.objexj.object.utils.SimpleFieldUtils;
import org.talframework.objexj.sample.api.stock.Product;

privileged aspect OrderItemImpl_Roo_ObjexObj {
    
    declare parents: OrderItemImpl extends BaseObjexObj;
    
    declare @type: OrderItemImpl: @XmlType(name = "OrderItem");
    
    declare @type: OrderItemImpl: @XmlAccessorType(XmlAccessType.NONE);
    
    public String OrderItemImpl.getType() {
        return "OrderItem";
    }
    
    @XmlAttribute
    public String OrderItemImpl.getRef() {
        String rawValue = bean.getId();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setRef(String val) {
        String rawValue = val;
        bean.setId(SimpleFieldUtils.setSimple(this, bean, "ref", rawValue, bean.getId()));
    }
    
    @XmlAttribute
    public String OrderItemImpl.getName() {
        String rawValue = bean.getName();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setName(String val) {
        String rawValue = val;
        bean.setName(SimpleFieldUtils.setSimple(this, bean, "name", rawValue, bean.getName()));
    }
    
    @XmlAttribute
    public String OrderItemImpl.getDescription() {
        String rawValue = bean.getDescription();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setDescription(String val) {
        String rawValue = val;
        bean.setDescription(SimpleFieldUtils.setSimple(this, bean, "description", rawValue, bean.getDescription()));
    }
    
    public Product OrderItemImpl.getStockItem() {
        return ReferenceFieldUtils.getReference(this, Product.class, bean.getStockItem());
    }
    
    public void OrderItemImpl.setStockItem(Product val) {
        bean.setStockItem(ReferenceFieldUtils.setReference(this, bean, bean.getStockItem(), val, false, "null"));
    }
    
    public String OrderItemImpl.getStockItemRef() {
        String rawValue = bean.getStockItem();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setStockItemRef(String val) {
        String rawValue = val;
        bean.setStockItem(SimpleFieldUtils.setSimple(this, bean, "StockItemRef", rawValue, bean.getStockItem()));
    }
    
    @XmlAttribute
    public double OrderItemImpl.getQuantity() {
        double rawValue = bean.getQuantity();
        double val = cloneValue(rawValue);
        return val;
    }
    
    public void OrderItemImpl.setQuantity(double val) {
        double rawValue = val;
        bean.setQuantity(SimpleFieldUtils.setSimple(this, bean, "quantity", rawValue, bean.getQuantity()));
    }
    
    @XmlAttribute
    public String OrderItemImpl.getMeasure() {
        String rawValue = bean.getMeasure();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setMeasure(String val) {
        String rawValue = val;
        bean.setMeasure(SimpleFieldUtils.setSimple(this, bean, "measure", rawValue, bean.getMeasure()));
    }
    
    @XmlAttribute
    public double OrderItemImpl.getPrice() {
        double rawValue = bean.getPrice();
        double val = cloneValue(rawValue);
        return val;
    }
    
    public void OrderItemImpl.setPrice(double val) {
        double rawValue = val;
        bean.setPrice(SimpleFieldUtils.setSimple(this, bean, "price", rawValue, bean.getPrice()));
    }
    
    @XmlAttribute
    public String OrderItemImpl.getCurrency() {
        String rawValue = bean.getCurrency();
        String val = rawValue;
        return val;
    }
    
    public void OrderItemImpl.setCurrency(String val) {
        String rawValue = val;
        bean.setCurrency(SimpleFieldUtils.setSimple(this, bean, "currency", rawValue, bean.getCurrency()));
    }
    
    public void OrderItemImpl.acceptReader(ObjexStateReader reader) {
        String ref = bean.getId();
        String new_ref = reader.read("ref", ref, java.lang.String.class, ObjexFieldType.STRING, true);
        if( new_ref != ref ) setId(new_ref);
        String name = bean.getName();
        String new_name = reader.read("name", name, java.lang.String.class, ObjexFieldType.STRING, true);
        if( new_name != name ) setName(new_name);
        String description = bean.getDescription();
        String new_description = reader.read("description", description, java.lang.String.class, ObjexFieldType.STRING, true);
        if( new_description != description ) setDescription(new_description);
        String stockItem = bean.getStockItem();
        String new_stockItem = reader.readReference("stockItem", stockItem, ObjexFieldType.REFERENCE, true);
        if( new_stockItem != stockItem ) setStockItemRef(new_stockItem);
        Double quantity = bean.getQuantity();
        Double new_quantity = reader.read("quantity", quantity, double.class, ObjexFieldType.NUMBER, true);
        if( new_quantity != quantity ) setQuantity(new_quantity);
        String measure = bean.getMeasure();
        String new_measure = reader.read("measure", measure, java.lang.String.class, ObjexFieldType.STRING, true);
        if( new_measure != measure ) setMeasure(new_measure);
        Double price = bean.getPrice();
        Double new_price = reader.read("price", price, double.class, ObjexFieldType.NUMBER, true);
        if( new_price != price ) setPrice(new_price);
        String currency = bean.getCurrency();
        String new_currency = reader.read("currency", currency, java.lang.String.class, ObjexFieldType.STRING, true);
        if( new_currency != currency ) setCurrency(new_currency);
    }
    
}
