package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.CategoryState;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

public class CategoryImpl extends SimpleObjexObj implements Category {

    public CategoryImpl(ObjectStrategy strategy, Container container, ObjexID id, ObjexID parent, Object state) {
        super(strategy, container, id, parent, state);
    }
    
    public CategoryState getCategoryState() {
        return this;
    }
    
    public void setCategoryState(CategoryState category) {
        checkUpdateable(true);
        
        // TODO: Update the fields that have changed!?!
    }
    
    public void setId(Object id) {
        throw new IllegalStateException("Cannot set the ID on an object that already has an ID!");
    }
    
    public void setParentId(Object id) {
        checkUpdateable(true);
        // TODO: Not sure here!?!
    }
    
    public String getName() {
        return getLocalState(CategoryBean.class).getName();
    }
    
    public void setName(String name) {
        checkUpdateable(true);
        getLocalState(CategoryBean.class).setName(name);
    }
    
    public String getDescription() {
        return getLocalState(CategoryBean.class).getDescription();
    }
    
    public void setDescription(String description) {
        checkUpdateable(true);
        getLocalState(CategoryBean.class).setDescription(description);
    }
    
    public String[] getCategories() {
        return getLocalState(CategoryBean.class).getCategories();
    }
    
    public List<Category> getCategoryList() {
        List<Category> categories = null;
        String[] cats = getCategories();
        if( cats != null && cats.length > 0 ) {
            for( int i = 0 ; i < cats.length ; i++ ) {
                Category c = getContainer().getObject(cats[i], Category.class);
                if( c != null ) {
                    if( categories == null ) categories = new ArrayList<Category>();
                    categories.add(c);
                }
            }
        }
        return categories;
    }
    
    public void setCategories(String[] categories) {
        checkUpdateable(true);
        getLocalState(CategoryBean.class).setCategories(categories);
    }
    
    public String[] getProducts() {
        return getLocalState(CategoryBean.class).getProducts();
    }
    
    public List<Product> getProductList() {
        List<Product> products = null;
        String[] prods = getProducts();
        if( prods != null && prods.length > 0 ) {
            for( int i = 0 ; i < prods.length ; i++ ) {
                Product p = getContainer().getObject(prods[i], Product.class);
                if( p != null ) {
                    if( products == null ) products = new ArrayList<Product>();
                    products.add(p);
                }
            }
        }
        
        return products;
    }
    
    public void setProducts(String[] products) {
        checkUpdateable(true);
        getLocalState(CategoryBean.class).setProducts(products);
    }
    
    public Category createNewCategory() {
        checkUpdateable(true);
        
        ObjexObj newCat = ((EditableContainer)getContainer()).newObject("Category", getId());
        String[] categories = getCategories();
        int ln = categories != null ? categories.length : 0;
        if( categories == null ) categories = new String[1];
        else {
            String[] cats = new String[ln + 1];
            System.arraycopy(categories, 0, cats, 0, ln);
            categories = cats;
        }
        categories[ln] = newCat.getId().toString();
        setCategories(categories);
        
        return newCat.getBehaviour(Category.class);
    }
    
    public Product createNewProduct() {
        checkUpdateable(true);
        
        ObjexObj newProduct = ((EditableContainer)getContainer()).newObject("Product", getId());
        String[] products = getProducts();
        int ln = products != null ? products.length : 0;
        if( products == null ) products = new String[1];
        else {
            String[] prods = new String[ln + 1];
            System.arraycopy(products, 0, prods, 0, ln);
            products = prods;
        }
        products[ln] = newProduct.getId().toString();
        setProducts(products);
        
        return newProduct.getBehaviour(Product.class);
    }
}
