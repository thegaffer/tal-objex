package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.object.StateBeanUtils;
import org.tpspencer.tal.objexj.sample.api.order.OrderItem;
import org.tpspencer.tal.objexj.sample.beans.order.OrderBean;

privileged aspect OrderImpl_Roo_ObjexObj {
    
    declare parents: OrderImpl extends BaseObjexObj;
    
    public OrderBean OrderImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void OrderImpl.validate(ValidationRequest request) {
        return;
    }
    
    public long OrderImpl.getAccount() {
        return bean.getAccount();
    }
    
    public void OrderImpl.setAccount(long val) {
        if( !StateBeanUtils.hasChanged(bean.getAccount(), val) ) return;
        ensureUpdateable(bean);
        bean.setAccount(val);
    }
    
    public java.util.List<OrderItem> OrderImpl.getItems() {
        return getContainer().getObjectList(bean.getItems(), OrderItem.class);
    }
    
    public OrderItem OrderImpl.getItemById(Object id) {
        ObjectUtils.testObjectHeld(bean.getItems(), id);
        return ObjectUtils.getObject(this, id, org.tpspencer.tal.objexj.sample.api.order.OrderItem.class);
    }
    
    public List<String> OrderImpl.getItemRefs() {
        return cloneValue(bean.getItems());
    }
    
    public OrderItem OrderImpl.createItem() {
        checkUpdateable();
        ObjexObj val = ObjectUtils.createObject(this, bean, "OrderItem");
        ensureUpdateable(bean);
        List<String> refs = bean.getItems();
        if( refs == null ) {
        	refs = new ArrayList<String>();
        	bean.setItems(refs);
        }
        refs.add(val.getId().toString());
        return val.getBehaviour(OrderItem.class);
    }
    
    public void OrderImpl.removeItemById(Object id) {
        checkUpdateable();
        String ref = ObjectUtils.getObjectRef(this, id);
        List<String> refs = bean.getItems();
        if( refs == null || refs.size() == 0 ) return;
        int size = refs.size();
        Iterator<String> it = refs.iterator();
        while( it.hasNext() ) {
        	if( ref.equals(it.next()) ) {
        		ensureUpdateable(bean);
        		it.remove();
        	}
        }
        if( refs.size() == size ) return;
        ObjectUtils.removeObject(this, bean, ref);
    }
    
    public void OrderImpl.removeItem(int index) {
        checkUpdateable();
        List<String> refs = bean.getItems();
        if( refs == null || index < 0 || index >= refs.size() ) return;
        String ref = refs.get(index);
        ensureUpdateable(bean);
        refs.remove(index);
        ObjectUtils.removeObject(this, bean, ref);
    }
    
    public void OrderImpl.removeItems() {
        List<String> refs = bean.getItems();
        if( refs == null || refs.size() == 0 ) return;
        checkUpdateable();
        Iterator<String> it = refs.iterator();
        while( it.hasNext() ) {
        	String ref = it.next();
        	ObjectUtils.removeObject(this, bean, ref);
        	it.remove();
        }
        ensureUpdateable(bean);
        bean.setItems(null);
    }
    
    public OrderItem OrderImpl.getTest() {
        return ObjectUtils.getObject(this, bean.getTest(), OrderItem.class);
    }
    
    public String OrderImpl.getTestRef() {
        return bean.getTest();
    }
    
    public OrderItem OrderImpl.createTest(String type) {
        ensureUpdateable(bean);
        if( bean.getTest() != null )
        	ObjectUtils.removeObject(this, bean, bean.getTest());
        ObjexObj val = ObjectUtils.createObject(this, bean, type);
        bean.setTest(val.getId().toString());
        return val.getBehaviour(OrderItem.class);
    }
    
    public void OrderImpl.removeTest() {
        ensureUpdateable(bean);
        if( bean.getTest() != null )
        	ObjectUtils.removeObject(this, bean, bean.getTest());
    }
    
}
