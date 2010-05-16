package org.tpspencer.tal.objexj.sample.api.order;

/**
 * This interface represents the raw state of the order
 * object. 
 * 
 * @author Tom Spencer
 */
public interface OrderState {
	
	/**
	 * @return The ID of this category
	 */
	public abstract Object getId();
	
	/**
	 * Set the ID of the category. Not allowed if already set.
	 * 
	 * @param id The ID to set
	 */
	public abstract void setId(Object id);
	
	/**
	 * @return The ID of the category that 'owns' us
	 */
	public abstract Object getParentId();
	
	/**
	 * Sets the category that owns us
	 * 
	 * @param id the categories id
	 */
	public abstract void setParentId(Object id);

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
	public abstract String[] getItems();
	
	/**
	 * @param items The items to set
	 */
	public abstract void setItems(String[] items);
}