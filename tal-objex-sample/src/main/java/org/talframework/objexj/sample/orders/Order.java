package org.talframework.objexj.sample.orders;

import java.util.List;

/**
 * This interface represents an Order in our sample project. This interface
 * is exposed to the outside world. Also differences between this interface
 * and our implementation object tell Objex how to treat the object.
 *
 * @author Tom Spencer
 */
public interface Order {

    /**
     * @return the account
     */
    public abstract long getAccount();

    /**
     * Setter for the account field
     *
     * @param account the account to set
     */
    public abstract void setAccount(long account);

    /**
     * @return the items
     */
    public abstract List<OrderItem> getItems();

    /**
     * Setter for the items field
     *
     * @param items the items to set
     */
    public abstract void setItems(List<OrderItem> items);

}