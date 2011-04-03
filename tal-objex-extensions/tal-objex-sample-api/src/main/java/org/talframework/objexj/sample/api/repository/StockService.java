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

package org.talframework.objexj.sample.api.repository;

/**
 * This service provides access to the stock repository.
 * There is only 1 stock repository, but it is possible
 * to have
 * 
 * @author Tom Spencer
 */
public interface StockService {

    /**
     * @return A read-only repository for the stock store
     */
	public StockRepository getRepository();
	
	/**
	 * @return An editable repository for the stock store
	 */
	public StockRepository getOpenRepository();
	
	/**
	 * @param id The existing transaction ID
	 * @return The open stock repository
	 */
	public StockRepository getOpenRepository(String id);
	
	/**
	 * Ensures the stock container has been created
	 */
	public void ensureCreated();
}
