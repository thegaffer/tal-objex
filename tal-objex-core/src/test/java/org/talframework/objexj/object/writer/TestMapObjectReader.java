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
package org.talframework.objexj.object.writer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;

public class TestMapObjectReader {

    private Mockery context = new JUnit4Mockery();

    @Test
    public void basic() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("name", "Name");
        props.put("description", "Description");
        props.put("stockLevel", (int)20);
        props.put("price", 50.0);
        
        final ObjexObj obj = context.mock(ObjexObj.class);
        final MapObjectReader underTest = new MapObjectReader();
        
        context.checking(new Expectations() {{
            oneOf(obj).acceptReader(underTest);
        }});
        
        underTest.readObject(props, obj);
        
        Assert.assertEquals("Name", underTest.read("name", "Something", String.class, ObjexFieldType.STRING, true));
        Assert.assertEquals("Description", underTest.read("description", "Something", String.class, ObjexFieldType.STRING, true));
        Assert.assertEquals((int)20, (int)underTest.read("stockLevel", 15, Integer.class, ObjexFieldType.NUMBER, true));
        Assert.assertEquals(50.0, underTest.read("price", 18.0, Double.class, ObjexFieldType.NUMBER, true));
        context.assertIsSatisfied();
    }
    
    @Test
    public void merge() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("description", "Merge This");
        props.put("stockLevel", (int)27);
        
        final ObjexObj obj = context.mock(ObjexObj.class);
        final MapObjectReader underTest = new MapObjectReader();
        underTest.setBehaviourOn(ObjectReaderBehaviour.MERGE);
        
        context.checking(new Expectations() {{
            oneOf(obj).acceptReader(underTest);
        }});
        
        underTest.readObject(props, obj);
        
        Assert.assertEquals("Something", underTest.read("name", "Something", String.class, ObjexFieldType.STRING, true));
        Assert.assertEquals("Merge This", underTest.read("description", "Something", String.class, ObjexFieldType.STRING, true));
        Assert.assertEquals((int)27, (int)underTest.read("stockLevel", 15, Integer.class, ObjexFieldType.NUMBER, true));
        Assert.assertEquals(18.0, underTest.read("price", 18.0, Double.class, ObjexFieldType.NUMBER, true));
        context.assertIsSatisfied();
    }
}
