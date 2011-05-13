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

package org.talframework.objexj.object.testmodel.pojo;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.talframework.domain.annotations.ChildProperty;
import org.talframework.domain.annotations.IdentityObject;
import org.talframework.domain.annotations.ReferenceProperty;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.api.IProduct;

/**
 * This is a simple implementation of our test business/domain category
 * object. The tests demonstrate that this can be used via a proxy
 * object. 
 * 
 * <p>You will note here there is no use of any ObjexObj classes
 * here, however, we using the Objex behaviour annotations. This is 
 * could be ommitted in some circumstances, but in truth the object
 * cannot do much unless we mark some intent about it's relationships
 * to the outside world. The annotations are in a separate jar file
 * (tal-objex-annotations) so you do not need to rely on the rest of
 * Objex.</p> 
 * 
 * <p>Note: If anyone knows some other public/standard annotations that
 * can be used instead of the ones in the package 
 * org.talframework.objexj.annotations.runtime, let me know and well
 * use those instead!.</p>
 * 
 * @author Tom Spencer
 */
@IdentityObject
public class Category implements ICategory {
    
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
    @ReferenceProperty(IProduct.class)
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
    @ChildProperty(IProduct.class)
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
    @ChildProperty(ICategory.class)
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
