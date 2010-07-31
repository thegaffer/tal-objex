package org.tpspencer.tal.objexj.sample.beans.stock;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;
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
        super();
        // Nothing
    }

    public ProductBean.new(ProductBean src) {
        super();
        this.name = src.name;
        this.description = src.description;
        this.effectiveFrom = src.effectiveFrom;
        this.effectiveTo = src.effectiveTo;
        this.price = src.price;
        this.currency = src.currency;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public ProductBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String ProductBean.getId() {
        return this.id;
    }
    
    public String ProductBean.getParentId() {
        return this.parentId;
    }
    
    public String ProductBean.getObjexObjType() {
        return "Product";
    }
    
    public void ProductBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void ProductBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
}
