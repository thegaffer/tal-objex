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
package org.talframework.objexj.sample.stock;

import java.util.List;

/**
 * This class is our Category business object implementation in the sample.
 *
 * @author Tom Spencer
 */
public class CategoryImpl implements Category {

    /** Holds the name of the category */
    private String name;
    /** Holds the description of the category */
    private String description;
    /** Holds child categories */
    private List<Category> categories;
    /** Holds the products in the category */
    private List<Product> products;
    
    public CategoryImpl() {
    }
    
    public CategoryImpl(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getCategories()
     */
    @Override
    public List<Category> getCategories() {
        return categories;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setCategories(java.util.List)
     */
    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getProducts()
     */
    @Override
    public List<Product> getProducts() {
        return products;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setProducts(java.util.List)
     */
    @Override
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    
}
