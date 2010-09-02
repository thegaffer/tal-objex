package org.talframework.objexj.exceptions;

/**
 * Indicates that an attempt to load a transaction failed. This
 * might be a programmatic error because the transaction did not
 * exist, or it might be because the transaction has expired as
 * transactions have a lifetime.
 * 
 * @author Tom Spencer
 */
public class TransactionNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final String id;
    
    public TransactionNotFoundException(String id) {
        super();
        this.id = id;
    }
    
    public TransactionNotFoundException(String id, Exception cause) {
        super(cause);
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Transaction [" + id + "] not found";
    }
}
