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
