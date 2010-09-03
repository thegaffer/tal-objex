/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.sample.beans.order;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.ObjectUtils;
import org.talframework.objexj.sample.beans.BaseBean;

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
	
	public String getObjexObjType() {
	    return "Order";
	}
	
	@Override
	public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
	    super.updateTemporaryReferences(refs);
	    items = ObjectUtils.updateTempReferences(items, refs);
	}
	
	public long getAccount() {
		return account;
	}
	
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
