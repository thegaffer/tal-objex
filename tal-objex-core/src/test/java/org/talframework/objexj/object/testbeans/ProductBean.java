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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.validation.groups.FieldChangeGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * A test objex state bean representing some kind of product
 * that allows us to test elements of Objex
 * 
 * @author Tom Spencer
 */
public class ProductBean extends BaseTestBean {
    private static final long serialVersionUID = 1L;
    
    @NotNull @Size(max=50)
	private String name;
    @NotNull(groups={IntraObjectGroup.class, FieldChangeGroup.class}) 
    @Size(max=500)
	private String description;
    private int stockLevel;
	@Min(0)
    private double price;
	
	public ProductBean() {}
    
    public ProductBean(String name, String description, int level, double price) {
        this.name = name;
        this.description = description;
        this.stockLevel = level;
        this.price = price;
    }
    
    public void acceptReader(ObjexStateReader reader) {
        name = reader.read("name", name, String.class, ObjexFieldType.STRING, true);
        description = reader.read("description", description, String.class, ObjexFieldType.MEMO, true);
        stockLevel = reader.read("stockLevel", stockLevel, int.class, ObjexFieldType.NUMBER, true);
        price = reader.read("price", price, double.class, ObjexFieldType.NUMBER, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.STRING, true);
        writer.write("description", description, ObjexFieldType.MEMO, true);
        writer.write("stockLevel", stockLevel, ObjexFieldType.NUMBER, true);
        writer.write("price", price, ObjexFieldType.NUMBER, true);
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
	 * @return the stockLevel
	 */
	public int getStockLevel() {
		return stockLevel;
	}
	/**
	 * @param stockLevel the stockLevel to set
	 */
	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
}
