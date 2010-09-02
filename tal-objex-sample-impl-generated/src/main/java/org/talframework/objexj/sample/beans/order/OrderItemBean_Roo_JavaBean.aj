package org.talframework.objexj.sample.beans.order;

import java.lang.String;

privileged aspect OrderItemBean_Roo_JavaBean {
    
    public String OrderItemBean.getRef() {
        return this.ref;
    }
    
    public void OrderItemBean.setRef(String ref) {
        this.ref = ref;
    }
    
    public String OrderItemBean.getName() {
        return this.name;
    }
    
    public void OrderItemBean.setName(String name) {
        this.name = name;
    }
    
    public String OrderItemBean.getDescription() {
        return this.description;
    }
    
    public void OrderItemBean.setDescription(String description) {
        this.description = description;
    }
    
    public String OrderItemBean.getStockItem() {
        return this.stockItem;
    }
    
    public void OrderItemBean.setStockItem(String stockItem) {
        this.stockItem = stockItem;
    }
    
    public double OrderItemBean.getQuantity() {
        return this.quantity;
    }
    
    public void OrderItemBean.setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public String OrderItemBean.getMeasure() {
        return this.measure;
    }
    
    public void OrderItemBean.setMeasure(String measure) {
        this.measure = measure;
    }
    
    public double OrderItemBean.getPrice() {
        return this.price;
    }
    
    public void OrderItemBean.setPrice(double price) {
        this.price = price;
    }
    
    public String OrderItemBean.getCurrency() {
        return this.currency;
    }
    
    public void OrderItemBean.setCurrency(String currency) {
        this.currency = currency;
    }
    
}
