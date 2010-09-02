package org.talframework.objexj.sample.beans.order;

import javax.jdo.annotations.PersistenceCapable;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.sample.beans.BaseBean;

/**
 * An individual order item.
 * 
 * TODO: Currency/price should be compound object
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
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
	
	public String getObjexObjType() {
        return "OrderItem";
    }
    
    public String getRef() {
		return ref;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
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
	
	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public String getMeasure() {
		return measure;
	}
	
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
