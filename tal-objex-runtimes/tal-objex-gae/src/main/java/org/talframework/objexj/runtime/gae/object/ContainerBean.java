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
package org.talframework.objexj.runtime.gae.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.runtime.gae.event.GAEEventListener;

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
    
    /** The type of the container */
    private String type;
    /** The non-runtime environment ID of this container */
    private String containerId;
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
    
    /** Holds the registered listeners for this container */
    private List<GAEEventListener> registeredListeners;
    
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
     * @return The full container ID in type/id form
     */
    public String getFullContainerId() {
        if( containerId == null ) return null;
        if( type.equals(containerId) ) return containerId;
        else return type + "/" + containerId;
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
    /**
     * @return the registeredListeners
     */
    public List<GAEEventListener> getRegisteredListeners() {
        return registeredListeners;
    }
    /**
     * @param registeredListeners the registeredListeners to set
     */
    public void setRegisteredListeners(List<GAEEventListener> registeredListeners) {
        this.registeredListeners = registeredListeners;
    }
    /**
     * Helper to add a listener. It is only added if it
     * is unique.
     * 
     * @param listener The listener to add
     */
    public void addListener(EventListener listener) {
        // a. Make sure listener does not already exist
        if( registeredListeners != null ) {
            boolean found = false;
            Iterator<GAEEventListener> it = registeredListeners.iterator();
            while( !found && it.hasNext() ) {
                GAEEventListener current = it.next();
                found = current.similar(listener);
            }
            
            // If found exit, but silently, already registered
            if( found ) return;
        }
        
        // b. Convert to GAEListener and add it
        if( registeredListeners == null ) registeredListeners = new ArrayList<GAEEventListener>();
        registeredListeners.add(new GAEEventListener(listener));
    }
}
