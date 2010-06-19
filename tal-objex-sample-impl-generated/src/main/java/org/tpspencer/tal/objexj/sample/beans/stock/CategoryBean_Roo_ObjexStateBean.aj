package org.tpspencer.tal.objexj.sample.beans.stock;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

privileged aspect CategoryBean_Roo_ObjexStateBean {
    
    declare parents: CategoryBean implements ObjexObjStateBean;
    
    declare @type: CategoryBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String CategoryBean.id;
    
    private String CategoryBean.parentId;
    
    public CategoryBean.new() {
        super();
        // Nothing
    }

    public CategoryBean.new(CategoryBean src) {
        super();
        this.name = src.name;
        this.description = src.description;
        this.products = src.products;
        this.categories = src.categories;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public CategoryBean.new(Object id, Object parentId) {
        super();
        this.id = id != null ? id.toString() : null;
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String CategoryBean.getId() {
        return this.id;
    }
    
    public String CategoryBean.getParentId() {
        return this.parentId;
    }
    
    public String CategoryBean.getObjexObjType() {
        return "Category";
    }
    
}
