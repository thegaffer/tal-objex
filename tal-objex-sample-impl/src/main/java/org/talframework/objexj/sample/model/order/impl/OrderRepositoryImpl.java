/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
