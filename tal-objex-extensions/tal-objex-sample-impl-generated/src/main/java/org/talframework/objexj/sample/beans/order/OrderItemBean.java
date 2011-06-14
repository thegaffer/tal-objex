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
package org.talframework.objexj.sample.beans.order;

import org.talframework.objexj.annotations.source.ObjexRefProp;
import org.talframework.objexj.annotations.source.ObjexStateBean;
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
