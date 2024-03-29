/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.beans.stock;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.utils.StateBeanUtils;
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
    
    @NotPersistent
    private int setFields = 0;
	
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
        this.setFields |= 0x0001;
		this.name = name;
	}
    
    public boolean isNameSet() {
        return (this.setFields & 0x0001) > 0;
    }
	
    @XmlAttribute
    public String getDescription() {
		return description;
	}
	
    public void setDescription(String description) {
        this.setFields |= 0x0002;
		this.description = description;
	}
    
    public boolean isDescriptionSet() {
        return (this.setFields & 0x0002) > 0;
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
	    this.setFields |= 0x0004;
		this.products = products;
	}
	
	public boolean isProductsSet() {
        return (this.setFields & 0x0004) > 0;
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
	    this.setFields |= 0x0008;
		this.categories = categories;
	}
	
	public boolean isCategoriesSet() {
        return (this.setFields & 0x0008) > 0;
    }
	
	public void acceptReader(ObjexStateReader reader) {
        name = reader.read("name", name, java.lang.String.class, ObjexFieldType.OBJECT, true);
        description = reader.read("description", description, java.lang.String.class, ObjexFieldType.OBJECT, true);
        products = reader.readReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        categories = reader.readReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.OBJECT, true);
        writer.write("description", description, ObjexFieldType.OBJECT, true);
        writer.writeReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        writer.writeReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CategoryBean [categories=" + categories + ", description=" + description + ", name=" + name + ", products=" + products + ", setFields="
                + setFields + "]";
    }
    
    
}
