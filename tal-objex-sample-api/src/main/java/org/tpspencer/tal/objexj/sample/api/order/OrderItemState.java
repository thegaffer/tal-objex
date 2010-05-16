package org.tpspencer.tal.objexj.sample.api.order;


public interface OrderItemState {
	
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
	 * @return Reference to the stock item (if any)
	 */
	public abstract String getStockItem();
	
	/**
	 * @param item the item to set
	 */
	public abstract void setStockItem(String item);

	/**
	 * @return the ref
	 */
	public abstract String getRef();
	
	/**
	 * @param ref the ref to set
	 */
	public abstract void setRef(String ref);

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
	 * @return the quantity
	 */
	public abstract double getQuantity();

	/**
	 * @param quantity the quantity to set
	 */
	public abstract void setQuantity(double quantity);

	/**
	 * @return the measure
	 */
	public abstract String getMeasure();

	/**
	 * @param measure the measure to set
	 */
	public abstract void setMeasure(String measure);

	/**
	 * @return the price
	 */
	public abstract double getPrice();

	/**
	 * @param price the price to set
	 */
	public abstract void setPrice(double price);

	/**
	 * @return the currency
	 */
	public abstract String getCurrency();

	/**
	 * @param currency the currency to set
	 */
	public abstract void setCurrency(String currency);

}