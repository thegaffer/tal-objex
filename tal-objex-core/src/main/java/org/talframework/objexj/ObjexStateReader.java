package org.talframework.objexj;

import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

/**
 * This interface is intended to all a client to get the objects
 * to read in their state from some source. The implementing class
 * may be reading an XML file, a property file, or other
 * persistence mechanism.
 * 
 * TODO: Not sure about this?!?
 *
 * @author Tom Spencer
 */
public interface ObjexStateReader {

    /**
     * Call to read the given property as the expected type.
     * 
     * @param name The name of the property
     * @param expected The expected type
     * @param type The subtle Objex field type
     * @param persistent Indicates the field is considered persistent
     * @return The value
     */
    public <T> T read(String name, T current, Class<T> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference property
     * 
     * @param name The name of the property
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference
     */
    public String readReference(String name, String current, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference list property
     * 
     * @param name The name of the property
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference list
     */
    public List<String> readReferenceList(String name, List<String> current, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference map property
     * 
     * @param name The name of the property
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference map
     */
    public Map<String, String> readReferenceMap(String name, Map<String, String> current, ObjexFieldType type, boolean persistent);
}
