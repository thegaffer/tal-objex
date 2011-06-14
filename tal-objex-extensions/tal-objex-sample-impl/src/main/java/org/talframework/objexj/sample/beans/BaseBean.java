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
package org.talframework.objexj.sample.beans;

import java.util.Map;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.utils.StateBeanUtils;

/**
 * Base class for any bean class. Holds the objects
 * own identity and its parent object (if any).
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE) 
public abstract class BaseBean implements ObjexObjStateBean {
    private final static long serialVersionUID = 1L;
	
	/** Holds the Id of this object */
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true") 
	private String id;
	
	/** Holds the parent objects' id (if any) */
	@Persistent
	private String parentId;
	
	@NotPersistent
	private transient boolean editable = false;
	
	public BaseBean() {
	}
	
	public void create(ObjexID parentId) {
	    this.parentId = parentId != null ? parentId.toString() : null;
	}
	
	public void preSave(Object id) {
	    this.id = id.toString();
	}
	
	public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs) {
	    parentId = StateBeanUtils.updateTempReferences(parentId, refs);
	}
	
	/**
	 * @return the id
	 */
	@XmlAttribute
	@XmlID
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		if( this.id != null ) throw new IllegalArgumentException("You cannot reset a beans ID");
		this.id = id != null ? id.toString() : null;
	}

	/**
	 * @return the parentId
	 */
	@XmlAttribute
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId != null ? parentId.toString() : null;
	}
	
	@XmlTransient
	public boolean isEditable() {
	    return editable;
	}
	
	public void setEditable() {
	    this.editable = true;
	}
}
