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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.talframework.domain.annotations.IdentityObject;
import org.talframework.objexj.object.testmodel.api.IProduct;

/**
 * This is a simple implementation of our test business/domain product
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
public class Product implements IProduct {
    
    @NotNull @Size(max=50)
	private String name;
    @Size(max=500)
	private String description;
    private int stockLevel;
	@Min(0)
    private double price;
	
	public Product() {}
    
    public Product(String name, String description, int level, double price) {
        this.name = name;
        this.description = description;
        this.stockLevel = level;
        this.price = price;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#getName()
     */
	@Override
    public String getName() {
		return name;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#setName(java.lang.String)
     */
	@Override
    public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#getDescription()
     */
	@Override
    public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#setDescription(java.lang.String)
     */
	@Override
    public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#getStockLevel()
     */
	@Override
    public int getStockLevel() {
		return stockLevel;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#setStockLevel(int)
     */
	@Override
    public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#getPrice()
     */
	@Override
    public double getPrice() {
		return price;
	}
	/* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Product#setPrice(double)
     */
	@Override
    public void setPrice(double price) {
		this.price = price;
	}
}
