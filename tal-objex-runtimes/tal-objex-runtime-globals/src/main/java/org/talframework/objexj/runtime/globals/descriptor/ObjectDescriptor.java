package org.talframework.objexj.runtime.globals.descriptor;

import java.util.Map;

import org.talframework.objexj.container.ObjectStrategy;

/**
 * This class contains a globals specific description of any class within
 * the document type holding it. The descriptions are calculated automatically
 * and saved in the globals db. The main reason is to support using value
 * lists in nodes (because it is quicker), but not burden the app with defining.
 * At the same time though we need to preserve existing data if it exists.
 *
 * @author Tom Spencer
 */
public class ObjectDescriptor {
    
    public static enum FieldStorageType {
        /** Indicates there is no known storage for this field */
        NONE,
        /** Indicates field held as a simple field */
        SIMPLE,
        /** Indicates field held in it's own node */
        NODE
    }

    /** Holds the type of object we describe */
    private String type;
    /** Holds all the fields globals storage type, keyed by name */
    private Map<String, FieldStorageType> fields;
    /** Holds the simple field indexes, keyed by field */
    private Map<String, Integer> simpleFields;
    /** Holds the highest field index we have (which may or may not be real!) */
    private int highestSimpleField;
    
    public ObjectDescriptor(String documentType, ObjectStrategy strategy) {
        // a. load existing from globals
        
        // b. Update with strategy
                
        // c. Save
    }
    
    private void retrieve(String documentType, String objectType) {
        
    }
    
    private void store(String documentType, String objectType) {
        
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Setter for the type field
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the fields
     */
    public Map<String, FieldStorageType> getFields() {
        return fields;
    }
    
    /**
     * Simple method to get the storage type of a field.
     * 
     * @param field The field
     * @return Its storage type
     */
    public FieldStorageType getStorageType(String field) {
        FieldStorageType ret = FieldStorageType.NONE;
        if( fields != null ) ret = fields.get(field);
        return ret;
    }

    /**
     * Setter for the fields field
     *
     * @param fields the fields to set
     */
    public void setFields(Map<String, FieldStorageType> fields) {
        this.fields = fields;
    }

    /**
     * @return the simpleFields
     */
    public Map<String, Integer> getSimpleFields() {
        return simpleFields;
    }
    
    /**
     * Simple helper to get the index for the field
     * 
     * @param field The field to get the index for
     * @return The index of this field (or -2 if not indexed or not known)
     */
    public int getFieldIndex(String field) {
        int ret = -1;
        if( simpleFields != null && simpleFields.containsKey(field) ) ret = simpleFields.get(field);
        return ret;
    }

    /**
     * Setter for the simpleFields field
     *
     * @param simpleFields the simpleFields to set
     */
    public void setSimpleFields(Map<String, Integer> simpleFields) {
        this.simpleFields = simpleFields;
    }

    /**
     * @return the highestSimpleField
     */
    public int getHighestSimpleField() {
        return highestSimpleField;
    }

    /**
     * Setter for the highestSimpleField field
     *
     * @param highestSimpleField the highestSimpleField to set
     */
    public void setHighestSimpleField(int highestSimpleField) {
        this.highestSimpleField = highestSimpleField;
    }
}
