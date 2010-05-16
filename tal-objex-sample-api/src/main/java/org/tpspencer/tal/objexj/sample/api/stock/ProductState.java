package org.tpspencer.tal.objexj.sample.api.stock;

import java.util.Date;

public interface ProductState {
	
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
	public String getName();
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name);
	
	/**
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description);
	
	/**
	 * @return the effectiveFrom
	 */
	public Date getEffectiveFrom();
	
	/**
	 * @param effectiveFrom the effectiveFrom to set
	 */
	public void setEffectiveFrom(Date effectiveFrom);
	
	/**
	 * @return the effectiveTo
	 */
	public Date getEffectiveTo();
	
	/**
	 * @param effectiveTo the effectiveTo to set
	 */
	public void setEffectiveTo(Date effectiveTo);
	
	/**
	 * @return the price
	 */
	public double getPrice();
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price);
	
	/**
	 * @return the currency
	 */
	public String getCurrency();
	
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency);
}