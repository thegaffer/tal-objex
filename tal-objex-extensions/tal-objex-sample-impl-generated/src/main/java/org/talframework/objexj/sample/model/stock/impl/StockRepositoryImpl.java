/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.model.stock.impl;

import java.util.List;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.sample.api.repository.StockRepository;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;

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
