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
