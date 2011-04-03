package org.talframework.objexj.sample.stock;

import java.util.List;

public interface Category {

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
     * @return the categories
     */
    public abstract List<Category> getCategories();

    /**
     * Setter for the categories field
     *
     * @param categories the categories to set
     */
    public abstract void setCategories(List<Category> categories);

    /**
     * @return the products
     */
    public abstract List<Product> getProducts();

    /**
     * Setter for the products field
     *
     * @param products the products to set
     */
    public abstract void setProducts(List<Product> products);

}