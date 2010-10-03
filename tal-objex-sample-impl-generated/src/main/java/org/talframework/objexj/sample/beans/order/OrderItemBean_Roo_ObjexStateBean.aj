// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.talframework.objexj.sample.beans.order;

import java.io.Writer;
import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.StateBeanUtils;

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
    
    public String OrderItemBean.getRef() {
        return ref;
    }
    
    public void OrderItemBean.setRef(String val) {
        ref = val;
    }
    
    public String OrderItemBean.getName() {
        return name;
    }
    
    public void OrderItemBean.setName(String val) {
        name = val;
    }
    
    public String OrderItemBean.getDescription() {
        return description;
    }
    
    public void OrderItemBean.setDescription(String val) {
        description = val;
    }
    
    public String OrderItemBean.getStockItem() {
        return stockItem;
    }
    
    public void OrderItemBean.setStockItem(String val) {
        stockItem = val;
    }
    
    public double OrderItemBean.getQuantity() {
        return quantity;
    }
    
    public void OrderItemBean.setQuantity(double val) {
        quantity = val;
    }
    
    public String OrderItemBean.getMeasure() {
        return measure;
    }
    
    public void OrderItemBean.setMeasure(String val) {
        measure = val;
    }
    
    public double OrderItemBean.getPrice() {
        return price;
    }
    
    public void OrderItemBean.setPrice(double val) {
        price = val;
    }
    
    public String OrderItemBean.getCurrency() {
        return currency;
    }
    
    public void OrderItemBean.setCurrency(String val) {
        currency = val;
    }
    
    public void OrderItemBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = StateBeanUtils.updateTempReferences(parentId, refs);
        stockItem = StateBeanUtils.updateTempReferences(stockItem, refs);
    }
    
    public void OrderItemBean.writeBean(Writer writer, ObjexID id, String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(".id=").append(id.toString()).append('\n');
        if( parentId != null ) builder.append(prefix).append(".parentId=").append(parentId).append('\n');
        if( ref != null ) builder.append(prefix).append(".ref=").append(ref).append('\n');
        if( name != null ) builder.append(prefix).append(".name=").append(name).append('\n');
        if( description != null ) builder.append(prefix).append(".description=").append(description).append('\n');
        if( stockItem != null ) builder.append(prefix).append(".stockItem=").append(stockItem).append('\n');
        builder.append(prefix).append(".quantity=").append(quantity).append('\n');
        if( measure != null ) builder.append(prefix).append(".measure=").append(measure).append('\n');
        builder.append(prefix).append(".price=").append(price).append('\n');
        if( currency != null ) builder.append(prefix).append(".currency=").append(currency).append('\n');
    }
    
}
