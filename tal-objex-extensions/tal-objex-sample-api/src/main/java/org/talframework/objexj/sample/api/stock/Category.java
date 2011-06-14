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
