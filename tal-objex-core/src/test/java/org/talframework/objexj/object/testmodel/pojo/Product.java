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
