package org.talframework.objexj.sample.beans.order;

import java.lang.String;
import java.util.List;

privileged aspect OrderBean_Roo_JavaBean {
    
    public long OrderBean.getAccount() {
        return this.account;
    }
    
    public void OrderBean.setAccount(long account) {
        this.account = account;
    }
    
    public List<String> OrderBean.getItems() {
        return this.items;
    }
    
    public void OrderBean.setItems(List<String> items) {
        this.items = items;
    }
    
    public String OrderBean.getTest() {
        return this.test;
    }
    
    public void OrderBean.setTest(String test) {
        this.test = test;
    }
    
}
