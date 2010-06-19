package org.tpspencer.tal.objexj.sample.api.order;

import java.util.List;

/**
 * The business interface to an order
 * 
 * @author Tom Spencer
 */
public interface Order {
    
    /**
     * @return the account
     */
    public abstract long getAccount();

    /**
     * @param account the account to set
     */
    public abstract void setAccount(long account);

    /**
     * @return A reference to each item in the order
     */
    public abstract List<OrderItem> getItems();
    
    /**
     * @return A reference to each item in the order
     */
    public abstract List<String> getItemRefs();
    
    /**
     * Helper to get an order item by ID
     * 
     * @param id The ID
     */
    public abstract OrderItem getItemById(Object id);
    
    public abstract OrderItem createItem();
	
	public abstract void removeItemById(Object id);
}
