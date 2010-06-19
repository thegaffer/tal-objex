package org.tpspencer.tal.objexj.sample.beans.order;

import java.lang.Object;
import java.lang.String;
import java.util.List;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

privileged aspect OrderBean_Roo_ObjexStateBean {
    
    declare parents: OrderBean implements ObjexObjStateBean;
    
    declare @type: OrderBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderBean.id;
    
    private String OrderBean.parentId;
    
    public OrderBean.new() {
        // Nothing
    }

    public OrderBean.new(OrderBean src) {
        this.account = src.account;
        this.items = src.items;
        this.test = src.test;
        this.listRefs = src.listRefs;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public OrderBean.new(Object id, Object parentId) {
        this.id = id != null ? id.toString() : null;
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String OrderBean.getId() {
        return this.id;
    }
    
    public String OrderBean.getParentId() {
        return this.parentId;
    }
    
    public long OrderBean.getAccount() {
        return this.account;
    }
    
    public void OrderBean.setAccount(long val) {
        this.account = val;
    }
    
    public String[] OrderBean.getItems() {
        return this.items;
    }
    
    public void OrderBean.setItems(String[] val) {
        this.items = val;
    }
    
    public String OrderBean.getTest() {
        return this.test;
    }
    
    public void OrderBean.setTest(String val) {
        this.test = val;
    }
    
    public List<String> OrderBean.getListRefs() {
        return this.listRefs;
    }
    
    public void OrderBean.setListRefs(List<String> val) {
        this.listRefs = val;
    }
    
    public String OrderBean.getObjexObjType() {
        return "Order";
    }
    
}
