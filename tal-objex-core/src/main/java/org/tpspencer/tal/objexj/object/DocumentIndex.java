package org.tpspencer.tal.objexj.object;

import java.util.Date;

/**
 * This interface represents the object that is the master index
 * record for a document container. This object exists inside the
 * primary store for the document type.
 * 
 * <p>It is not absolutely neccessary to use this interface in 
 * all types of runtime environment, but it does give an idea of
 * what is expected of a documents index object.
 * 
 * @author Tom Spencer
 */
public interface DocumentIndex {

    /**
     * @return The ID of the document
     */
    public String getDocumentId();
    
    /**
     * Call to get the name of the document. It is a good idea to name
     * the documents in some way for human consumption. Consider this
     * the label of the file if it were in a suspended file. This name
     * does not need to be unique and is very often a reference number
     * (i.e. account number).
     * 
     * @return The name of the document
     */
    public String getName();
    
    /**
     * The type of the document. Although this is typically also inside
     * the ID of the document it is useful to have the type here so it
     * can be seen.
     * 
     * @return The type of document
     */
    public String getType();
    
    /**
     * Call to get a description of the document. It is a good idea
     * to provide a human-readable description of the document. This
     * may be formed from the contents of the document when it is
     * saved.
     * 
     * @return A description of this document
     */
    public String getDescription();
    
    /**
     * Call to get the status of the document. Documents typically
     * have a lifecycle state so its good to keep this inside the
     * index so at a glance it can be seen. 
     * 
     * @return The status of the document
     */
    public String getStatus();
    
    /**
     * @return The user id of the person who created the doc initially (if known)
     */
    public String getAuthor();
    
    /**
     * @return The date the document was first created
     */
    public Date getCreated();
    
    /**
     * @return The user id of the person who last updated the doc (if known)
     */
    public String getLastModifier();
    
    /**
     * @return The date/time the document was last updated
     */
    public Date getUpdated();
    
    /**
     * Call to get a transaction id for the document. Through
     * this method the container can exercise control over
     * whether multiple transactions are even possible.
     * 
     * TODO: Should this throw a checked exception of type already locked?
     * 
     * @return The ID of the new transaction
     */
    public String getTransactionId();
    
    /**
     * Called when the transaction has ended.
     * 
     * @param id The transaction id that has ended
     */
    public void clearTransactionId(String id);
}
