package org.talframework.objexj.sample.model.order.impl;

import org.talframework.objexj.Container;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.object.ObjectStrategy;
import org.talframework.objexj.object.SimpleObjectStrategy;
import org.talframework.objexj.sample.api.order.OrderSummary;
import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.api.repository.OrderService;
import org.talframework.objexj.sample.beans.order.OrderBean;
import org.talframework.objexj.sample.beans.order.OrderItemBean;

/**
 * This class implements the order service
 * 
 * @author Tom Spencer
 */
public class OrderServiceImpl implements OrderService {
    public static final ObjectStrategy ORDER_STRATEGY = new SimpleObjectStrategy("Order", OrderImpl.class, OrderBean.class);
    public static final ObjectStrategy ITEM_STRATEGY = new SimpleObjectStrategy("OrderItem", OrderItemImpl.class, OrderItemBean.class);
    
    /** Holds the Objex factory for the order container type */
	private ContainerFactory locator;
	
	public OrderServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    public OrderServiceImpl(ContainerMiddlewareFactory middlewareFactory) {
        ObjectStrategy[] strategies = new ObjectStrategy[]{ORDER_STRATEGY, ITEM_STRATEGY};
        ContainerStrategy strategy = new SimpleContainerStrategy("Order", "Order", strategies);
        
        locator = new SimpleContainerFactory(strategy, middlewareFactory);
    }

	/**
	 * Simply gets the container from the container factory
	 * and wraps it inside a new {@link OrderRepositoryImpl}
	 * instance.
	 */
	public OrderRepository getRepository(String id) {
		Container container = locator.get(id);
		if( container == null ) throw new IllegalArgumentException("Container does not exist: " + id);
		return new OrderRepositoryImpl(container);
	}
	
	/**
	 * Simply gets the container from the container factory
	 * and wraps it inside a new {@link OrderRepositoryImpl}
	 * instance. 
	 */
	public OrderRepository getOpenRepository(String id) {
		Container container = locator.open(id);
		if( container == null ) throw new IllegalArgumentException("Container or transaction does not exist: " + id);
		return new OrderRepositoryImpl(container);
	}
	
	public OrderRepository createNewOrder() {
	    Container container = locator.create();
	    
	    return new OrderRepositoryImpl(container);
	}
	
	/**
	 * TODO: Implement search across docs using doc store object
	 */
	public OrderSummary[] findOrdersByAccount(String accountId) {
	    throw new UnsupportedOperationException("Search across docs not yet implemented");
	}
	
	/**
	 * TODO: Implement search across docs
	 */
	public OrderSummary[] findOrdersByStockItem(String stockItemId) {
	    throw new UnsupportedOperationException("Search across docs not yet implemented");
	}

	/**
	 * @return the locator
	 */
	public ContainerFactory getLocator() {
		return locator;
	}

	/**
	 * @param locator the locator to set
	 */
	public void setLocator(ContainerFactory locator) {
		this.locator = locator;
	}
}
