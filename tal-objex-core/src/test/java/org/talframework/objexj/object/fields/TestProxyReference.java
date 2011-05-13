package org.talframework.objexj.object.fields;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;

/**
 * This class tests the proxy reference field
 *
 * @author Tom Spencer
 */
public class TestProxyReference {
    
    private Mockery context = new JUnit4Mockery();

    /**
     * Simple test to ensure we do not get the object for the simple methods
     */
    @Test
    public void basic() {
        final Container container = context.mock(Container.class);
        
        ProxyReference ref = new ProxyReference(new DefaultObjexID("test", 123), container);
        
        Assert.assertEquals(new DefaultObjexID("test", 123), ref.getId());
        Assert.assertEquals("test", ref.getType());
        Assert.assertEquals(container, ref.getContainer());
    }
    
    /**
     * Ensures we go to the container when we need to get the object
     */
    @Test
    public void getObject() {
        final Container container = context.mock(Container.class);
        final ObjexObj obj = context.mock(ObjexObj.class);
        final ObjexID id = new DefaultObjexID("test", 123);
        
        context.checking(new Expectations() {{
            oneOf(container).getObject(id); will(returnValue(obj));
            oneOf(obj).getProperty("test"); will(returnValue("Testing 123"));
        }});
        
        ProxyReference ref = new ProxyReference(id, container);
        
        Assert.assertEquals("Testing 123", ref.getProperty("test"));
    }
}
