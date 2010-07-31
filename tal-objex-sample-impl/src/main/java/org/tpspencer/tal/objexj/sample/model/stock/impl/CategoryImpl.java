package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjectStrategy;
import org.tpspencer.tal.objexj.object.SimpleObjexObj;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.beans.stock.CategoryBean;

public class CategoryImpl extends SimpleObjexObj implements Category {
    
    public static final ObjectStrategy STRATEGY = new SimpleObjectStrategy("Category", CategoryImpl.class, CategoryBean.class);

    public CategoryImpl(ObjexObjStateBean state) {
        super(STRATEGY, state);
    }
    
    public String getParentCategoryId() {
        ObjexID parentId = getParentId();
        return parentId != null ? parentId.toString() : null;
    }

    public String getName() {
        return getLocalState(CategoryBean.class).getName();
    }
    
    public void setName(String name) {
        checkUpdateable();
        getLocalState(CategoryBean.class).setName(name);
    }
    
    public String getDescription() {
        return getLocalState(CategoryBean.class).getDescription();
    }
    
    public void setDescription(String description) {
        checkUpdateable();
        getLocalState(CategoryBean.class).setDescription(description);
    }
    
    public List<String> getCategoryRefs() {
        return getLocalState(CategoryBean.class).getCategories();
    }
    
    public List<Category> getCategories() {
        List<Category> categories = null;
        
        List<String> cats = getCategoryRefs();
        if( cats != null ) {
            Iterator<String> it = cats.iterator();
            while( it.hasNext() ) {
                Category c = getContainer().getObject(it.next(), Category.class);
                if( c != null ) {
                    if( categories == null ) categories = new ArrayList<Category>();
                    categories.add(c);
                }
            }
        }
        return categories;
    }
    
    public void setCategories(List<String> categories) {
        checkUpdateable();
        getLocalState(CategoryBean.class).setCategories(categories);
    }
    
    public List<String> getProductRefs() {
        return getLocalState(CategoryBean.class).getProducts();
    }
    
    public List<Product> getProducts() {
        List<Product> products = null;
        List<String> prods = getProductRefs();
        if( prods != null ) {
            Iterator<String> it = prods.iterator();
            while( it.hasNext() ) {
                Product p = getContainer().getObject(it.next(), Product.class);
                if( p != null ) {
                    if( products == null ) products = new ArrayList<Product>();
                    products.add(p);
                }
            }
        }
        
        return products;
    }
    
    public void setProducts(List<String> products) {
        checkUpdateable();
        getLocalState(CategoryBean.class).setProducts(products);
    }
    
    public Category createCategory() {
        checkUpdateable();
        
        ObjexObj newCat = ((EditableContainer)getContainer()).newObject("Category", this);
        
        List<String> cats = getCategoryRefs();
        if( cats == null ) cats = new ArrayList<String>();
        cats.add(newCat.getId().toString());
        getLocalState(CategoryBean.class).setCategories(cats);
        
        return newCat.getBehaviour(Category.class);
    }
    
    public Product createProduct() {
        checkUpdateable();
        
        ObjexObj newProduct = ((EditableContainer)getContainer()).newObject("Product", this);
        
        List<String> prods = getProductRefs();
        if( prods == null ) prods = new ArrayList<String>();
        prods.add(newProduct.getId().toString());
        getLocalState(CategoryBean.class).setProducts(prods);
        
        return newProduct.getBehaviour(Product.class);
    }
}
