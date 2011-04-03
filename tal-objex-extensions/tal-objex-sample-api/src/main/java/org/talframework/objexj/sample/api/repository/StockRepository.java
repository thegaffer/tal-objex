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
