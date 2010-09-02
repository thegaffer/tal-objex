package org.talframework.objexj.sample.beans.stock;

import java.lang.String;
import java.util.List;

privileged aspect CategoryBean_Roo_JavaBean {
    
    public String CategoryBean.getName() {
        return this.name;
    }
    
    public void CategoryBean.setName(String name) {
        this.name = name;
    }
    
    public String CategoryBean.getDescription() {
        return this.description;
    }
    
    public void CategoryBean.setDescription(String description) {
        this.description = description;
    }
    
    public List<String> CategoryBean.getProducts() {
        return this.products;
    }
    
    public void CategoryBean.setProducts(List<String> products) {
        this.products = products;
    }
    
    public List<String> CategoryBean.getCategories() {
        return this.categories;
    }
    
    public void CategoryBean.setCategories(List<String> categories) {
        this.categories = categories;
    }
    
}
