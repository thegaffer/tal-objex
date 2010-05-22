package org.tpspencer.tal.objexj.sample.api.stock;

import java.util.List;


/**
 * This interface represents a category (of products).
 * 
 * @author Tom Spencer
 */
public interface Category extends CategoryState {

	/**
	 * Call to get the current state of the cateogry.
	 * The return is detached and any changes made are
	 * not remembered.
	 * 
	 * @return The state of the category
	 */
	public CategoryState getCategoryState();
	
	/**
	 * Call to set the category state.
	 * 
	 * @param category The state to set
	 */
	public void setCategoryState(CategoryState category);
	
	/**
	 * @return The categories under this one
	 */
	public List<Category> getCategoryList();
	
	/**
	 * @return The products under this category
	 */
	public List<Product> getProductList();
	
    /**
     * Call to create a new category inside this category
     * 
     * @return The new categry
     */
    public Category createNewCategory();
	
	/**
	 * Call to create a new product inside this category
	 * 
	 * @return The new product
	 */
	public Product createNewProduct();
}
