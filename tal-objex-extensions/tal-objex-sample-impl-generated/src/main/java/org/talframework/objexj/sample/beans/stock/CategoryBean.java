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
package org.talframework.objexj.sample.beans.stock;

import java.util.List;

import org.talframework.objexj.annotations.source.ObjexRefProp;
import org.talframework.objexj.annotations.source.ObjexStateBean;
import org.talframework.objexj.sample.api.stock.Category;
import org.talframework.objexj.sample.api.stock.Product;

/**
 * Represents a category of product, which can hold
 * products or more categories.
 * 
 * <p><b>Note: </b>Category is not a particularly good
 * example - the association between a category and a
 * product would be a lot 'looser' than the arrangement
 * in this sample. But I always use a Stock store with
 * Products and Categories because it was part of the 
 * first piece of 'Objex' software called SalesBase,
 * developed back in the dark ages - well circa 1995!
 * Please don't write to me about how you would do this
 * better!!</p> 
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="Category")
public class CategoryBean {
    private final static long serialVersionUID = 1L;
	
	/** The name of the category */
	private String name;
	
	/** The description of the category */
	private String description;
	
	/** The products in this category */
	@ObjexRefProp(owned=true, type=Product.class, newType="Product")
	private List<String> products;
	
	/** The sub-cateogories */
	@ObjexRefProp(owned=true, type=Category.class, newType="Category")
	private List<String> categories;
}
