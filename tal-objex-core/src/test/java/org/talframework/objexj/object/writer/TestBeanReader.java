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

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.object.testbeans.ProductBean;

/**
 * This class tests the bean reader
 *
 * @author Tom Spencer
 */
public class TestBeanReader {
    
    private Mockery context = new JUnit4Mockery();

    @Test
    public void basic() {
        ProductBean bean = new ProductBean("Name", "Description", 20, 50);
        
        final ObjexObj obj = context.mock(ObjexObj.class);
        final BeanReader underTest = new BeanReader();
        
        context.checking(new Expectations() {{
            oneOf(obj).acceptReader(underTest);
        }});
        
        underTest.readObject(bean, obj);
        
        Assert.assertEquals("Name", underTest.read("name", "Something", String.class, ObjexFieldType.STRING, true));
        Assert.assertEquals("Description", underTest.read("description", "Something", String.class, ObjexFieldType.STRING, true));
        Assert.assertEquals((int)20, (int)underTest.read("stockLevel", 15, Integer.class, ObjexFieldType.NUMBER, true));
        Assert.assertEquals(50.0, underTest.read("price", 18.0, Double.class, ObjexFieldType.NUMBER, true));
        context.assertIsSatisfied();
    }
}
