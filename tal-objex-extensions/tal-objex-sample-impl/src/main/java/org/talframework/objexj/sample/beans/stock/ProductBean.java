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

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.sample.beans.BaseBean;

/**
 * Represents a product in our companies stock list
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
@XmlRootElement(name="Product")
public class ProductBean extends BaseBean {
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
	
	public ProductBean() {
    }
    
	public ObjexObjStateBean cloneState() {
        ProductBean ret = new ProductBean();
        ret.setId(this.getId());
        ret.setParentId(this.getParentId());
        ret.setName(name);
        ret.setDescription(description);
        ret.setEffectiveFrom(effectiveFrom);
        ret.setEffectiveTo(effectiveTo);
        ret.setPrice(price);
        ret.setCurrency(currency);
        return ret;
    }
	
	@XmlTransient
	public String getObjexObjType() {
        return "Product";
    }
    
    /**
	 * @return the name
	 */
	@XmlAttribute
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
	@XmlAttribute
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
	 * @return the effectiveFrom
	 */
	@XmlAttribute
    public Date getEffectiveFrom() {
		return effectiveFrom;
	}
	/**
	 * @param effectiveFrom the effectiveFrom to set
	 */
	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	/**
	 * @return the effectiveTo
	 */
	@XmlAttribute
    public Date getEffectiveTo() {
		return effectiveTo;
	}
	/**
	 * @param effectiveTo the effectiveTo to set
	 */
	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	/**
	 * @return the price
	 */
	@XmlAttribute
    public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the currency
	 */
	@XmlAttribute
    public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void acceptReader(ObjexStateReader reader) {
        name = reader.read("name", name, java.lang.String.class, ObjexFieldType.OBJECT, true);
        description = reader.read("description", description, java.lang.String.class, ObjexFieldType.OBJECT, true);
        effectiveFrom = reader.read("effectiveFrom", effectiveFrom, java.util.Date.class, ObjexFieldType.OBJECT, true);
        effectiveTo = reader.read("effectiveTo", effectiveTo, java.util.Date.class, ObjexFieldType.OBJECT, true);
        price = reader.read("price", price, double.class, ObjexFieldType.OBJECT, true);
        currency = reader.read("currency", currency, java.lang.String.class, ObjexFieldType.OBJECT, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("name", name, ObjexFieldType.OBJECT, true);
        writer.write("description", description, ObjexFieldType.OBJECT, true);
        writer.write("effectiveFrom", effectiveFrom, ObjexFieldType.OBJECT, true);
        writer.write("effectiveTo", effectiveTo, ObjexFieldType.OBJECT, true);
        writer.write("price", price, ObjexFieldType.OBJECT, true);
        writer.write("currency", currency, ObjexFieldType.OBJECT, true);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProductBean [currency=" + currency + ", description=" + description + ", effectiveFrom=" + effectiveFrom + ", effectiveTo=" + effectiveTo
                + ", name=" + name + ", price=" + price + "]";
    }
    
    
}
