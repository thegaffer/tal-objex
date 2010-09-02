package org.talframework.objexj.sample.api.stock;

import java.util.List;


/**
 * This interface represents a category (of products).
 * 
 * @author Tom Spencer
 */
public interface Category {
    
    public abstract String getParentCategoryId();

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
     * @return The sub-categories
     */
    public abstract List<Category> getCategories();
    
    /**
     * @return The sub-categories
     */
    public abstract List<String> getCategoryRefs();
    
    /**
     * Call to create a new category inside this category
     * 
     * @return The new category
     */
    public Category createCategory();
    
    /**
     * @return The products in the category
     */
    public abstract List<Product> getProducts();
    
    /**
     * @return The products in the category
     */
    public abstract List<String> getProductRefs();
    
    /**
     * Call to create a new product inside this category
     * 
     * @return The new product
     */
    public Product createProduct();
}
