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

package org.talframework.objexj.sample.api.stock;

import java.util.List;


/**
 * This interface represents a category (of products).
 * 
 * @author Tom Spencer
 */
public interface Category {
    
    public abstract String getParentCategoryId();

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * @param name the name to set
     */
    public abstract void setName(String name);

    /**
     * @return the description
     */
    public abstract String getDescription();

    /**
     * @param description the description to set
     */
    public abstract void setDescription(String description);
    
    /**
     * @return The sub-categories
     */
    public abstract List<Category> getCategories();
    
    /**
     * @return The sub-categories
     */
    public abstract List<String> getCategoryRefs();
    
    /**
     * Call to create a new category inside this category
     * 
     * @return The new category
     */
    public Category createCategory();
    
    /**
     * @return The products in the category
     */
    public abstract List<Product> getProducts();
    
    /**
     * @return The products in the category
     */
    public abstract List<String> getProductRefs();
    
    /**
     * Call to create a new product inside this category
     * 
     * @return The new product
     */
    public Product createProduct();
}
