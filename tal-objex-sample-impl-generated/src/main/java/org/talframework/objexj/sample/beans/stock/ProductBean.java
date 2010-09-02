package org.talframework.objexj.sample.beans.stock;

import java.util.Date;

import org.talframework.objexj.annotations.ObjexStateBean;

/**
 * Represents a product in our companies stock list
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="Product")
public class ProductBean {
    private final static long serialVersionUID = 1L;

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
}
