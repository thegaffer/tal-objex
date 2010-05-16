package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.sample.api.order.OrderSummary;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;
import org.tpspencer.tal.objexj.sample.api.repository.OrderService;

/**
 * This class implements the order service
 * 
 * @author Tom Spencer
 */
public class OrderServiceImpl implements OrderService {
	
	/** Holds the Objex factory for the order container type */
	private ContainerFactory locator;

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
	public OrderRepository getOpenRepository(String id, String transactionId) {
		EditableContainer container = locator.get(id, transactionId);
		if( container == null ) throw new IllegalArgumentException("Container or transaction does not exist: " + id + ", " + transactionId);
		return new OrderRepositoryImpl(container);
	}
	
	public OrderRepository createNewOrder() {
	    // TODO: Need to generate a new ID properly!?!
	    String id = Long.toString(System.currentTimeMillis());
	    return new OrderRepositoryImpl(locator.open(id, false));
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
