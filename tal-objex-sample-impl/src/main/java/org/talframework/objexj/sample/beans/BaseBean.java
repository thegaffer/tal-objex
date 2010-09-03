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

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.ObjectUtils;

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
	    parentId = ObjectUtils.updateTempReferences(parentId, refs);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Object id) {
		if( this.id != null ) throw new IllegalArgumentException("You cannot reset a beans ID");
		this.id = id != null ? id.toString() : null;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Object parentId) {
		this.parentId = parentId != null ? parentId.toString() : null;
	}
	
	public boolean isEditable() {
	    return editable;
	}
	
	public void setEditable() {
	    this.editable = true;
	}
}
