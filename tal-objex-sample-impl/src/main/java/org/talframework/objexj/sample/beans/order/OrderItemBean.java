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

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.sample.beans.BaseBean;

/**
 * An individual order item.
 * 
 * TODO: Currency/price should be compound object
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
@XmlRootElement(name="OrderItem")
public class OrderItemBean extends BaseBean {
    private final static long serialVersionUID = 1L;

	/** A reference number for this */
	private String ref;
	/** Name of item - typically copied from Stock Item */
	private String name;
	/** Description - typically copied from stock item */
	private String description;
	/** Reference to the stock item (if any) */
	private String stockItem;
	/** The quantity of this item */
	private double quantity;
	/** The type of measure of this item (no, mm, kg etc) */
	private String measure;
	/** The price of this item */
	private double price;
	/** The currency of the price */
	private String currency;
	
	public OrderItemBean() {
    }
    
	public ObjexObjStateBean cloneState() {
        OrderItemBean ret = new OrderItemBean();
        ret.setId(this.getId());
        ret.setParentId(this.getParentId());
        ret.setRef(ref);
        ret.setName(name);
        ret.setDescription(description);
        ret.setStockItem(stockItem);
        ret.setQuantity(quantity);
        ret.setMeasure(measure);
        ret.setPrice(price);
        ret.setCurrency(currency);
        return ret;
    }
	
	@XmlTransient
	public String getObjexObjType() {
        return "OrderItem";
    }
    
	@XmlAttribute
    public String getRef() {
		return ref;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	@XmlAttribute
    public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
    public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the stockItem
	 */
	@XmlAttribute
    public String getStockItem() {
		return stockItem;
	}
	@XmlTransient
	public String getStockItemRef() {
		return stockItem;
	}
	/**
	 * @param stockItem the stockItem to set
	 */
	public void setStockItem(String stockItem) {
		this.stockItem = stockItem;
	}
	
	@XmlAttribute
    public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	@XmlAttribute
    public String getMeasure() {
		return measure;
	}
	
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	
	@XmlAttribute
    public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	@XmlAttribute
    public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void acceptReader(ObjexStateReader reader) {
        ref = reader.read("ref", java.lang.String.class, ObjexFieldType.OBJECT, true);
        name = reader.read("name", java.lang.String.class, ObjexFieldType.OBJECT, true);
        description = reader.read("description", java.lang.String.class, ObjexFieldType.OBJECT, true);
        stockItem = reader.readReference("stockItem", ObjexFieldType.REFERENCE, true);
        quantity = reader.read("quantity", double.class, ObjexFieldType.OBJECT, true);
        measure = reader.read("measure", java.lang.String.class, ObjexFieldType.OBJECT, true);
        price = reader.read("price", double.class, ObjexFieldType.OBJECT, true);
        currency = reader.read("currency", java.lang.String.class, ObjexFieldType.OBJECT, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("ref", ref, ObjexFieldType.OBJECT, true);
        writer.write("name", name, ObjexFieldType.OBJECT, true);
        writer.write("description", description, ObjexFieldType.OBJECT, true);
        writer.writeReference("stockItem", stockItem, ObjexFieldType.REFERENCE, true);
        writer.write("quantity", quantity, ObjexFieldType.OBJECT, true);
        writer.write("measure", measure, ObjexFieldType.OBJECT, true);
        writer.write("price", price, ObjexFieldType.OBJECT, true);
        writer.write("currency", currency, ObjexFieldType.OBJECT, true);
    }
}
