package org.tpspencer.tal.objexj.sample.beans.stock;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect ProductBean_Roo_ObjexStateBean {
    
    declare parents: ProductBean implements ObjexObjStateBean;
    
    declare @type: ProductBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String ProductBean.id;
    
    private String ProductBean.parentId;
    
    @NotPersistent
    private transient boolean ProductBean._editable;
    
    public ProductBean.new() {
        super();
        _editable = false;
    }

    public String ProductBean.getId() {
        return this.id;
    }
    
    public String ProductBean.getParentId() {
        return this.parentId;
    }
    
    public boolean ProductBean.isEditable() {
        return _editable;
    }
    
    public void ProductBean.setEditable() {
        _editable = true;
    }
    
    public String ProductBean.getObjexObjType() {
        return "Product";
    }
    
    public void ProductBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void ProductBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void ProductBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
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
    
}
