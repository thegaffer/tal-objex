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

package org.talframework.objexj;

/**
 * This interface extends {@link Container} to represent
 * a Store. A store is a fixed or named collection of 
 * objects. It is very often used to hold index records
 * of documents - although it can exist in its own right.
 * In a business sense if your quotes, tenders and sales
 * are documents, then your stock list is your store.
 * 
 * @author Tom Spencer
 */
public interface Store extends Container {

}
