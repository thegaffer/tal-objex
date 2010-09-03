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

package org.talframework.objexj.sample.api.stock;

import java.util.Date;


/**
 * Adds the business operations on to the core state.
 * This includes
 * 
 * @author Tom Spencer
 */
public interface Product {

	/**
     * @return the name
     */
    public String getName();
    
    /**
     * @param name the name to set
     */
    public void setName(String name);
    
    /**
     * @return the description
     */
    public String getDescription();
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description);
    
    /**
     * @return the effectiveFrom
     */
    public Date getEffectiveFrom();
    
    /**
     * @param effectiveFrom the effectiveFrom to set
     */
    public void setEffectiveFrom(Date effectiveFrom);
    
    /**
     * @return the effectiveTo
     */
    public Date getEffectiveTo();
    
    /**
     * @param effectiveTo the effectiveTo to set
     */
    public void setEffectiveTo(Date effectiveTo);
    
    /**
     * @return the price
     */
    public double getPrice();
    
    /**
     * @param price the price to set
     */
    public void setPrice(double price);
    
    /**
     * @return the currency
     */
    public String getCurrency();
    
    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency);
}
