package org.tpspencer.tal.objexj.sample.api.stock;


/**
 * This interface represents a category (of products).
 * 
 * @author Tom Spencer
 */
public interface Category {

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
}
