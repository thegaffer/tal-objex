package org.tpspencer.tal.objexj.sample.beans.stock;

import java.util.List;

import org.tpspencer.tal.objexj.annotations.ObjexRefProp;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;
import org.tpspencer.tal.objexj.sample.api.stock.Category;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

/**
 * Represents a category of product, which can hold
 * products or more categories.
 * 
 * <p><b>Note: </b>Category is not a particularly good
 * example - the association between a category and a
 * product would be a lot 'looser' than the arrangement
 * in this sample. But I always use a Stock store with
 * Products and Categories because it was part of the 
 * first piece of 'Objex' software called SalesBase,
 * developed back in the dark ages - well circa 1995!
 * Please don't write to me about how you would do this
 * better!!</p> 
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="Category")
public class CategoryBean {
    private final static long serialVersionUID = 1L;
	
	/** The name of the category */
	private String name;
	
	/** The description of the category */
	private String description;
	
	/** The products in this category */
	@ObjexRefProp(owned=true, type=Product.class, newType="Product")
	private List<String> products;
	
	/** The sub-cateogories */
	@ObjexRefProp(owned=true, type=Category.class, newType="Category")
	private List<String> categories;
}
