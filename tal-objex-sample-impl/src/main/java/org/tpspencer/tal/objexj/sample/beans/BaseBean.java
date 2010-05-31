package org.tpspencer.tal.objexj.sample.beans;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.tpspencer.tal.objexj.ObjexObjStateBean;

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
	private String parentId;
	
	public BaseBean() {
	}
	
	public BaseBean(Object id, Object parentId) {
	    this.id = id != null ? id.toString() : null;
	    this.parentId = parentId != null ? parentId.toString() : null;
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
}
