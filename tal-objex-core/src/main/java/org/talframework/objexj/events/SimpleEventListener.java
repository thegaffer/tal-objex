package org.talframework.objexj.events;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class implements the event listener interface that
 * is suitable for use in a configuration. Typically for
 * registered listeners a runtime environment will have its
 * own version of this object which is persisted.
 * 
 * @author Tom Spencer
 */
public final class SimpleEventListener implements EventListener, Serializable {
    private static final long serialVersionUID = 1L;

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
    public SimpleEventListener() {
    }
    
    /**
     * Helper constructor for one-line construction. ETA is not
     * included as this is specific only to transaction specific
     * listeners. Use the setter for that.
     * 
     * @param container The container to send event to
     * @param eventProcessor The processor on that container
     * @param onCreation If true creation events are sent
     * @param onStateChange If true state change events are sent
     * @param interestedStates The custom state changes to send events on
     * @param onEdit If true all edit cause event emission
     * @param channel The channel to send the event on
     * @param delay The delay to introduce (platform specific)
     */
    public SimpleEventListener(
            String container, 
            String eventProcessor, 
            boolean onCreation, 
            boolean onStateChange, 
            String[] interestedStates, 
            boolean onEdit, 
            String channel, 
            long delay) {
        this.container = container; 
        this.eventProcessor = eventProcessor; 
        this.onCreation = onCreation;
        this.onStateChange = onStateChange; 
        this.interestedStates = interestedStates; 
        this.onEdit = onEdit;
        this.channel = channel; 
        this.delay = delay;
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
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EventListener [channel=" + channel + ", container=" + container + ", delay=" + delay + ", eta=" + eta + ", eventProcessor="
                + eventProcessor + ", interestedStates=" + Arrays.toString(interestedStates) + ", onCreation=" + onCreation + ", onEdit=" + onEdit
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
        SimpleEventListener other = (SimpleEventListener)obj;
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
        if( !Arrays.equals(interestedStates, other.interestedStates) ) return false;
        if( onCreation != other.onCreation ) return false;
        if( onEdit != other.onEdit ) return false;
        if( onStateChange != other.onStateChange ) return false;
        return true;
    }
}
