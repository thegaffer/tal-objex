/*
 * Copyright 2009 Thomas Spencer
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

package org.talframework.objexj.object.writer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;

public class TestPropertyReader {

    private Mockery context = new JUnit4Mockery();

    @Test
    public void basic() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("name", "Name");
        props.put("description", "Description");
        props.put("stockLevel", (int)20);
        props.put("price", 50.0);
        
        final ObjexObj obj = context.mock(ObjexObj.class);
        final PropertyReader underTest = new PropertyReader();
        
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
        final PropertyReader underTest = new PropertyReader();
        underTest.setMerge(true);
        
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
