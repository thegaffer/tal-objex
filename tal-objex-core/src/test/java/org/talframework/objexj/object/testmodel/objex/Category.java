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

package org.talframework.objexj.object.testmodel.objex;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.api.IProduct;
import org.talframework.objexj.service.beans.rules.CategoryValid;

/**
 * This object implements our test Category interface. This is also an
 * example of how you can directly support Objex by implementing the
 * {@link ObjexObj} interface via {@link BaseObjexObj} - there are
 * generators that make this even easier. Although we override a
 * couple of methods from the base class for efficiency, more could
 * be overridden.
 *
 * @author Tom Spencer
 */
@CategoryValid // Custom validation Annotation
public class Category extends BaseObjexObj implements ICategory {
    
    @NotNull @Size(max=50)
    private String name;
    @NotNull @Size(max=500)
    private String description;
	
	private IProduct mainProduct;
	private List<IProduct> products;
	private Map<String, ICategory> categories;
	
	public Category() {}
	
	public Category(String name, String description) {
	    this.name = name;
	    this.description = description;
	}

	/////////////////////////////////////////////////////////////////////////////
    // Overridden BaseObjexObj (for efficiency and so we don't supply a strategy)
	
	@Override
    public void acceptReader(ObjexStateReader reader) {
	    setParentId(reader.read("parentId", getParentId(), ObjexID.class, ObjexFieldType.PARENT_ID, true));
	    
        name = reader.read("name", name, String.class, ObjexFieldType.STRING, true);
        description = reader.read("description", description, String.class, ObjexFieldType.MEMO, true);
        mainProduct = reader.readReference("mainProduct", mainProduct, IProduct.class, ObjexFieldType.REFERENCE, true);
        products = reader.readReferenceList("products", products, IProduct.class, ObjexFieldType.OWNED_REFERENCE, true);
        categories = reader.readReferenceMap("categories", categories, ICategory.class, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    @Override
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.STRING, true);
        writer.write("description", description, ObjexFieldType.MEMO, true);
        writer.writeReference("mainProduct", mainProduct, ObjexFieldType.REFERENCE, true);
        writer.writeReferenceList("products", products, ObjexFieldType.OWNED_REFERENCE, true);
        writer.writeReferenceMap("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    /////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#getMainProduct()
     */
    @Override
    public IProduct getMainProduct() {
        return mainProduct;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#setMainProduct(org.talframework.objexj.object.testmodel.api.Product)
     */
    @Override
    public void setMainProduct(IProduct mainProduct) {
        this.mainProduct = mainProduct;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#getProducts()
     */
    @Override
    public List<IProduct> getProducts() {
        return products;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#setProducts(java.util.List)
     */
    @Override
    public void setProducts(List<IProduct> products) {
        this.products = products;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#getCategories()
     */
    @Override
    public Map<String, ICategory> getCategories() {
        return categories;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Category#setCategories(java.util.Map)
     */
    @Override
    public void setCategories(Map<String, ICategory> categories) {
        this.categories = categories;
    }
}
