package org.talframework.objexj;

/**
 * This interface extends {@link Container} to represent a
 * document. A document is a specific instance of a document
 * type, there can be many instances (or documents) or the
 * same type. A document also has a lifecycle, that typically
 * goes to a point where the document is not editable any 
 * may be archived or removed.
 * 
 * @author Tom Spencer
 */
public interface Document extends Container {

}
