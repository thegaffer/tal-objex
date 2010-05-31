package org.tpspencer.tal.objexj.service.beans;

import org.tpspencer.tal.objexj.ObjexObjStateBean;

public class StockBean implements ObjexObjStateBean {
	private String id = null;
	
	private String[] categories = null;
	
	public StockBean() {
	    
	}
	
	public StockBean(Object id, Object parentId) {
        this.id = id != null ? id.toString() : null;
    }

	/**
     * Hard-coded type
     */
    public String getObjexObjType() {
        return "Stock";
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
	
	public Object getParentId() {
	    return null;
	}
	
	public void setParentId(Object parentId) {
	    // no-op
	}

	/**
	 * @return the categories
	 */
	public String[] getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
}
