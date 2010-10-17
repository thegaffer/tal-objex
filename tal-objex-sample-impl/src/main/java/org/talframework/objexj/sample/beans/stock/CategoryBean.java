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

package org.talframework.objexj.sample.beans.stock;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.StateBeanUtils;
import org.talframework.objexj.sample.beans.BaseBean;

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
@XmlRootElement(name="Category")
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
    
	public ObjexObjStateBean cloneState() {
        CategoryBean ret = new CategoryBean();
        ret.setId(this.getId());
        ret.setParentId(this.getParentId());
        ret.setName(name);
        ret.setDescription(description);
        ret.setProducts(products);
        ret.setCategories(categories);
        return ret;
    }
    
	@XmlTransient
    public String getObjexObjType() {
        return "Category";
    }
    
    @Override
    public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
        super.updateTemporaryReferences(refs);
        products = StateBeanUtils.updateTempReferences(products, refs);
        categories = StateBeanUtils.updateTempReferences(categories, refs);
    }
    
    @XmlAttribute
    public String getName() {
		return name;
	}
	
    public void setName(String name) {
		this.name = name;
	}
	
    @XmlAttribute
    public String getDescription() {
		return description;
	}
	
    public void setDescription(String description) {
		this.description = description;
	}
	
    /**
	 * @return the products
	 */
	@XmlList
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
	@XmlList
	public List<String> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public void acceptReader(ObjexStateReader reader) {
        name = reader.read("name", java.lang.String.class, ObjexFieldType.OBJECT, true);
        description = reader.read("description", java.lang.String.class, ObjexFieldType.OBJECT, true);
        products = reader.readReferenceList("products", ObjexFieldType.OWNED_REFERENCE, true);
        categories = reader.readReferenceList("categories", ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.OBJECT, true);
        writer.write("description", description, ObjexFieldType.OBJECT, true);
        writer.writeReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        writer.writeReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }
}
