package org.tpspencer.tal.objexj.sample.api.order;

public interface OrderItemState {
	
	/**
	 * @return Reference to the stock item (if any)
	 */
	public abstract String getStockItemRef();

	/**
	 * @return the ref
	 */
	public abstract String getRef();

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