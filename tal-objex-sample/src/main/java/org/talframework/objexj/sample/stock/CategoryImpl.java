package org.talframework.objexj.sample.stock;

import java.util.List;

/**
 * This class is our Category business object implementation in the sample.
 *
 * @author Tom Spencer
 */
public class CategoryImpl implements Category {

    /** Holds the name of the category */
    private String name;
    /** Holds the description of the category */
    private String description;
    /** Holds child categories */
    private List<Category> categories;
    /** Holds the products in the category */
    private List<Product> products;
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getCategories()
     */
    @Override
    public List<Category> getCategories() {
        return categories;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setCategories(java.util.List)
     */
    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#getProducts()
     */
    @Override
    public List<Product> getProducts() {
        return products;
    }
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.stock.Category#setProducts(java.util.List)
     */
    @Override
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    
}
