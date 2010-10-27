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

package org.talframework.objexj.object.testbeans;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.testbeans.BaseTestBean;

/**
 * A test objex state bean representing a category of products
 * that allows us to test elements of Objex
 * 
 * @author Tom Spencer
 */
public class CategoryBean extends BaseTestBean {
    private static final long serialVersionUID = 1L;
    
    @NotNull @Size(max=50)
    private String name;
    @NotNull @Size(max=500)
    private String description;
	
	private String mainProduct;
	private List<String> products;
	private Map<String, String> categories;
	
	public CategoryBean() {}
	
	public CategoryBean(String name, String description) {
	    this.name = name;
	    this.description = description;
	}
	
	public void acceptReader(ObjexStateReader reader) {
	    name = reader.read("name", name, String.class, ObjexFieldType.STRING, true);
	    description = reader.read("description", description, String.class, ObjexFieldType.MEMO, true);
	    mainProduct = reader.readReference("mainProduct", mainProduct, ObjexFieldType.REFERENCE, true);
	    products = reader.readReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
	    categories = reader.readReferenceMap("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
	}
	
	public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
	    writer.write("name", name, ObjexFieldType.STRING, true);
	    writer.write("description", description, ObjexFieldType.MEMO, true);
	    writer.writeReference("mainProduct", mainProduct, ObjexFieldType.REFERENCE, true);
	    writer.writeReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
	    writer.writeReferenceMap("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
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
