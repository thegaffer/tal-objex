package org.tpspencer.tal.objexj.sample.beans.stock;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

import org.tpspencer.tal.objexj.sample.api.stock.ProductState;
import org.tpspencer.tal.objexj.sample.beans.BaseBean;

/**
 * Represents a product in our companies stock list
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
public class ProductBean extends BaseBean implements ProductState {

	/** The name of the product */
	private String name;
	/** The description of the product */
	private String description;
	/** The products start date */
	private Date effectiveFrom;
	/** The products end date */
	private Date effectiveTo;
	/** The products price */
	private double price;
	/** The products price currency */
	private String currency;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the effectiveFrom
	 */
	public Date getEffectiveFrom() {
		return effectiveFrom;
	}
	/**
	 * @param effectiveFrom the effectiveFrom to set
	 */
	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	/**
	 * @return the effectiveTo
	 */
	public Date getEffectiveTo() {
		return effectiveTo;
	}
	/**
	 * @param effectiveTo the effectiveTo to set
	 */
	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
