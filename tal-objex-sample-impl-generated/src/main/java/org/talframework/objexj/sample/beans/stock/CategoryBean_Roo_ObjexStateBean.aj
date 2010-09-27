// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.beans.stock;

import java.io.Writer;
import java.lang.Object;
import java.lang.String;
import java.util.Iterator;
import java.util.List;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.ObjectUtils;

privileged aspect CategoryBean_Roo_ObjexStateBean {
    
    declare parents: CategoryBean implements ObjexObjStateBean;
    
    declare @type: CategoryBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String CategoryBean.id;
    
    @Persistent(column = "parentId")
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
    
    public String CategoryBean.getName() {
        return name;
    }
    
    public void CategoryBean.setName(String val) {
        name = val;
    }
    
    public String CategoryBean.getDescription() {
        return description;
    }
    
    public void CategoryBean.setDescription(String val) {
        description = val;
    }
    
    public List<String> CategoryBean.getProducts() {
        return products;
    }
    
    public void CategoryBean.setProducts(List<String> val) {
        products = val;
    }
    
    public List<String> CategoryBean.getCategories() {
        return categories;
    }
    
    public void CategoryBean.setCategories(List<String> val) {
        categories = val;
    }
    
    public void CategoryBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        products = ObjectUtils.updateTempReferences(products, refs);
        categories = ObjectUtils.updateTempReferences(categories, refs);
    }
    
    public void CategoryBean.writeBean(Writer writer, ObjexID id, String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(".id=").append(id.toString()).append('\n');
        if( parentId != null ) builder.append(prefix).append(".parentId=").append(parentId).append('\n');
        if( name != null ) builder.append(prefix).append(".name=").append(name).append('\n');
        if( description != null ) builder.append(prefix).append(".description=").append(description).append('\n');
        if( products != null ) {
        	Iterator<String> it = products.iterator();
        	int index = 0;
        	while( it.hasNext() ) {
        		builder.append(prefix).append(".products[index]=").append(it.next()).append('\n');
        		index++;
        	}
        }
        if( categories != null ) {
        	Iterator<String> it = categories.iterator();
        	int index = 0;
        	while( it.hasNext() ) {
        		builder.append(prefix).append(".categories[index]=").append(it.next()).append('\n');
        		index++;
        	}
        }
    }
    
}
