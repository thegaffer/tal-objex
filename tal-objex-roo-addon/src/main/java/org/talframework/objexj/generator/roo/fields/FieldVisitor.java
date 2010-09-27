package org.talframework.objexj.generator.roo.fields;

/**
 * This interface represents something that can visit each
 * type of property. We are using the visitor pattern for
 * extensibility, but the main purpose is for the generation
 * of the methods on the properties.
 *
 * @author Tom Spencer
 */
public interface FieldVisitor {

    /**
     * Call for the derived visitor to visit the simple
     * property.
     * 
     * @param prop The property
     */
    public void visitSimple(SimpleField prop);
    
    /**
     * Call for the derived visitor to visit the simple
     * reference property.
     * 
     * @param prop The property
     */
    public void visitReference(SimpleReferenceField prop);
    
    /**
     * Call for the derived visitor to visit the list
     * reference property.
     * 
     * @param prop The property
     */
    public void visitList(ListReferenceField prop);
    
    /**
     * Call for the derived visitor to visit the map
     * reference property.
     * 
     * @param prop The property
     */
    public void visitMap(MapReferenceField prop);
}
