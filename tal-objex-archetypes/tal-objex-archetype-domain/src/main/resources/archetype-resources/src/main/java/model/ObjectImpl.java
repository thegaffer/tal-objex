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
package ${package}.model;

import org.talframework.objexj.annotations.ObjexObj;
import ${package}.beans.ObjectBean;

/**
 * This is a sample ObjexObj implementation class. Either
 * rename it and point to appropriate state bean or use
 * it as a template for other beans.
 * 
 * <p>Note it is good practice to implement a non-Objex
 * exposing business interface from this object. Which
 * is not shown in the sample.</p>
 * 
 * TODO: Amend this Javadoc to describe actual business object & replace author
 *
 * @author Tom Spencer
 */
@ObjexObj(ObjectBean.class)
public class ObjectImpl {
    
    /** Member holds the state bean for this object */
    private final ObjectBean bean;
    
    /**
     * Basic constructor for the object taking in the 
     * current value of the state bean.
     */
    public ObjectImpl(ObjectBean bean) {
        this.bean = bean;
    }
    
}
