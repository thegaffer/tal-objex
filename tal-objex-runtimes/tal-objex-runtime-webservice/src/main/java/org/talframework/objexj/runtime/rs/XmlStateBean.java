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
package org.talframework.objexj.runtime.rs;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.ObjexObjStateBean;

/**
 * This class exists to allow better XML documents. Often in Objex
 * we don't know the exact type of object we are holding in a
 * reference property. Due to JAXB this is very ugly if we simply
 * use the XMLAnyElement notation because it names the elements
 * after the actual type held, this is even worse in JSON where
 * to read it you would have to know the type in advance.
 * 
 * <p>This class aims to solve that by providing a wrapper that
 * can be used. So you configure the reference attribute to use
 * this class, which then holds the real object.</p>
 *
 * @author Tom Spencer
 */
@XmlType(name="object")
@XmlJavaTypeAdapter(XmlStateBeanAdapter.class)
public class XmlStateBean {

    @XmlAnyElement(lax=true)
    public ObjexObjStateBean realObject;
    
    public XmlStateBean() {
    }
    
    public XmlStateBean(ObjexObjStateBean obj) {
        this.realObject = obj;
    }
}
