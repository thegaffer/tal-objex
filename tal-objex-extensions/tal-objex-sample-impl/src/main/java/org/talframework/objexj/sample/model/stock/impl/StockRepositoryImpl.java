/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
