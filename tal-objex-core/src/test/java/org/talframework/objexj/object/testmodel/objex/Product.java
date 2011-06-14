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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.testmodel.api.IProduct;
import org.talframework.objexj.validation.groups.FieldChangeGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * This object implements our test Product interface. This is also an
 * example of how you can directly support Objex by implementing the
 * {@link ObjexObj} interface via {@link BaseObjexObj} - there are
 * generators that make this even easier. Although we override a
 * couple of methods from the base class for efficiency, more could
 * be overridden.
 *
 * @author Tom Spencer
 */
public class Product extends BaseObjexObj implements IProduct {
    
    @NotNull @Size(max=50)
	private String name;
    @NotNull(groups={IntraObjectGroup.class, FieldChangeGroup.class}) 
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
    
    /////////////////////////////////////////////////////////////////////////////
    // Overridden BaseObjexObj (for efficiency and so we don't supply a strategy)
    
    @Override
    public void acceptReader(ObjexStateReader reader) {
        setParentId(reader.read("parentId", getParentId(), ObjexID.class, ObjexFieldType.PARENT_ID, true));
        
        name = reader.read("name", name, String.class, ObjexFieldType.STRING, true);
        description = reader.read("description", description, String.class, ObjexFieldType.MEMO, true);
        stockLevel = reader.read("stockLevel", stockLevel, int.class, ObjexFieldType.NUMBER, true);
        price = reader.read("price", price, double.class, ObjexFieldType.NUMBER, true);
    }
    
    @Override
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.STRING, true);
        writer.write("description", description, ObjexFieldType.MEMO, true);
        writer.write("stockLevel", stockLevel, ObjexFieldType.NUMBER, true);
        writer.write("price", price, ObjexFieldType.NUMBER, true);
    }
    
    /////////////////////////////////////////////////////////////////////////////

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
	    validateValue(this, "name", name);
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
