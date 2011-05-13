package org.talframework.objexj.object.testmodel.api;

import java.util.List;
import java.util.Map;

/**
 * This represents an interface to a categry object which we use in our
 * tests.
 * 
 * <p><b>Authors Note: </b>I don't like the use of the prefix 'I', but
 * I want to name the interfaces and the objects the same in the tests.
 * Normally you would name the interfaces pure and suffix the impl
 * classes in some way.</p>
 *
 * @author Tom Spencer
 */
public interface ICategory {

    /**
     * @return the name
     */
    public String getName();

    /**
     * Setter for the name field
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * @return the description
     */
    public String getDescription();

    /**
     * Setter for the description field
     *
     * @param description the description to set
     */
    public void setDescription(String description);

    /**
     * @return the mainProduct
     */
    public IProduct getMainProduct();

    /**
     * Setter for the mainProduct field
     *
     * @param mainProduct the mainProduct to set
     */
    public void setMainProduct(IProduct mainProduct);

    /**
     * @return the products
     */
    public List<IProduct> getProducts();

    /**
     * Setter for the products field
     *
     * @param products the products to set
     */
    public void setProducts(List<IProduct> products);

    /**
     * @return the categories
     */
    public Map<String, ICategory> getCategories();

    /**
     * Setter for the categories field
     *
     * @param categories the categories to set
     */
    public void setCategories(Map<String, ICategory> categories);

}