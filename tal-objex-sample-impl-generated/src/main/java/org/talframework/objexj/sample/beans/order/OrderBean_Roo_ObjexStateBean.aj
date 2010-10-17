// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.beans.order;

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
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.StateBeanUtils;

privileged aspect OrderBean_Roo_ObjexStateBean {
    
    declare parents: OrderBean implements ObjexObjStateBean;
    
    declare @type: OrderBean: @PersistenceCapable;
    
    declare @type: OrderBean: @XmlRootElement;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderBean.id;
    
    @Persistent(column = "parentId")
    private String OrderBean.parentId;
    
    @NotPersistent
    private transient boolean OrderBean._editable;
    
    public OrderBean.new() {
        super();
        _editable = false;
    }

    @XmlAttribute
    @XmlID
    public String OrderBean.getId() {
        return this.id;
    }
    
    public void OrderBean.setId(String val) {
        if( this.id != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.id = val;
    }
    
    @XmlAttribute
    public String OrderBean.getParentId() {
        return this.parentId;
    }
    
    public void OrderBean.setParentId(String val) {
        if( this.parentId != null ) throw new IllegalArgumentException("You cannot set a parent ID on an object once it is set");
        this.parentId = val;
    }
    
    @XmlTransient
    public boolean OrderBean.isEditable() {
        return _editable;
    }
    
    public void OrderBean.setEditable() {
        _editable = true;
    }
    
    @XmlTransient
    public String OrderBean.getObjexObjType() {
        return "Order";
    }
    
    public void OrderBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void OrderBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public ObjexObjStateBean OrderBean.cloneState() {
        OrderBean ret = new OrderBean();
        ret.account = this.account;
        ret.items = this.items;
        ret.test = this.test;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
    @XmlAttribute
    public long OrderBean.getAccount() {
        return account;
    }
    
    public void OrderBean.setAccount(long val) {
        account = val;
    }
    
    @XmlList
    public List<String> OrderBean.getItems() {
        return items;
    }
    
    public void OrderBean.setItems(List<String> val) {
        items = val;
    }
    
    @XmlAttribute
    public String OrderBean.getTest() {
        return test;
    }
    
    public void OrderBean.setTest(String val) {
        test = val;
    }
    
    public void OrderBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = StateBeanUtils.updateTempReferences(parentId, refs);
        items = StateBeanUtils.updateTempReferences(items, refs);
        test = StateBeanUtils.updateTempReferences(test, refs);
    }
    
    public void OrderBean.acceptReader(ObjexStateReader reader) {
        account = reader.read("account", long.class, ObjexFieldType.NUMBER, true);
        items = reader.readReferenceList("items", ObjexFieldType.OWNED_REFERENCE, true);
        test = reader.readReference("test", ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    public void OrderBean.acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("account", account, ObjexFieldType.NUMBER, true);
        writer.writeReferenceList("items", items, ObjexFieldType.OWNED_REFERENCE, true);
        writer.writeReference("test", test, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
}
