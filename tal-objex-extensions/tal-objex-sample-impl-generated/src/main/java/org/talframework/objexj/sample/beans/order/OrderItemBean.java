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

package org.talframework.objexj.sample.beans.order;

import org.talframework.objexj.annotations.ObjexRefProp;
import org.talframework.objexj.annotations.ObjexStateBean;
import org.talframework.objexj.sample.api.stock.Product;

/**
 * An individual order item.
 * 
 * TODO: Currency/price should be compound object
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="OrderItem")
public class OrderItemBean {
    private final static long serialVersionUID = 1L;

	/** A reference number for this */
	private String ref;
	
	/** Name of item - typically copied from Stock Item */
	private String name;
	
	/** Description - typically copied from stock item */
	private String description;
	
	/** Reference to the stock item (if any) */
	@ObjexRefProp(owned=false, type=Product.class)
    private String stockItem;
	
	/** The quantity of this item */
	private double quantity;
	
	/** The type of measure of this item (no, mm, kg etc) */
	private String measure;
	
	/** The price of this item */
	private double price;
	
	/** The currency of the price */
	private String currency;
}
