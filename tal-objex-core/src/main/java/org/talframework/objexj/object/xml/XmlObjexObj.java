package org.talframework.objexj.object.xml;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.talframework.objexj.ObjexObj;

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
@XmlJavaTypeAdapter(XmlObjexObjAdapter.class)
public class XmlObjexObj {

    @XmlAnyElement(lax=true)
    public ObjexObj realObject;
    
    public XmlObjexObj() {
    }
    
    public XmlObjexObj(ObjexObj obj) {
        this.realObject = obj;
    }
}
