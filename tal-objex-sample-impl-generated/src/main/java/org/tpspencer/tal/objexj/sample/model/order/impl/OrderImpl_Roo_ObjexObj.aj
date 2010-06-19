package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

privileged aspect OrderImpl_Roo_ObjexObj {
    
    declare parents: OrderImpl extends BaseObjexObj;
    
    public OrderBean OrderImpl.getLocalState() {
        return bean;
    }
    
    public OrderBean OrderImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new OrderBean(bean);
    }
    
    public long OrderImpl.getAccount() {
        return bean.getAccount();
    }
    
    public void OrderImpl.setAccount(long val) {
        checkUpdateable();
        bean.setAccount(val);
    }
    
    public java.util.List<OrderItem> OrderImpl.getItems() {
        return ObjectUtils.getObjectList(this, bean.getItems(), OrderItem.class);
    }
    
    public OrderItem OrderImpl.getItemById(Object id) {
        ObjectUtils.testObjectHeld(bean.getItems(), id);
        return ObjectUtils.getObject(this, id, org.tpspencer.tal.objexj.sample.api.order.OrderItem.class);
    }
    
    public List<String> OrderImpl.getItemRefs() {
        return bean.getItems();
    }
    
    public OrderItem OrderImpl.createItem() {
        checkUpdateable();
        ObjexObj val = ObjectUtils.createObject(this, "OrderItemBean");
        if( bean.getItems() == null ) bean.setItems(new ArrayList<String>());
        bean.getItems().add(val.getId().toString());
        return val.getBehaviour(OrderItem.class);
    }
    
    public void OrderImpl.removeItemById(Object id) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(this, id);
        Iterator<String> it = bean.getItems().iterator();
        while( it.hasNext() ) {
        	if( ref.equals(it.next()) ) it.remove();
        }
        ObjectUtils.removeObject(this, ref);
    }
    
    public void OrderImpl.removeItem(int index) {
        checkUpdateable();
        List<String> refs = bean.getItems();
        if( refs != null && index >= 0 && index < refs.size() ) {
        	String ref = refs.get(index);
        	ObjectUtils.removeObject(this, ref);
        	refs.remove(index);
        }
    }
    
    public void OrderImpl.removeItems() {
        checkUpdateable();
        Iterator<String> it = bean.getItems().iterator();
        while( it.hasNext() ) {
        	String ref = it.next();
        	ObjectUtils.removeObject(this, ref);
        	it.remove();
        }
        bean.setItems(null);
    }
    
    public OrderItem OrderImpl.getTest() {
        return ObjectUtils.getObject(this, bean.getTest(), OrderItem.class);
    }
    
    public String OrderImpl.getTestRef() {
        return bean.getTest();
    }
    
    public OrderItem OrderImpl.createTest(String type) {
        checkUpdateable();
        if( bean.getTest() != null )
        	ObjectUtils.removeObject(this, bean.getTest());
        ObjexObj val = ObjectUtils.createObject(this, type);
        bean.setTest(val.getId().toString());
        return val.getBehaviour(OrderItem.class);
    }
    
    public void OrderImpl.removeTest() {
        checkUpdateable();
        if( bean.getTest() != null )
        	ObjectUtils.removeObject(this, bean.getTest());
    }
    
}
