package org.tpspencer.tal.objexj.sample.beans.order;

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

privileged aspect OrderItemBean_Roo_ObjexStateBean {
    
    declare parents: OrderItemBean implements ObjexObjStateBean;
    
    declare @type: OrderItemBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderItemBean.id;
    
    @Persistent(column = "parentId")
    private String OrderItemBean.parentId;
    
    @NotPersistent
    private transient boolean OrderItemBean._editable;
    
    public OrderItemBean.new() {
        super();
        _editable = false;
    }

    public String OrderItemBean.getId() {
        return this.id;
    }
    
    public String OrderItemBean.getParentId() {
        return this.parentId;
    }
    
    public boolean OrderItemBean.isEditable() {
        return _editable;
    }
    
    public void OrderItemBean.setEditable() {
        _editable = true;
    }
    
    public String OrderItemBean.getObjexObjType() {
        return "OrderItem";
    }
    
    public void OrderItemBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void OrderItemBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void OrderItemBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        stockItem = ObjectUtils.updateTempReferences(stockItem, refs);
    }
    
    public ObjexObjStateBean OrderItemBean.cloneState() {
        OrderItemBean ret = new OrderItemBean();
        ret.ref = this.ref;
        ret.name = this.name;
        ret.description = this.description;
        ret.stockItem = this.stockItem;
        ret.quantity = this.quantity;
        ret.measure = this.measure;
        ret.price = this.price;
        ret.currency = this.currency;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
