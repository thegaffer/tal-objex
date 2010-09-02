package org.tpspencer.tal.objexj.sample.beans.order;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;
import org.tpspencer.tal.objexj.sample.beans.BaseBean;

/**
 * Represents an individual order. This is the root
 * object inside an order document.
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
public class OrderBean extends BaseBean {
    private final static long serialVersionUID = 1L;
	
	/** Holds the account number for the order (effectively a ref to customer) */
	private long account;
	/** Holds the items in the order */
	private List<String> items;
	
	public OrderBean() {
	}
	
	public ObjexObjStateBean cloneState() {
	    OrderBean ret = new OrderBean();
	    ret.setId(this.getId());
	    ret.setParentId(this.getParentId());
	    ret.setAccount(account);
	    ret.setItems(items);
	    return ret;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tpspencer.tal.objexj.ObjexObjStateBean#getObjexObjType()
	 */
	public String getObjexObjType() {
	    return "Order";
	}
	
	@Override
	public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
	    super.updateTemporaryReferences(refs);
	    items = ObjectUtils.updateTempReferences(items, refs);
	}
	
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
	public List<String> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}
}
