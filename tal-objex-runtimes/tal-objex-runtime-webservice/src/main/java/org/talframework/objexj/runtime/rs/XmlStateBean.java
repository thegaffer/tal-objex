/*
 * Copyright 2010 Thomas Spencer
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
