package org.talframework.objexj.sample.model.order.impl;

import org.talframework.objexj.Container;
import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.api.repository.OrderRepository;

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
