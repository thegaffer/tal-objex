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
package org.talframework.objexj.runtime.globals;


/**
 * This class tests the globals writer
 *
 * @author Tom Spencer
 */
public class TestGlobalsWriter {
    
    /*private Mockery context = new JUnit4Mockery();
    private Connection connection = null;
    private NodeReference node = null;
    
    @Before
    public void setup() {
        this.connection = context.mock(Connection.class);
        this.node = context.mock(NodeReference.class);
    }
    
    *//**
     * Tests the main conditions that we call node reference as expected
     *//*
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

    *//**
     * Ensures no Globals method called if not persistent
     *//*
    @Test
    public void ignoreIfNotPersistent() {
        GlobalsObjectWriter writer = new GlobalsObjectWriter(connection, node);
        writer.write("name", "val", ObjexFieldType.STRING, false);
        writer.writeReference("name", "123", ObjexFieldType.OWNED_REFERENCE, false);
        writer.writeReferenceList("name", new ArrayList<String>(), ObjexFieldType.REFERENCE, false);
        writer.writeReferenceMap("name", new HashMap<String, String>(), ObjexFieldType.REFERENCE, false);
    }*/
}
