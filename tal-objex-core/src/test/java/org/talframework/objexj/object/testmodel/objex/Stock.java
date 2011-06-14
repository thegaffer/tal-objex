/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
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
