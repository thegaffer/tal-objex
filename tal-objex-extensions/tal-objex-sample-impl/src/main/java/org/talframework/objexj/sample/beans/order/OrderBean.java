/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.sample.beans.order;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.utils.StateBeanUtils;
import org.talframework.objexj.sample.beans.BaseBean;

/**
 * Represents an individual order. This is the root
 * object inside an order document.
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
@XmlRootElement(name="Order")
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
	
	@XmlTransient
	public String getObjexObjType() {
	    return "Order";
	}
	
	@Override
	public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
	    super.updateTemporaryReferences(refs);
	    items = StateBeanUtils.updateTempReferences(items, refs);
	}
	
	@XmlAttribute
	public long getAccount() {
		return account;
	}
	
	public void setAccount(long account) {
		this.account = account;
	}
	/**
	 * @return the items
	 */
	@XmlList
	public List<String> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}
	
	public void acceptReader(ObjexStateReader reader) {
        account = reader.read("account", account, long.class, ObjexFieldType.OBJECT, true);
        items = reader.readReferenceList("items", items, ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.write("account", account, ObjexFieldType.OBJECT, true);
        writer.writeReferenceList("items", items, ObjexFieldType.OWNED_REFERENCE, true);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OrderBean [account=" + account + ", items=" + items + "]";
    }
}
