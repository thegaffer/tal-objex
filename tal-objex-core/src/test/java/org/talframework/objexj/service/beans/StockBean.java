/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.service.beans;

import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;

public class StockBean implements ObjexObjStateBean {
    private static final long serialVersionUID = 1L;
    
	private String id = null;
	
	private String[] categories = null;
	
	public StockBean() {
	}
	
	public void create(ObjexID parentId) {
    }

	public void preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
	
	public ObjexObjStateBean cloneState() {
	    StockBean ret = new StockBean();
	    ret.setId(this.id);
	    ret.setCategories(this.categories); // Should clone, but ok for test
	    return ret;
	}
	
	public boolean isEditable() {
	    return true; // OK for test
	}
    
	public void setEditable() {
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
