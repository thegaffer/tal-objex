package org.talframework.objexj.sample.stock;

public interface Product {

    /**
     * @return the id
     */
    public abstract String getId();

    /**
     * Setter for the id field
     *
     * @param id the id to set
     */
    public abstract void setId(String id);

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * Setter for the name field
     *
     * @param name the name to set
     */
    public abstract void setName(String name);

    /**
     * @return the description
     */
    public abstract String getDescription();

    /**
     * Setter for the description field
     *
     * @param description the description to set
     */
    public abstract void setDescription(String description);

    /**
     * @return the price
     */
    public abstract double getPrice();

    /**
     * Setter for the price field
     *
     * @param price the price to set
     */
    public abstract void setPrice(double price);

    /**
     * @return the currency
     */
    public abstract String getCurrency();

    /**
     * Setter for the currency field
     *
     * @param currency the currency to set
     */
    public abstract void setCurrency(String currency);

    /**
     * @return the nearestProduct
     */
    public Product getNearestProduct();

    /**
     * Setter for the nearestProduct field
     *
     * @param nearestProduct the nearestProduct to set
     */
    public void setNearestProduct(Product nearestProduct);
}