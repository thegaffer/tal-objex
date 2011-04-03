package org.talframework.objexj.sample.stock;

/**
 * This class is our Product business object implementation in the sample.
 * 
 * <h5>Objex Sample Objects</h5>
 * <p>All the same objects in the simple sample have the same basic
 * characteristics. They are simple POJO classes. However, they must
 * implement an Interface - it is the interface that is projected to
 * the outside world when this object is presented via Objex.</p>
 * 
 * <p>All properties of this classes are treated as fields of the
 * Objex object. If the property is called 'id' is must be either a
 * string or an object type and will be set by Objex. Similarly if
 * there is a property called parentId is will also be set by Objex
 * and must be a string or object. Properties will only a getter are
 * treated as calculated and are not persisted. There are some 
 * annotations that can further refine these properties as transient
 * even if they do have a setter. Objex only supports a limited
 * range of basic types, but annotations exist to extend this.</p>
 * 
 * <p>Objects often have references to other objects that will exist
 * in the same container/document. For this the property in this 
 * class must use the interface to that property. Additionally, if
 * the properties is a list, map or set, then the interface to those
 * collection objects should be used (i.e. List<T>, not ArrayList<T>,
 * where T is the interface to the business object. All of these 
 * properties are handled as either references or parent/child
 * relationships based on whether there is a corresponding set or
 * a non-JavaBean create method.</p>
 *
 * @author Tom Spencer
 */
public class ProductImpl implements Product {

    private String id;
    private String name;
    private String description;
    private double price;
    private String currency;
    
    /** Holds the one and only nearest product to this one */
    private Product nearestProduct;
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#getId()
     */
    public String getId() {
        return id;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#setId(java.lang.String)
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#getName()
     */
    public String getName() {
        return name;
    }
    
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#getDescription()
     */
    public String getDescription() {
        return description;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#getPrice()
     */
    public double getPrice() {
        return price;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#setPrice(double)
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#getCurrency()
     */
    public String getCurrency() {
        return currency;
    }
    
    /* (non-Javadoc)
     * @see org.taleframework.objexj.sample.stock.impl.IProduct#setCurrency(java.lang.String)
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the nearestProduct
     */
    public Product getNearestProduct() {
        return nearestProduct;
    }

    /**
     * Setter for the nearestProduct field
     *
     * @param nearestProduct the nearestProduct to set
     */
    public void setNearestProduct(Product nearestProduct) {
        this.nearestProduct = nearestProduct;
    }
    
    
}
