package org.tpspencer.tal.objexj.sample.beans.order;

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
        super();
        // Nothing
    }

    public OrderItemBean.new(OrderItemBean src) {
        super();
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

    public OrderItemBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String OrderItemBean.getId() {
        return this.id;
    }
    
    public String OrderItemBean.getParentId() {
        return this.parentId;
    }
    
    public String OrderItemBean.getObjexObjType() {
        return "OrderItem";
    }
    
    public void OrderItemBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void OrderItemBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        stockItem = ObjectUtils.updateTempReferences(stockItem, refs);
    }
    
}
