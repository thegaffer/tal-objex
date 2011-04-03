package org.talframework.objexj.runtime.globals;

import org.junit.Test;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexStateReader;

import junit.framework.Assert;

/**
 * This class tests the reader class that reads an object from the globals
 * db into a statebean.
 *
 * @author Tom Spencer
 */
public class TestGlobalsReader {

    @Test
    public void basic() {
        ObjexStateReader reader = new GlobalsObjectReader(null);
        
        Assert.assertEquals("", reader.read("test", "val", String.class, ObjexObjStateBean.ObjexFieldType.STRING, true));
        Assert.assertEquals(null, reader.read("test", null, Long.class, ObjexObjStateBean.ObjexFieldType.NUMBER, true));
    }
}
