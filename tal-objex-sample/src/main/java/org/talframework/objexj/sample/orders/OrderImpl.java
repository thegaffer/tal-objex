package org.talframework.objexj.sample.orders;

import java.util.List;

/**
 * This class is our Order business object implementation in the sample.
 *
 * @author Tom Spencer
 */
public class OrderImpl implements Order {

    private long account;
    private List<OrderItem> items;
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#getAccount()
     */
    @Override
    public long getAccount() {
        return account;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#setAccount(long)
     */
    @Override
    public void setAccount(long account) {
        this.account = account;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#getItems()
     */
    @Override
    public List<OrderItem> getItems() {
        return items;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#setItems(java.util.List)
     */
    @Override
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
