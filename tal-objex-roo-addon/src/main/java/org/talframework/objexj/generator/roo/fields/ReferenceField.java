package org.talframework.objexj.generator.roo.fields;

/**
 * Base class for all reference fields holding common
 * attributes.
 *
 * @author Tom Spencer
 */
public abstract class ReferenceField extends ObjexField {

    private String newType = null;
    private boolean owned = true;
    
    public ReferenceField(boolean naturalBeanField) {
        super(naturalBeanField);
    }
    
    /**
     * @return the newType
     */
    public String getNewType() {
        return newType;
    }

    /**
     * Setter for the newType field
     *
     * @param newType the newType to set
     */
    public void setNewType(String newType) {
        this.newType = newType;
    }

    /**
     * @return the owned
     */
    public boolean isOwned() {
        return owned;
    }

    /**
     * Setter for the owned field
     *
     * @param owned the owned to set
     */
    public void setOwned(boolean owned) {
        this.owned = owned;
    }
    
    /**
     * Helper to work out a natural name for the reference
     * property getter/setter
     * 
     * @return The reference property name
     */
    public abstract String getReferencePropName();
}
