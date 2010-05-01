package org.tpspencer.tal.objexj.sample.api.order;

/**
 * This interface represents the raw state of the order
 * object. 
 * 
 * @author Tom Spencer
 */
public interface OrderState {
	
	/**
	 * @return Call to set the ID of the order state
	 */
	public abstract String getId();

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
	public abstract String[] getItemRefs();
	
	/**
	 * @param items The items to set
	 */
	public abstract void setItemRefs(String[] items);
}