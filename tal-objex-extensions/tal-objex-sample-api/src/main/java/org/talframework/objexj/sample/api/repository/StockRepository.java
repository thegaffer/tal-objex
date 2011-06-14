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
package org.talframework.objexj.sample.api.repository;

import java.util.List;

import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;

/**
 * This interface represents a domain-like service interface
 * around the Stock store. It is not neccessary to use this
 * interface.
 * 
 * @author Tom Spencer
 */
public interface StockRepository {
    
    public Product createNewProduct(Object parentCategoryId);
    
    public Category createNewCategory(Object parentCategoryId);

	public Category findCategory(String id);
	
	public Product findProduct(String id);
	
	public List<Category> getRootCategories();
	
	public List<Category> findCategoriesByName(String name);
	
	public List<Product> findProductsByCategory(String categoryId);
	
	public List<Product> findProductsByName(String name);
	
	public List<Product> findProductsByPrice(double priceFrom, double priceTo);
	
	public List<Product> findExpiredProducts();
	
	/**
	 * Call to suspend all changes currently made and
	 * come back to them later 
	 * 
	 * @return The ID of the transaction
	 */
	public String suspend();
	
	/**
	 * Call to persist all changes made on this repository.
	 */
	public void persist();
}
