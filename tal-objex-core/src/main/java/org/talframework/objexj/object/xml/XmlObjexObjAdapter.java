package org.talframework.objexj.object.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.talframework.objexj.ObjexObj;

/**
 * This is the JAXB XML Adaptor for a unknown type. See
 * {@link XmlObjexObj} for more information.
 *
 * @author Tom Spencer
 */
public class XmlObjexObjAdapter extends XmlAdapter<XmlObjexObj, ObjexObj> {
    public ObjexObj unmarshal(XmlObjexObj val) throws Exception {
        return val.realObject;
    }
    
    public XmlObjexObj marshal(ObjexObj val) throws Exception {
        return new XmlObjexObj(val);
    }
}
