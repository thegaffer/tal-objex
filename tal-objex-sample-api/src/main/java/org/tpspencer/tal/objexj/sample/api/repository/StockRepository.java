package org.tpspencer.tal.objexj.sample.api.repository;

import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.CategoryState;
import org.tpspencer.tal.objexj.sample.api.stock.Product;
import org.tpspencer.tal.objexj.sample.api.stock.ProductState;

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
	
	public Category[] getRootCategories();
	
	public Category[] findCategoriesByName(String name);
	
	public Product[] findProductsByCategory(String categoryId);
	
	public Product[] findProductsByName(String name);
	
	public Product[] findProductsByPrice(double priceFrom, double priceTo);
	
	public Product[] findExpiredProducts();
	
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
