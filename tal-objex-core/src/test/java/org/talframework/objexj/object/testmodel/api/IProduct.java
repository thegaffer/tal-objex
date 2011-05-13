package org.talframework.objexj.object.testmodel.api;

/**
 * This represents an interface to a product object which we use in our
 * tests.
 * 
 * <p><b>Authors Note: </b>I don't like the use of the prefix 'I', but
 * I want to name the interfaces and the objects the same in the tests.
 * Normally you would name the interfaces pure and suffix the impl
 * classes in some way.</p>
 *
 * @author Tom Spencer
 */
public interface IProduct {

    /**
     * @return the name
     */
    public String getName();

    /**
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * @return the description
     */
    public String getDescription();

    /**
     * @param description the description to set
     */
    public void setDescription(String description);

    /**
     * @return the stockLevel
     */
    public int getStockLevel();

    /**
     * @param stockLevel the stockLevel to set
     */
    public void setStockLevel(int stockLevel);

    /**
     * @return the price
     */
    public double getPrice();

    /**
     * @param price the price to set
     */
    public void setPrice(double price);

}