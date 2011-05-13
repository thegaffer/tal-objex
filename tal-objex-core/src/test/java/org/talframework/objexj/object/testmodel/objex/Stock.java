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

package org.talframework.objexj.object.testmodel.objex;

import java.util.List;

import javax.validation.constraints.Size;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexStateReader;
import org.talframework.objexj.ObjexStateWriter;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.testmodel.api.ICategory;
import org.talframework.objexj.object.testmodel.api.IStock;

/**
 * This object implements our test Stock interface. This is also an
 * example of how you can directly support Objex by implementing the
 * {@link ObjexObj} interface via {@link BaseObjexObj} - there are
 * generators that make this even easier. Although we override a
 * couple of methods from the base class for efficiency, more could
 * be overridden.
 *
 * @author Tom Spencer
 */
public class Stock extends BaseObjexObj implements IStock {
    
    @Size(min=1)
	private List<ICategory> categories = null;
	
	public Stock() {
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// Overridden BaseObjexObj (for efficiency and so we don't supply a strategy)
	
	@Override
	public void acceptReader(ObjexStateReader reader) {
	    setParentId(reader.read("parentId", getParentId(), ObjexID.class, ObjexFieldType.PARENT_ID, true));
	    
	    categories = reader.readReferenceList("categories", categories, ICategory.class, ObjexFieldType.OWNED_REFERENCE, true);
	}
	
	@Override
	public void acceptWriter(ObjexStateWriter writer, boolean includeNonPersistent) {
	    writer.writeReferenceList("categories", categories, ObjexFieldType.OWNED_REFERENCE, true);
	}
	
	/////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Stock#getCategories()
     */
    @Override
    public List<ICategory> getCategories() {
        return categories;
    }

    /* (non-Javadoc)
     * @see org.talframework.objexj.object.testmodel.pojo.Stock#setCategories(java.util.List)
     */
    @Override
    public void setCategories(List<ICategory> categories) {
        this.categories = categories;
    }
}
