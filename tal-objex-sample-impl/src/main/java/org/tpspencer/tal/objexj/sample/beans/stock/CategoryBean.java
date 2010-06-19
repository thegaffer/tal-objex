package org.tpspencer.tal.objexj.sample.beans.stock;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

import org.tpspencer.tal.objexj.sample.beans.BaseBean;

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
@PersistenceCapable
public class CategoryBean extends BaseBean {
    private final static long serialVersionUID = 1L;
	
	/** The name of the category */
	private String name;
	/** The description of the category */
	private String description;
	/** The products in this category */
	private List<String> products;
	/** The sub-cateogories */
	private List<String> categories;
	
	public CategoryBean() {
    }
    
    public CategoryBean(Object id, Object parentId) {
        super(id, parentId);
    }
	
	/*
     * (non-Javadoc)
     * @see org.tpspencer.tal.objexj.ObjexObjStateBean#getObjexObjType()
     */
    public String getObjexObjType() {
        return "Category";
    }
    
    /* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.stock.CategoryState#getName()
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.stock.CategoryState#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.stock.CategoryState#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.stock.CategoryState#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the products
	 */
	public List<String> getProducts() {
		return products;
	}
	/**
	 * @param products the products to set
	 */
	public void setProducts(List<String> products) {
		this.products = products;
	}
	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
}
