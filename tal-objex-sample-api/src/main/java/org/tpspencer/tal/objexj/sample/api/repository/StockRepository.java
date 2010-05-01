package org.tpspencer.tal.objexj.sample.api.repository;

import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

/**
 * This interface represents a domain-like service interface
 * around the Stock store. It is not neccessary to use this
 * interface.
 * 
 * @author Tom Spencer
 */
public interface StockRepository {

	public Category findCategory(String id);
	
	public Product findProduct(String id);
	
	public Category[] findRootCategories();
	
	public Category[] findCategoriesByName(String name);
	
	public Product[] findProductsByCategory(String categoryId);
	
	public Product[] findProductsByName(String name);
	
	public Product[] findProductsByPrice(double priceFrom, double priceTo);
	
	public Product[] findExpiredProducts();
}
