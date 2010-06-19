package org.tpspencer.tal.objexj.sample.beans.order;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.sample.beans.order.OrderItemBean;

privileged aspect OrderItemBean_Roo_ObjexStateBean {
    
    declare parents: OrderItemBean implements ObjexObjStateBean;
    
    declare @type: OrderItemBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderItemBean.id;
    
    private String OrderItemBean.parentId;
    
    public OrderItemBean.new() {
        // Nothing
    }

    public OrderItemBean.new(OrderItemBean src) {
        this.ref = src.ref;
        this.name = src.name;
        this.description = src.description;
        this.stockItem = src.stockItem;
        this.quantity = src.quantity;
        this.measure = src.measure;
        this.price = src.price;
        this.currency = src.currency;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public OrderItemBean.new(Object id, Object parentId) {
        this.id = id != null ? id.toString() : null;
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String OrderItemBean.getId() {
        return this.id;
    }
    
    public String OrderItemBean.getParentId() {
        return this.parentId;
    }
    
    public String OrderItemBean.getRef() {
        return this.ref;
    }
    
    public void OrderItemBean.setRef(String val) {
        this.ref = val;
    }
    
    public String OrderItemBean.getName() {
        return this.name;
    }
    
    public void OrderItemBean.setName(String val) {
        this.name = val;
    }
    
    public String OrderItemBean.getDescription() {
        return this.description;
    }
    
    public void OrderItemBean.setDescription(String val) {
        this.description = val;
    }
    
    public String OrderItemBean.getStockItem() {
        return this.stockItem;
    }
    
    public void OrderItemBean.setStockItem(String val) {
        this.stockItem = val;
    }
    
    public double OrderItemBean.getQuantity() {
        return this.quantity;
    }
    
    public void OrderItemBean.setQuantity(double val) {
        this.quantity = val;
    }
    
    public String OrderItemBean.getMeasure() {
        return this.measure;
    }
    
    public void OrderItemBean.setMeasure(String val) {
        this.measure = val;
    }
    
    public double OrderItemBean.getPrice() {
        return this.price;
    }
    
    public void OrderItemBean.setPrice(double val) {
        this.price = val;
    }
    
    public String OrderItemBean.getCurrency() {
        return this.currency;
    }
    
    public void OrderItemBean.setCurrency(String val) {
        this.currency = val;
    }
    
    public String OrderItemBean.getObjexObjType() {
        return "OrderItem";
    }
    
}
