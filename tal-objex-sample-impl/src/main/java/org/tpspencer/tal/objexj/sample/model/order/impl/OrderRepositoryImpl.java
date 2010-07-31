package org.tpspencer.tal.objexj.sample.model.order.impl;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {
	
	private final Container container;
	private EditableContainer editableContainer;
	
	public OrderRepositoryImpl(Container container) {
		this.container = container;
		this.editableContainer = null;
	}
	
	public OrderRepositoryImpl(EditableContainer container) {
		this.container = container;
		this.editableContainer = container;
	}
	
	public String getId() {
	    return editableContainer != null ? editableContainer.getId() : container.getId();
	}

	public Order getOrder() {
		Container c = editableContainer != null ? editableContainer : container;
		return c.getRootObject().getBehaviour(Order.class);
	}
	
	public void open() {
		if( editableContainer != null ) throw new IllegalArgumentException("The container is already open: " + container.getId());
		
		editableContainer = container.openContainer();
	}
	
	public String suspend() {
		if( editableContainer == null ) throw new IllegalArgumentException("Cannot suspend a container that is not open: " + container.getId());
		return editableContainer.suspend();
	}
	
	public void persist() {
		if( editableContainer == null ) throw new IllegalArgumentException("Cannot persist a container that is not open: " + container.getId());
		editableContainer.saveContainer();
	}
	
	public void abort() {
		if( editableContainer == null ) throw new IllegalArgumentException("Cannot abort a container that is not open: " + container.getId());
		editableContainer.closeContainer();
	}
}
