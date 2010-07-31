package org.tpspencer.tal.objexj.service.beans;

import java.util.Map;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

public class StockBean implements ObjexObjStateBean {
    private static final long serialVersionUID = 1L;
    
	private String id = null;
	
	private String[] categories = null;
	
	public StockBean() {
	}
	
	public StockBean(ObjexID parentId) {
    }

	public void init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
        // No-op in this test bean
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
	
	public String getParentId() {
	    return null;
	}
	
	public void setParentId(String parentId) {
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
