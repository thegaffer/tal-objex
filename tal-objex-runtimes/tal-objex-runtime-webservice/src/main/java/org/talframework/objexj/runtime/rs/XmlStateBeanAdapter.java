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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.talframework.objexj.ObjexObjStateBean;

/**
 * This is the JAXB XML Adaptor for a unknown type. See
 * {@link XmlStateBean} for more information.
 *
 * @author Tom Spencer
 */
public class XmlStateBeanAdapter extends XmlAdapter<XmlStateBean, ObjexObjStateBean> {
    public ObjexObjStateBean unmarshal(XmlStateBean val) throws Exception {
        return val.realObject;
    }
    
    public XmlStateBean marshal(ObjexObjStateBean val) throws Exception {
        return new XmlStateBean(val);
    }
}
