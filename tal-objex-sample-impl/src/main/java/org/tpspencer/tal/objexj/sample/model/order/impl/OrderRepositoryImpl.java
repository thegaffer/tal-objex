package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {
	
	private Container container;
	
	public OrderRepositoryImpl(Container container) {
		this.container = container;
	}
	
	public String getId() {
	    return container.getId();
	}

	public Order getOrder() {
		return container.getRootObject().getBehaviour(Order.class);
	}
	
	public void open() {
	    container = container.openContainer();
	}
	
	public String suspend() {
		return container.suspend();
	}
	
	public void persist() {
		container.saveContainer();
	}
	
	public void abort() {
		container.closeContainer();
	}
}
