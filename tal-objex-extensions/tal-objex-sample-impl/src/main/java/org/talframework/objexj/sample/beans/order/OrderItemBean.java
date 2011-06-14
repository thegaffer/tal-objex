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
        ref = reader.read("ref", ref, String.class, ObjexFieldType.OBJECT, true);
        name = reader.read("name", name, String.class, ObjexFieldType.OBJECT, true);
        description = reader.read("description", description, String.class, ObjexFieldType.OBJECT, true);
        stockItem = reader.readReference("stockItem", stockItem, ObjexFieldType.REFERENCE, true);
        quantity = reader.read("quantity", quantity, double.class, ObjexFieldType.OBJECT, true);
        measure = reader.read("measure", measure, String.class, ObjexFieldType.OBJECT, true);
        price = reader.read("price", price, double.class, ObjexFieldType.OBJECT, true);
        currency = reader.read("currency", currency, String.class, ObjexFieldType.OBJECT, true);
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OrderItemBean [currency=" + currency + ", description=" + description + ", measure=" + measure + ", name=" + name + ", price=" + price
                + ", quantity=" + quantity + ", ref=" + ref + ", stockItem=" + stockItem + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((measure == null) ? 0 : measure.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(quantity);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        result = prime * result + ((ref == null) ? 0 : ref.hashCode());
        result = prime * result + ((stockItem == null) ? 0 : stockItem.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        OrderItemBean other = (OrderItemBean)obj;
        if( currency == null ) {
            if( other.currency != null ) return false;
        }
        else if( !currency.equals(other.currency) ) return false;
        if( description == null ) {
            if( other.description != null ) return false;
        }
        else if( !description.equals(other.description) ) return false;
        if( measure == null ) {
            if( other.measure != null ) return false;
        }
        else if( !measure.equals(other.measure) ) return false;
        if( name == null ) {
            if( other.name != null ) return false;
        }
        else if( !name.equals(other.name) ) return false;
        if( Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price) ) return false;
        if( Double.doubleToLongBits(quantity) != Double.doubleToLongBits(other.quantity) ) return false;
        if( ref == null ) {
            if( other.ref != null ) return false;
        }
        else if( !ref.equals(other.ref) ) return false;
        if( stockItem == null ) {
            if( other.stockItem != null ) return false;
        }
        else if( !stockItem.equals(other.stockItem) ) return false;
        return true;
    }
    
    
}
