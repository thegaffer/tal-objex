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

package org.talframework.objexj.sample.beans.stock;

import java.lang.Object;
import java.lang.String;
import java.util.Date;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.utils.StateBeanUtils;
import org.talframework.util.beans.BeanComparison;

privileged aspect ProductBean_Roo_ObjexStateBean {
    
    declare parents: Product implements ObjexObjStateBean;
    
    declare @type: Product: @PersistenceCapable;
    
    declare @type: Product: @XmlRootElement;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String ProductBean.id;
    
    @Persistent(column = "parentId")
    private String ProductBean.parentId;
    
    @NotPersistent
    private transient boolean ProductBean._editable;
    
    @NotPersistent
    private transient int ProductBean.setFields;
    
    @NotPersistent
    private transient int ProductBean.changedFields;
    
    public ProductBean.new() {
        super();
        _editable = false;
    }

    @XmlAttribute
    @XmlID
    public String ProductBean.getId() {
        return this.id;
    }
    
    public void ProductBean.setId(String val) {
        if( this.id != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.id = val;
    }
    
    @XmlAttribute
    public String ProductBean.getParentId() {
        return this.parentId;
    }
    
    public void ProductBean.setParentId(String val) {
        if( this.parentId != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.parentId = val;
    }
    
    @XmlTransient
    public boolean ProductBean.isEditable() {
        return _editable;
    }
    
    public void ProductBean.setEditable() {
        _editable = true;
    }
    
    @XmlTransient
    public String ProductBean.getObjexObjType() {
        return "Product";
    }
    
    public void ProductBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void ProductBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public ObjexObjStateBean ProductBean.cloneState() {
        ProductBean ret = new ProductBean();
        ret.name = this.name;
        ret.description = this.description;
        ret.effectiveFrom = this.effectiveFrom;
        ret.effectiveTo = this.effectiveTo;
        ret.price = this.price;
        ret.currency = this.currency;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
    public String ProductBean.toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProductBean: { ");
        builder.append("id=").append(getId());
        builder.append("parentId=").append(getParentId());
        builder.append("name=").append(name);
        builder.append("description=").append(description);
        builder.append("effectiveFrom=").append(effectiveFrom);
        builder.append("effectiveTo=").append(effectiveTo);
        builder.append("price=").append(price);
        builder.append("currency=").append(currency);
        return builder.append(" }").toString();
    }
    
    public int ProductBean.hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((effectiveFrom == null) ? 0 : effectiveFrom.hashCode());
        result = prime * result + ((effectiveTo == null) ? 0 : effectiveTo.hashCode());
        long price_temp = Double.doubleToRawLongBits(price);
        result = prime * result + (int)(price_temp ^ (price_temp >>> 32));
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        return result;
    }
    
    public boolean ProductBean.equals(Object obj) {
        ProductBean other = BeanComparison.basic(this, obj);
        boolean same = other != null;
        if( same ) {
        	same = BeanComparison.equals(same, getId(), other.getId());
        	same = BeanComparison.equals(same, getParentId(), other.getParentId());
        	same = BeanComparison.equals(same, name, other.name);
        	same = BeanComparison.equals(same, description, other.description);
        	same = BeanComparison.equals(same, effectiveFrom, other.effectiveFrom);
        	same = BeanComparison.equals(same, effectiveTo, other.effectiveTo);
        	same = BeanComparison.equals(same, price, other.price);
        	same = BeanComparison.equals(same, currency, other.currency);
        }
        return same;
    }
    
    public String ProductBean.getName() {
        return name;
    }
    
    public void ProductBean.setName(String val) {
        setFields |= 1;
        name = val;
    }
    
    public boolean ProductBean.isNameSet() {
        return (setFields & 1) > 0;
    }
    
    public boolean ProductBean.isNameChanged() {
        return (changedFields & 1) > 0;
    }
    
    public String ProductBean.getDescription() {
        return description;
    }
    
    public void ProductBean.setDescription(String val) {
        setFields |= 2;
        description = val;
    }
    
    public boolean ProductBean.isDescriptionSet() {
        return (setFields & 2) > 0;
    }
    
    public boolean ProductBean.isDescriptionChanged() {
        return (changedFields & 2) > 0;
    }
    
    public Date ProductBean.getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void ProductBean.setEffectiveFrom(Date val) {
        setFields |= 4;
        effectiveFrom = val;
    }
    
    public boolean ProductBean.isEffectiveFromSet() {
        return (setFields & 4) > 0;
    }
    
    public boolean ProductBean.isEffectiveFromChanged() {
        return (changedFields & 4) > 0;
    }
    
    public Date ProductBean.getEffectiveTo() {
        return effectiveTo;
    }
    
    public void ProductBean.setEffectiveTo(Date val) {
        setFields |= 8;
        effectiveTo = val;
    }
    
    public boolean ProductBean.isEffectiveToSet() {
        return (setFields & 8) > 0;
    }
    
    public boolean ProductBean.isEffectiveToChanged() {
        return (changedFields & 8) > 0;
    }
    
    public double ProductBean.getPrice() {
        return price;
    }
    
    public void ProductBean.setPrice(double val) {
        setFields |= 16;
        price = val;
    }
    
    public boolean ProductBean.isPriceSet() {
        return (setFields & 16) > 0;
    }
    
    public boolean ProductBean.isPriceChanged() {
        return (changedFields & 16) > 0;
    }
    
    public String ProductBean.getCurrency() {
        return currency;
    }
    
    public void ProductBean.setCurrency(String val) {
        setFields |= 32;
        currency = val;
    }
    
    public boolean ProductBean.isCurrencySet() {
        return (setFields & 32) > 0;
    }
    
    public boolean ProductBean.isCurrencyChanged() {
        return (changedFields & 32) > 0;
    }
    
    public void ProductBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = StateBeanUtils.updateTempReferences(parentId, refs);
    }
    
    public void ProductBean.acceptReader(ObjexStateReader reader) {
        name = reader.read("name", name, java.lang.String.class, ObjexFieldType.STRING, true);
        description = reader.read("description", description, java.lang.String.class, ObjexFieldType.STRING, true);
        effectiveFrom = reader.read("effectiveFrom", effectiveFrom, java.util.Date.class, ObjexFieldType.DATE, true);
        effectiveTo = reader.read("effectiveTo", effectiveTo, java.util.Date.class, ObjexFieldType.DATE, true);
        price = reader.read("price", price, double.class, ObjexFieldType.NUMBER, true);
        currency = reader.read("currency", currency, java.lang.String.class, ObjexFieldType.STRING, true);
    }
    
    public void ProductBean.acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.STRING, true);
        writer.write("description", description, ObjexFieldType.STRING, true);
        writer.write("effectiveFrom", effectiveFrom, ObjexFieldType.DATE, true);
        writer.write("effectiveTo", effectiveTo, ObjexFieldType.DATE, true);
        writer.write("price", price, ObjexFieldType.NUMBER, true);
        writer.write("currency", currency, ObjexFieldType.STRING, true);
    }
    
}
