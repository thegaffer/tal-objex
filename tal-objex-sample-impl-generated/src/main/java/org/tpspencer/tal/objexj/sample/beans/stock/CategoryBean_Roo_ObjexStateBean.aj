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

privileged aspect CategoryBean_Roo_ObjexStateBean {
    
    declare parents: CategoryBean implements ObjexObjStateBean;
    
    declare @type: CategoryBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String CategoryBean.id;
    
    private String CategoryBean.parentId;
    
    @NotPersistent
    private transient boolean CategoryBean._editable;
    
    public CategoryBean.new() {
        super();
        _editable = false;
    }

    public String CategoryBean.getId() {
        return this.id;
    }
    
    public String CategoryBean.getParentId() {
        return this.parentId;
    }
    
    public boolean CategoryBean.isEditable() {
        return _editable;
    }
    
    public void CategoryBean.setEditable() {
        _editable = true;
    }
    
    public String CategoryBean.getObjexObjType() {
        return "Category";
    }
    
    public void CategoryBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void CategoryBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void CategoryBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        products = ObjectUtils.updateTempReferences(products, refs);
        categories = ObjectUtils.updateTempReferences(categories, refs);
    }
    
    public ObjexObjStateBean CategoryBean.cloneState() {
        CategoryBean ret = new CategoryBean();
        ret.name = this.name;
        ret.description = this.description;
        ret.products = this.products;
        ret.categories = this.categories;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
