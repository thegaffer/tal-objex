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

package org.talframework.objexj.sample.beans.stock;

import java.util.Date;

import org.talframework.objexj.annotations.ObjexStateBean;

/**
 * Represents a product in our companies stock list
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="Product")
public class ProductBean {
    private final static long serialVersionUID = 1L;

	/** The name of the product */
	private String name;
	
	/** The description of the product */
	private String description;
	
	/** The products start date */
	private Date effectiveFrom;
	
	/** The products end date */
	private Date effectiveTo;
	
	/** The products price */
	private double price;
	
	/** The products price currency */
	private String currency;
}
