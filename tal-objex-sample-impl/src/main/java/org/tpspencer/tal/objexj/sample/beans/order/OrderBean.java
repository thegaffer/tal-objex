package org.tpspencer.tal.objexj.sample.beans.order;

import javax.jdo.annotations.PersistenceCapable;

import org.tpspencer.tal.objexj.sample.api.order.OrderState;
import org.tpspencer.tal.objexj.sample.beans.BaseBean;

/**
 * Represents an individual order. This is the root
 * object inside an order document.
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
public class OrderBean extends BaseBean implements OrderState {
	
	/** Holds the account number for the order (effectively a ref to customer) */
	private long account;
	/** Holds the details of the customer as they were at time of order */
	// TODO: Customer Details
	/** Holds the items in the order */
	private String[] items;
	/** Holds the summary price etc */
	// TODO: Summary details!?!
	
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderState#getAccount()
	 */
	public long getAccount() {
		return account;
	}
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.sample.beans.order.OrderState#setAccount(long)
	 */
	public void setAccount(long account) {
		this.account = account;
	}
	/**
	 * @return the items
	 */
	public String[] getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(String[] items) {
		this.items = items;
	}
}
