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

package org.talframework.objexj.sample.api.order;

import java.util.List;

/**
 * The business interface to an order
 * 
 * @author Tom Spencer
 */
public interface Order {
    
    /**
     * @return the account
     */
    public abstract long getAccount();

    /**
     * @param account the account to set
     */
    public abstract void setAccount(long account);

    /**
     * @return A reference to each item in the order
     */
    public abstract List<OrderItem> getItems();
    
    /**
     * @return A reference to each item in the order
     */
    public abstract List<String> getItemRefs();
    
    /**
     * Helper to get an order item by ID
     * 
     * @param id The ID
     */
    public abstract OrderItem getItemById(Object id);
    
    public abstract OrderItem createItem();
	
	public abstract void removeItemById(Object id);
}
