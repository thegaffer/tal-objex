package org.tpspencer.tal.objexj.service.beans;

import org.tpspencer.tal.objexj.ObjexObjStateBean;

/**
 * This bean class represents the raw persistable version of
 * an object, in this case a ProductBean. As it's simplest
 * level it will simply have private members with appropriate
 * getter/setter values.
 * 
 * <p>Objex requires that there are up to 3 attributes on the
 * raw bean. These are the 'id' of the object, the 'parentId' 
 * of the object and optionally a version if controlling
 * versions at object level.</p>
 * 
 * <p>What is not shown though is any adournment to this class.
 * Typically we might expect to see JSR303 annotations to
 * express the raw validation of this bean and secondly we will
 * likely see annotations for the persistence. Though the later
 * is not neccessary (most persistence technologies allow you 
 * to specify this in a file) it is usual to do this.</p>
 * 
 * @author Tom Spencer
 */
public class ProductBean implements ObjexObjStateBean {
	private String id;
	private String parentId;
	
	private String name;
	private String description;
	private int stockLevel;
	private double price;
	
	public ProductBean() {
        // TODO Auto-generated constructor stub
    }
	
	public ProductBean(Object id, Object parentId) {
        this.id = id != null ? id.toString() : null;
        this.parentId = parentId != null ? parentId.toString() : null;
    }
	
	/**
     * Hard-coded type
     */
    public String getObjexObjType() {
        return "Product";
    }
    
    /**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Object id) {
		this.id = id != null ? id.toString() : null;
	}
	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Object parentId) {
		this.parentId = parentId != null ? parentId.toString() : null;
	}
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
	 * @return the stockLevel
	 */
	public int getStockLevel() {
		return stockLevel;
	}
	/**
	 * @param stockLevel the stockLevel to set
	 */
	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
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
}
