package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.List;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

public class StockRepositoryImpl implements StockRepository {
    
private Container container;
    
    public StockRepositoryImpl(Container container) {
        this.container = container;
    }
    
    public Category createNewCategory(Object parentCategoryId) {
        Category cat = findCategory(parentCategoryId != null ? parentCategoryId.toString() : null);
        if( cat == null ) throw new IllegalArgumentException("Cannot create category if not parent!");
        
        return cat.createCategory();
    }
    
    public Product createNewProduct(Object parentCategoryId) {
        Category cat = findCategory(parentCategoryId != null ? parentCategoryId.toString() : null);
        if( cat == null ) throw new IllegalArgumentException("Cannot create category if not parent!");
        
        return cat.createProduct();
    }
    
    public Category findCategory(String id) {
        return id != null ? container.getObject(id, Category.class) : container.getRootObject().getBehaviour(Category.class);
    }
    
    public Product findProduct(String id) {
        return container.getObject(id, Product.class);
    }
    
    public List<Category> getRootCategories() {
        ObjexObj obj = container.getRootObject();
        return obj.getBehaviour(Category.class).getCategories();
    }
    
    public List<Category> findCategoriesByName(String name) {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public List<Product> findExpiredProducts() {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public List<Product> findProductsByCategory(String categoryId) {
        Category cat = findCategory(categoryId);
        return cat != null ? cat.getProducts() : null;
    }
    
    public List<Product> findProductsByName(String name) {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public List<Product> findProductsByPrice(double priceFrom, double priceTo) {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public String suspend() {
        return container.suspend();
    }
    
    public void persist() {
        container.saveContainer();
    }
}
