package org.talframework.objexj.runtime.globals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

import com.intersys.globals.Connection;
import com.intersys.globals.NodeReference;

/**
 * This class tests the globals writer
 *
 * @author Tom Spencer
 */
public class TestGlobalsWriter {
    
    private Mockery context = new JUnit4Mockery();
    private Connection connection = null;
    private NodeReference node = null;
    
    @Before
    public void setup() {
        this.connection = context.mock(Connection.class);
        this.node = context.mock(NodeReference.class);
    }
    
    /**
     * Tests the main conditions that we call node reference as expected
     */
    @Test
    public void basic() {
        GlobalsObjectWriter writer = new GlobalsObjectWriter(connection, node);
        final Date today = new Date();
        
        context.checking(new Expectations() {{
            oneOf(node).set("val", "name");
            oneOf(node).set((double)1.0, "dbl");
            oneOf(node).set((long)1, "long");
            oneOf(node).set((int)1, "int");
            oneOf(node).set(today.getTime(), "time");
        }});
        
        writer.write("name", "val", ObjexFieldType.STRING, true);
        writer.write("dbl", (double)1.0, ObjexFieldType.NUMBER, true);
        writer.write("long", (long)1, ObjexFieldType.NUMBER, true);
        writer.write("int", (int)1, ObjexFieldType.NUMBER, true);
        writer.write("time", today, ObjexFieldType.DATE, true);
    }

    /**
     * Ensures no Globals method called if not persistent
     */
    @Test
    public void ignoreIfNotPersistent() {
        GlobalsObjectWriter writer = new GlobalsObjectWriter(connection, node);
        writer.write("name", "val", ObjexFieldType.STRING, false);
        writer.writeReference("name", "123", ObjexFieldType.OWNED_REFERENCE, false);
        writer.writeReferenceList("name", new ArrayList<String>(), ObjexFieldType.REFERENCE, false);
        writer.writeReferenceMap("name", new HashMap<String, String>(), ObjexFieldType.REFERENCE, false);
    }
}
