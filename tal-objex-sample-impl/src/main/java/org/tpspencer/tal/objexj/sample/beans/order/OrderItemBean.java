package org.tpspencer.tal.objexj.sample.beans.order;

import javax.jdo.annotations.PersistenceCapable;

import org.tpspencer.tal.objexj.sample.api.order.OrderItemState;
import org.tpspencer.tal.objexj.sample.beans.BaseBean;

/**
 * An individual order item.
 * 
 * TODO: Currency/price should be compound object
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
public class OrderItemBean extends BaseBean implements OrderItemState {
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
	
	/*
     * (non-Javadoc)
     * @see org.tpspencer.tal.objexj.ObjexObjStateBean#getType()
     */
    public String getType() {
        return "OrderItem";
    }
    
    /* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getRef()
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getName()
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the stockItem
	 */
	public String getStockItem() {
		return stockItem;
	}
	public String getStockItemRef() {
		return stockItem;
	}
	/**
	 * @param stockItem the stockItem to set
	 */
	public void setStockItem(String stockItem) {
		this.stockItem = stockItem;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getQuantity()
	 */
	public double getQuantity() {
		return quantity;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#setQuantity(double)
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getMeasure()
	 */
	public String getMeasure() {
		return measure;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#setMeasure(java.lang.String)
	 */
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getPrice()
	 */
	public double getPrice() {
		return price;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#setPrice(double)
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#getCurrency()
	 */
	public String getCurrency() {
		return currency;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderItemState#setCurrency(java.lang.String)
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
