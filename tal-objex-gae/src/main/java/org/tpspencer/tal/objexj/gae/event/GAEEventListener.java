package org.tpspencer.tal.objexj.gae.event;

import java.io.Serializable;
import java.util.Arrays;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.tpspencer.tal.objexj.events.EventListener;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public final class GAEEventListener implements EventListener, Serializable {
    private final static long serialVersionUID = 1L;
    
    /** The primary key */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;
    
    private String container;
    private String eventProcessor;
    private boolean onCreation;
    private boolean onStateChange;
    private String[] interestedStates;
    private boolean onEdit;
    private String channel;
    private long delay;
    private long eta;
    
    /**
     * Default constructor
     */
    public GAEEventListener() {
    }
    
    /**
     * Copy constructor, copies an interface version of
     * the listener into this instance.
     */
    public GAEEventListener(EventListener listener) {
        this.container = listener.getContainer(); 
        this.eventProcessor = listener.getEventProcessor(); 
        this.onCreation = listener.isOnCreation();
        this.onStateChange = listener.isOnStateChange(); 
        this.interestedStates = listener.getInterestedStates(); 
        this.onEdit = listener.isOnEdit();
        this.channel = listener.getChannel(); 
        this.delay = listener.getDelay();
        this.eta = listener.getEta();
    }
    
    /**
     * @return the id
     */
    public Key getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Key id) {
        this.id = id;
    }

    /**
     * @return the container
     */
    public String getContainer() {
        return container;
    }
    /**
     * @param container the container to set
     */
    public void setContainer(String container) {
        this.container = container;
    }
    /**
     * @return the eventProcessor
     */
    public String getEventProcessor() {
        return eventProcessor;
    }
    /**
     * @param eventProcessor the eventProcessor to set
     */
    public void setEventProcessor(String eventProcessor) {
        this.eventProcessor = eventProcessor;
    }
    /**
     * @return the onCreation
     */
    public boolean isOnCreation() {
        return onCreation;
    }
    /**
     * @param onCreation the onCreation to set
     */
    public void setOnCreation(boolean onCreation) {
        this.onCreation = onCreation;
    }
    /**
     * @return the onStateChange
     */
    public boolean isOnStateChange() {
        return onStateChange;
    }
    /**
     * @param onStateChange the onStateChange to set
     */
    public void setOnStateChange(boolean onStateChange) {
        this.onStateChange = onStateChange;
    }
    /**
     * @return the interestedStates
     */
    public String[] getInterestedStates() {
        return interestedStates;
    }
    /**
     * @param interestedStates the interestedStates to set
     */
    public void setInterestedStates(String[] interestedStates) {
        this.interestedStates = interestedStates;
    }
    /**
     * @return the onEdit
     */
    public boolean isOnEdit() {
        return onEdit;
    }
    /**
     * @param onEdit the onEdit to set
     */
    public void setOnEdit(boolean onEdit) {
        this.onEdit = onEdit;
    }
    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }
    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    /**
     * @return the delay
     */
    public long getDelay() {
        return delay;
    }
    /**
     * @param delay the delay to set
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }
    /**
     * @return the eta
     */
    public long getEta() {
        return eta;
    }
    /**
     * @param eta the eta to set
     */
    public void setEta(long eta) {
        this.eta = eta;
    }
    
    /**
     * This method checks if the given event listener is
     * the same as this one. There should only be 1
     * event listener for a container per channel.
     * 
     * @param listener The listener to check
     * @return True if they are similar, false otherwise
     */
    public boolean similar(EventListener listener) {
        return container.equals(listener.getContainer()) && channel.equals(listener.getChannel());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GAEEventListener [channel=" + channel + ", container=" + container + ", delay=" + delay + ", eta=" + eta + ", eventProcessor=" + eventProcessor
                + ", id=" + id + ", interestedStates=" + Arrays.toString(interestedStates) + ", onCreation=" + onCreation + ", onEdit=" + onEdit
                + ", onStateChange=" + onStateChange + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channel == null) ? 0 : channel.hashCode());
        result = prime * result + ((container == null) ? 0 : container.hashCode());
        result = prime * result + (int)(delay ^ (delay >>> 32));
        result = prime * result + (int)(eta ^ (eta >>> 32));
        result = prime * result + ((eventProcessor == null) ? 0 : eventProcessor.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + Arrays.hashCode(interestedStates);
        result = prime * result + (onCreation ? 1231 : 1237);
        result = prime * result + (onEdit ? 1231 : 1237);
        result = prime * result + (onStateChange ? 1231 : 1237);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        GAEEventListener other = (GAEEventListener)obj;
        if( channel == null ) {
            if( other.channel != null ) return false;
        }
        else if( !channel.equals(other.channel) ) return false;
        if( container == null ) {
            if( other.container != null ) return false;
        }
        else if( !container.equals(other.container) ) return false;
        if( delay != other.delay ) return false;
        if( eta != other.eta ) return false;
        if( eventProcessor == null ) {
            if( other.eventProcessor != null ) return false;
        }
        else if( !eventProcessor.equals(other.eventProcessor) ) return false;
        if( id == null ) {
            if( other.id != null ) return false;
        }
        else if( !id.equals(other.id) ) return false;
        if( !Arrays.equals(interestedStates, other.interestedStates) ) return false;
        if( onCreation != other.onCreation ) return false;
        if( onEdit != other.onEdit ) return false;
        if( onStateChange != other.onStateChange ) return false;
        return true;
    }
}
