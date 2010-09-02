package org.talframework.objexj.sample.api.order;

import org.talframework.objexj.sample.api.stock.Product;

public interface OrderItem {

    /**
     * @return Reference to the stock item (if any)
     */
    public abstract Product getStockItem();
    
    /**
     * @param item the item to set
     */
    public abstract void setStockItem(Product item);

    /**
     * @return the ref
     */
    public abstract String getRef();
    
    /**
     * @param ref the ref to set
     */
    public abstract void setRef(String ref);

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * @param name the name to set
     */
    public abstract void setName(String name);

    /**
     * @return the description
     */
    public abstract String getDescription();

    /**
     * @param description the description to set
     */
    public abstract void setDescription(String description);

    /**
     * @return the quantity
     */
    public abstract double getQuantity();

    /**
     * @param quantity the quantity to set
     */
    public abstract void setQuantity(double quantity);

    /**
     * @return the measure
     */
    public abstract String getMeasure();

    /**
     * @param measure the measure to set
     */
    public abstract void setMeasure(String measure);

    /**
     * @return the price
     */
    public abstract double getPrice();

    /**
     * @param price the price to set
     */
    public abstract void setPrice(double price);

    /**
     * @return the currency
     */
    public abstract String getCurrency();

    /**
     * @param currency the currency to set
     */
    public abstract void setCurrency(String currency); 
}
