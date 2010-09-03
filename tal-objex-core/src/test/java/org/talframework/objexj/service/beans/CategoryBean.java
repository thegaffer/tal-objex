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

import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;

/**
 * This bean class represents the raw persistable version of
 * an object, in this case a CategoryBean. As it's simplest
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
public class CategoryBean implements ObjexObjStateBean {
    private static final long serialVersionUID = 1L;
    
	/** Represents the ID of this object */
	private String id;
	/** Represents the id of parent object of this object */
	private String parentId;
	/** Determines if bean is editable */
	private transient boolean editable = false;
	
	/** The name of the category */
	private String name;
	/** The description of the category */
	private String description;
	
	/** A main product - here to test */
	private String mainProduct;
	/** References to the products of this category */
	private List<String> products;
	/** A map of categories */
	private Map<String, String> categories;
	
	public CategoryBean() {}
	
	public CategoryBean(String name) {
	    this.name = name;
	}
	
	public void create(ObjexID parentId) {
	    this.parentId = parentId != null ? parentId.toString() : null;
	}
	
	public void preSave(Object id) {
	    this.id = id != null ? id.toString() : null;
	}
	
	public ObjexObjStateBean cloneState() {
	    CategoryBean ret = new CategoryBean();
	    ret.setId(this.id);
	    ret.setParentId(this.parentId);
	    ret.setName(this.name);
	    ret.setDescription(this.description);
	    ret.setProducts(this.products); // Should clone it, but ok in test
	    return ret;
	}
	
	public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
	    // No-op in this test bean
	}
	
	public boolean isEditable() {
	    return editable;
	}
	
	public void setEditable() {
	    editable = true;
	}
	
	/**
	 * Hard-coded type
	 */
	public String getObjexObjType() {
	    return "Category";
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
	public String getName2() {
	    return name;
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
     * @return the mainProduct
     */
    public String getMainProduct() {
        return mainProduct;
    }

    /**
     * Setter for the mainProduct field
     *
     * @param mainProduct the mainProduct to set
     */
    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    /**
     * @return the products
     */
    public List<String> getProducts() {
        return products;
    }

    /**
     * Setter for the products field
     *
     * @param products the products to set
     */
    public void setProducts(List<String> products) {
        this.products = products;
    }

    /**
     * @return the categories
     */
    public Map<String, String> getCategories() {
        return categories;
    }

    /**
     * Setter for the categories field
     *
     * @param categories the categories to set
     */
    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }
}
