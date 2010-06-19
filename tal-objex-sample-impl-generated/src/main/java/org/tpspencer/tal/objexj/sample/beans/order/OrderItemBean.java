package org.tpspencer.tal.objexj.sample.beans.order;

import org.tpspencer.tal.objexj.annotations.ObjexRefProp;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;
import org.tpspencer.tal.objexj.sample.api.stock.Product;

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
