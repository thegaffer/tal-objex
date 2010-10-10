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

package org.talframework.objexj.object.testbeans;

import java.util.List;

import javax.validation.constraints.Size;

import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;

/**
 * A test bean representing all of companies stock items
 * that allows us to test elements of Objex
 *
 * @author Tom Spencer
 */
public class StockBean extends BaseTestBean {
    private static final long serialVersionUID = 1L;
    
    @Size(min=1)
	private List<String> categories = null;
	
	public StockBean() {
	}
	
	public void acceptReader(ObjexStateReader reader) {
        categories = reader.readReferenceList("categories", ObjexFieldType.OWNED_REFERENCE, true);
    }
    
    public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
        writer.writeReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
    }

    /**
     * @return the categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Setter for the categories field
     *
     * @param categories the categories to set
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
