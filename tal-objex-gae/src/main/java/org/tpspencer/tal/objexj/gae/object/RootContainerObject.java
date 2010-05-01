package org.tpspencer.tal.objexj.gae.object;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class is the root object for any container inside
 * the Google App Engine. 
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
public class RootContainerObject {

	/** The primary key */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id = null;
	/** The type of the container */
	private String type;
	/** Holds the last ID used for objects inside the container */
	private long lastId;
	/** Holds the date container was created */
	private Date created;
	/** Holds the person who created the document (if known) */
	private String creator;
	/** Holds the date the container was last updated */
	private Date updated;
	/** Holds the ID of the person who made the last update (if known) */
	private String author;
	
	public Key getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getLastId() {
		return lastId;
	}
	public void setLastId(long lastId) {
		this.lastId = lastId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
}
