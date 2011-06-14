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
package org.talframework.objexj.generator.roo.fields;

/**
 * Base class for all reference fields holding common
 * attributes.
 *
 * @author Tom Spencer
 */
public abstract class ReferenceField extends ObjexField {

    private String newType = null;
    private boolean owned = true;
    
    public ReferenceField(boolean naturalBeanField) {
        super(naturalBeanField);
        setObjexType("ObjexFieldType.OWNED_REFERENCE");
    }
    
    /**
     * @return the newType
     */
    public String getNewType() {
        return newType;
    }

    /**
     * Setter for the newType field
     *
     * @param newType the newType to set
     */
    public void setNewType(String newType) {
        this.newType = newType;
    }
    
    /**
     * @return Returns of the field is a collection (list or map) of references
     */
    public abstract boolean isCollection();

    /**
     * @return the owned
     */
    public boolean isOwned() {
        return owned;
    }

    /**
     * Setter for the owned field
     *
     * @param owned the owned to set
     */
    public void setOwned(boolean owned) {
        this.owned = owned;
        
        if( owned ) setObjexType("ObjexFieldType.OWNED_REFERENCE");
        else setObjexType("ObjexFieldType.REFERENCE");
    }
    
    /**
     * Helper to work out a natural name for the reference
     * property getter/setter
     * 
     * @return The reference property name
     */
    public abstract String getReferencePropName();
}
