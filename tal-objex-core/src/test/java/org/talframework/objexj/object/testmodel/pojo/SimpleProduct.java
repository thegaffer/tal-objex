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

/**
 * This class represents a very simple object that exposes no
 * interfaces. It can still be used by Objex as a simple object.
 * 
 * @author Tom Spencer
 */
public class SimpleProduct {
    
    @NotNull @Size(max=50)
	private String name;
    @Size(max=500)
	private String description;
    private int stockLevel;
	@Min(0)
    private double price;
	
	public SimpleProduct() {}
    
    public SimpleProduct(String name, String description, int level, double price) {
        this.name = name;
        this.description = description;
        this.stockLevel = level;
        this.price = price;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name field
     *
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
     * Setter for the description field
     *
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
     * Setter for the stockLevel field
     *
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
     * Setter for the price field
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
