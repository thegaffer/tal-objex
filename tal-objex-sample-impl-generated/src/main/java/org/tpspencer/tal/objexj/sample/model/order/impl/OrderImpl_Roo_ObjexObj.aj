package org.tpspencer.tal.objexj.sample.model.order.impl;

import java.lang.String;
import java.util.List;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
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
    
    public String[] OrderImpl.getItems() {
        return bean.getItems();
    }
    
    public void OrderImpl.setItems(String[] val) {
        checkUpdateable();
        bean.setItems(val);
    }
    
    public String OrderImpl.getTest() {
        return bean.getTest();
    }
    
    public void OrderImpl.setTest(String val) {
        checkUpdateable();
        bean.setTest(val);
    }
    
    public List<String> OrderImpl.getListRefs() {
        return bean.getListRefs();
    }
    
    public void OrderImpl.setListRefs(List<String> val) {
        checkUpdateable();
        bean.setListRefs(val);
    }
    
}
