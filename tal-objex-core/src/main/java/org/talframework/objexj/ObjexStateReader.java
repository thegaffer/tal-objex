package org.talframework.objexj;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj.ObjexFieldType;

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
     * <p>Note: That unlike the writer the parentId will be read though
     * a call to this method.</p>
     * 
     * @param name The name of the property
     * @param expected The expected type (basically T) - this is both for type safety and to allow sensible conversion
     * @param type The subtle Objex field type
     * @param persistent Indicates the field is considered persistent
     * @return The value
     */
    public <T> T read(String name, T current, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference property
     * 
     * @param name The name of the property
     * @param current The current value of the property
     * @param expected The expected (interface) type (basically T) - this is to ensure type safety
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference
     */
    public <T> T readReference(String name, T current, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference list property
     * 
     * @param name The name of the property
     * @param current The current value of the property
     * @param expected The expected (interface) type of the members (basically T) - this is to ensure type safety
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference list
     */
    public <T> List<T> readReferenceList(String name, List<T> current, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference set property
     * 
     * @param name The name of the property
     * @param current The current value of the property
     * @param expected The expected (interface) type of the members (basically T) - this is to ensure type safety
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference set
     */
    public <T> Set<T> readReferenceSet(String name, Set<T> current, Class<?> expected, ObjexFieldType type, boolean persistent);
    
    /**
     * Call to read the given reference map property
     * 
     * @param name The name of the property
     * @param current The current value of the property
     * @param expected The expected (interface) type of the members (basically T) - this is to ensure type safety
     * @param type The field type
     * @param persistent Indicates the field is considered persistent
     * @return The reference map
     */
    public <T> Map<String, T> readReferenceMap(String name, Map<String, T> current, Class<?> expected, ObjexFieldType type, boolean persistent);
}
