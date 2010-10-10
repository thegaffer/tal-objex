package org.talframework.objexj;

import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

/**
 * This interface is intended to all a client to get the objects
 * to write out their state as it currently stands. The class
 * implementing this interface may be writing the state to a
 * stream, an XML file, a custom file or some other persistence
 * mechanism. 
 * 
 * TODO: Not sure about this?!?
 *
 * @author Tom Spencer
 */
public interface ObjexStateWriter {

    /**
     * Writes out the given property as a simple property
     * 
     * @param name The name of the proeprty
     * @param obj The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void write(String name, Object obj, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReference(String name, String ref, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference list property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReferenceList(String name, List<String> ref, ObjexFieldType type, boolean persistent);
    
    /**
     * Writes out the given reference map property
     * 
     * @param name The name of the property
     * @param ref The value
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     */
    public void writeReferenceMap(String name, Map<String, String> ref, ObjexFieldType type, boolean persistent);
}
