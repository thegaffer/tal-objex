package org.talframework.objexj.runtime.globals.descriptor;

import java.util.Set;

/**
 * This internal class describes a document type. This is calculated when the globals
 * runtime first see's the document (or container type). It is saved in the globals
 * datastore and retrieved for the future (although it is checked for differences).
 * This is really there so we can change the structure of our objects easily, but
 * still benefit from the increased performance of the value list.
 *
 * @author Tom Spencer
 */
public class DocumentDescriptor {

    /** Member holds the type of container */
    private String type;
    /** Member holds a definition of each object */
    private Set<ObjectDescriptor> objects;
}
