package org.tpspencer.tal.objexj.sample.model.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.sample.api.repository.StockRepository;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

public class StockRepositoryImpl implements StockRepository {
    
    private final Container container;
    private EditableContainer editableContainer;
    
    public StockRepositoryImpl(Container container) {
        this.container = container;
        this.editableContainer = null;
    }
    
    public StockRepositoryImpl(EditableContainer container) {
        this.container = container;
        this.editableContainer = container;
    }

    public Category createNewCategory(Object parentCategoryId) {
        Category cat = editableContainer.getObject(parentCategoryId, Category.class);
        if( cat == null ) throw new IllegalArgumentException("Cannot create category if not parent!");
        
        return cat.createNewCategory();
    }
    
    public Product createNewProduct(Object parentCategoryId) {
        Category cat = editableContainer.getObject(parentCategoryId, Category.class);
        if( cat == null ) throw new IllegalArgumentException("Cannot create category if not parent!");
        
        return cat.createNewProduct();
    }
    
    public Category findCategory(String id) {
        Container c = editableContainer != null ? editableContainer : container;
        return c.getObject(id, Category.class);
    }
    
    public Product findProduct(String id) {
        Container c = editableContainer != null ? editableContainer : container;
        return c.getObject(id, Product.class);
    }
    
    public Category[] getRootCategories() {
        ObjexObj obj = container.getRootObject();
        // TODO: Not sure about this method
        return null;
    }
    
    public Category[] findCategoriesByName(String name) {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public Product[] findExpiredProducts() {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public Product[] findProductsByCategory(String categoryId) {
        Product[] ret = null;
        
        Category cat = findCategory(categoryId);
        if( cat != null && cat.getProducts() != null ) {
            // FUTURE: This would be more efficient to get in a batch!
            
            List<Product> products = new ArrayList<Product>();
            String[] prods = cat.getProducts();
            for( int i = 0 ; i < prods.length ; i++ ) {
                Product p = container.getObject(prods[i], Product.class);
                if( p != null ) products.add(p);
            }
            
            if( products.size() > 0 ) ret = products.toArray(new Product[products.size()]);
        }

        return ret;
    }
    
    public Product[] findProductsByName(String name) {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public Product[] findProductsByPrice(double priceFrom, double priceTo) {
        throw new UnsupportedOperationException("Requires search capability");
    }
    
    public String suspend() {
        return editableContainer.suspend();
    }
    
    public void persist() {
        editableContainer.saveContainer();
    }
}
