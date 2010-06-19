package org.tpspencer.tal.objexj.sample.beans.stock;

import java.lang.Object;
import java.lang.String;
import java.util.Date;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.sample.beans.stock.ProductBean;

privileged aspect ProductBean_Roo_ObjexStateBean {
    
    declare parents: ProductBean implements ObjexObjStateBean;
    
    declare @type: ProductBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String ProductBean.id;
    
    private String ProductBean.parentId;
    
    public ProductBean.new() {
        // Nothing
    }

    public ProductBean.new(ProductBean src) {
        this.name = src.name;
        this.description = src.description;
        this.effectiveFrom = src.effectiveFrom;
        this.effectiveTo = src.effectiveTo;
        this.price = src.price;
        this.currency = src.currency;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public ProductBean.new(Object id, Object parentId) {
        this.id = id != null ? id.toString() : null;
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String ProductBean.getId() {
        return this.id;
    }
    
    public String ProductBean.getParentId() {
        return this.parentId;
    }
    
    public String ProductBean.getName() {
        return this.name;
    }
    
    public void ProductBean.setName(String val) {
        this.name = val;
    }
    
    public String ProductBean.getDescription() {
        return this.description;
    }
    
    public void ProductBean.setDescription(String val) {
        this.description = val;
    }
    
    public Date ProductBean.getEffectiveFrom() {
        return this.effectiveFrom;
    }
    
    public void ProductBean.setEffectiveFrom(Date val) {
        this.effectiveFrom = val;
    }
    
    public Date ProductBean.getEffectiveTo() {
        return this.effectiveTo;
    }
    
    public void ProductBean.setEffectiveTo(Date val) {
        this.effectiveTo = val;
    }
    
    public double ProductBean.getPrice() {
        return this.price;
    }
    
    public void ProductBean.setPrice(double val) {
        this.price = val;
    }
    
    public String ProductBean.getCurrency() {
        return this.currency;
    }
    
    public void ProductBean.setCurrency(String val) {
        this.currency = val;
    }
    
    public String ProductBean.getObjexObjType() {
        return "Product";
    }
    
}
