package org.tpspencer.tal.objexj.gae.object;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class represents the very root bean for all containers
 * stored in the Google App Engine.
 * 
 * TODO: Add in version
 * 
 * @author Tom Spencer
 */
@PersistenceCapable
public final class ContainerBean implements Serializable {
    private final static long serialVersionUID = 1L;

    /** The primary key */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;
    // private Key id;
    /** Holds the last ID used for objects inside the container */
    private long lastId;
    
    /** The type of the container */
    private String type;
    /** The non-runtime environment ID of this container */
    private String containerId;
    /** The name of this container */
    private String name;
    /** A description for this container */
    private String description;
    /** The status of this document */
    private String status;
    
    /** Holds the date the container was last updated */
    private Date updated;
    /** Holds the ID of the person who made the last update (if known) */
    private String author;
    
    /** Holds the date container was created */
    private Date created;
    /** Holds the person who created the document (if known) */
    private String creator;
    
    /**
     * @return the id
     */
    public Key getId() {
        return id;
    }
    public void setId(Key id) {
        this.id = id;
    }
    /**
     * @return the lastId
     */
    public long getLastId() {
        return lastId;
    }
    /**
     * @param lastId the lastId to set
     */
    public void setLastId(long lastId) {
        this.lastId = lastId;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the containerId
     */
    public String getContainerId() {
        return containerId;
    }
    /**
     * @param containerId the containerId to set
     */
    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }
    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }
    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }
    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @return the updated
     */
    public Date getUpdated() {
        return updated;
    }
    /**
     * @param updated the updated to set
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }
    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
