// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.beans.stock;

import java.lang.Object;
import java.lang.String;
import java.util.List;
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

privileged aspect CategoryBean_Roo_ObjexStateBean {
    
    declare parents: CategoryBean implements ObjexObjStateBean;
    
    declare @type: CategoryBean: @PersistenceCapable;
    
    declare @type: CategoryBean: @XmlRootElement;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String CategoryBean.id;
    
    @Persistent(column = "parentId")
    private String CategoryBean.parentId;
    
    @NotPersistent
    private transient boolean CategoryBean._editable;
    
    @NotPersistent
    private transient int CategoryBean.setFields;
    
    @NotPersistent
    private transient int CategoryBean.changedFields;
    
    public CategoryBean.new() {
        super();
        _editable = false;
    }

    @XmlAttribute
    @XmlID
    public String CategoryBean.getId() {
        return this.id;
    }
    
    public void CategoryBean.setId(String val) {
        if( this.id != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.id = val;
    }
    
    @XmlAttribute
    public String CategoryBean.getParentId() {
        return this.parentId;
    }
    
    public void CategoryBean.setParentId(String val) {
        if( this.parentId != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.parentId = val;
    }
    
    @XmlTransient
    public boolean CategoryBean.isEditable() {
        return _editable;
    }
    
    public void CategoryBean.setEditable() {
        _editable = true;
    }
    
    @XmlTransient
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
    
    public String CategoryBean.toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CategoryBean: { ");
        builder.append("id=").append(getId());
        builder.append("parentId=").append(getParentId());
        builder.append("name=").append(name);
        builder.append("description=").append(description);
        builder.append("products=").append(products);
        builder.append("categories=").append(categories);
        return builder.append(" }").toString();
    }
    
    public int CategoryBean.hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((products == null) ? 0 : products.hashCode());
        result = prime * result + ((categories == null) ? 0 : categories.hashCode());
        return result;
    }
    
    public boolean CategoryBean.equals(Object obj) {
        CategoryBean other = BeanComparison.basic(CategoryBean.class, this, obj);
        boolean same = other != null;
        if( same ) {
        	same = BeanComparison.equals(same, getId(), other.getId());
        	same = BeanComparison.equals(same, getParentId(), other.getParentId());
        	same = BeanComparison.equals(same, name, other.name);
        	same = BeanComparison.equals(same, description, other.description);
        	same = BeanComparison.equals(same, products, other.products);
        	same = BeanComparison.equals(same, categories, other.categories);
        }
        return same;
    }
    
    public String CategoryBean.getName() {
        return name;
    }
    
    public void CategoryBean.setName(String val) {
        setFields |= 1;
        name = val;
    }
    
    public boolean CategoryBean.isNameSet() {
        return (setFields & 1) > 0;
    }
    
    public boolean CategoryBean.isNameChanged() {
        return (changedFields & 1) > 0;
    }
    
    public String CategoryBean.getDescription() {
        return description;
    }
    
    public void CategoryBean.setDescription(String val) {
        setFields |= 2;
        description = val;
    }
    
    public boolean CategoryBean.isDescriptionSet() {
        return (setFields & 2) > 0;
    }
    
    public boolean CategoryBean.isDescriptionChanged() {
        return (changedFields & 2) > 0;
    }
    
    public List<String> CategoryBean.getProducts() {
        return products;
    }
    
    public void CategoryBean.setProducts(List<String> val) {
        setFields |= 4;
        products = val;
    }
    
    public boolean CategoryBean.isProductsSet() {
        return (setFields & 4) > 0;
    }
    
    public boolean CategoryBean.isProductsChanged() {
        return (changedFields & 4) > 0;
    }
    
    public List<String> CategoryBean.getCategories() {
        return categories;
    }
    
    public void CategoryBean.setCategories(List<String> val) {
        setFields |= 8;
        categories = val;
    }
    
    public boolean CategoryBean.isCategoriesSet() {
        return (setFields & 8) > 0;
    }
    
    public boolean CategoryBean.isCategoriesChanged() {
        return (changedFields & 8) > 0;
    }
    
    public void CategoryBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = StateBeanUtils.updateTempReferences(parentId, refs);
        products = StateBeanUtils.updateTempReferences(products, refs);
        categories = StateBeanUtils.updateTempReferences(categories, refs);
    }
    
    public void CategoryBean.acceptReader(ObjexStateReader reader) {
        name = reader.read("name", name, java.lang.String.class, ObjexFieldType.STRING, true);
        description = reader.read("description", description, java.lang.String.class, ObjexFieldType.STRING, true);
        products = reader.readReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        categories = reader.readReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    public void CategoryBean.acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.STRING, true);
        writer.write("description", description, ObjexFieldType.STRING, true);
        writer.writeReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        writer.writeReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
}
