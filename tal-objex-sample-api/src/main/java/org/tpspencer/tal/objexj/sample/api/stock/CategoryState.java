package org.tpspencer.tal.objexj.sample.api.stock;

public interface CategoryState {
	
	/**
	 * @return The ID of this category
	 */
	public abstract Object getId();
	
	/**
	 * Set the ID of the category. Not allowed if already set.
	 * 
	 * @param id The ID to set
	 */
	public abstract void setId(Object id);
	
	/**
	 * @return The ID of the category that 'owns' us
	 */
	public abstract Object getParentId();
	
	/**
	 * Sets the category that owns us
	 * 
	 * @param id the categories id
	 */
	public abstract void setParentId(Object id);

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
	public abstract String[] getCategories();
	
	/**
	 * @param categories The new sub-categories
	 */
	public abstract void setCategories(String[] categories);

	/**
	 * @return The products in the category
	 */
	public abstract String[] getProducts();
	
	/**
	 * @param products The new products
	 */
	public abstract void setProducts(String[] products);
}