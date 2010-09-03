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
 * This interface extends {@link Container} to represent a
 * document. A document is a specific instance of a document
 * type, there can be many instances (or documents) or the
 * same type. A document also has a lifecycle, that typically
 * goes to a point where the document is not editable any 
 * may be archived or removed.
 * 
 * @author Tom Spencer
 */
public interface Document extends Container {

}
