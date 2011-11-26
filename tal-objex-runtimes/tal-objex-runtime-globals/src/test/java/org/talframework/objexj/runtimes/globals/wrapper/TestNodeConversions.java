package org.talframework.objexj.runtimes.globals.wrapper;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeValue;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.ValueType;
import org.talframework.objexj.runtimes.globals.wrapper.impl.NodeImpl;
import org.talframework.objexj.runtimes.globals.wrapper.impl.RootNodeImpl;
import org.talframework.objexj.runtimes.globals.wrapper.mapping.NodeValueImpl;

import com.intersys.globals.Connection;
import com.intersys.globals.NodeReference;

/**
 * This test class specifically tests the conversions of types within
 * Node wrapper.
 *
 * @author Tom Spencer
 */
public class TestNodeConversions {
    
    private Connection connection;
    private NodeReference reference;
    private RootNode root;
    
    @Before
    public void setup() {
        connection = mock(Connection.class);
        reference = mock(NodeReference.class);
        
        when(connection.createNodeReference("test")).thenReturn(reference);
        root = new RootNodeImpl(connection, "test");
        verify(connection).createNodeReference("test");
    }
    
    @After
    public void teardown() {
        root.release();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void asDouble() {
        NodeMapping mapping = mock(NodeMapping.class);
        root.setMapping(mapping);
        
        // Primitive
        when(mapping.getValueClass()).thenReturn((Class)double.class);
        when(mapping.getValueType()).thenReturn(ValueType.DOUBLE);
        when(reference.exists()).thenReturn(true);
        when(reference.getDouble()).thenReturn(123.5);
        Assert.assertEquals(123.5, root.get(null));
        
        // Object
        when(mapping.getValueClass()).thenReturn((Class)Double.class);
        when(mapping.getValueType()).thenReturn(ValueType.DOUBLE);
        when(reference.exists()).thenReturn(true);
        when(reference.getDouble()).thenReturn(123.5);
        Assert.assertEquals(123.5, root.get(null));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void asFloat() {
        NodeMapping mapping = mock(NodeMapping.class);
        root.setMapping(mapping);
        
        // Primitive
        when(mapping.getValueClass()).thenReturn((Class)float.class);
        when(mapping.getValueType()).thenReturn(ValueType.FLOAT);
        when(reference.exists()).thenReturn(true);
        when(reference.getDouble()).thenReturn(123.5);
        Assert.assertEquals(123.5f, root.get(null));
        
        // Object
        when(mapping.getValueClass()).thenReturn((Class)Float.class);
        when(mapping.getValueType()).thenReturn(ValueType.FLOAT);
        when(reference.exists()).thenReturn(true);
        when(reference.getDouble()).thenReturn(123.5);
        Assert.assertEquals(123.5f, root.get(null));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void asLong() {
        NodeMapping mapping = mock(NodeMapping.class);
        root.setMapping(mapping);
        
        // Primitive
        when(mapping.getValueClass()).thenReturn((Class)long.class);
        when(mapping.getValueType()).thenReturn(ValueType.LONG);
        when(reference.exists()).thenReturn(true);
        when(reference.getLong()).thenReturn(123L);
        Assert.assertEquals(123L, root.get(null));
        
        // Object
        when(mapping.getValueClass()).thenReturn((Class)Long.class);
        when(mapping.getValueType()).thenReturn(ValueType.LONG);
        when(reference.exists()).thenReturn(true);
        when(reference.getLong()).thenReturn(123L);
        Assert.assertEquals(123L, root.get(null));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void asInteger() {
        NodeMapping mapping = mock(NodeMapping.class);
        root.setMapping(mapping);
        
        // Primitive
        when(mapping.getValueClass()).thenReturn((Class)int.class);
        when(mapping.getValueType()).thenReturn(ValueType.INT);
        when(reference.exists()).thenReturn(true);
        when(reference.getInt()).thenReturn(123);
        Assert.assertEquals(123, root.get(null));
        
        // Object
        when(mapping.getValueClass()).thenReturn((Class)Integer.class);
        when(mapping.getValueType()).thenReturn(ValueType.INT);
        when(reference.exists()).thenReturn(true);
        when(reference.getInt()).thenReturn(123);
        Assert.assertEquals(123, root.get(null));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void asShort() {
        NodeMapping mapping = mock(NodeMapping.class);
        root.setMapping(mapping);
        
        // Primitive
        when(mapping.getValueClass()).thenReturn((Class)short.class);
        when(mapping.getValueType()).thenReturn(ValueType.SHORT);
        when(reference.exists()).thenReturn(true);
        when(reference.getInt()).thenReturn(123);
        Assert.assertEquals((short)123, root.get(null));
        
        // Object
        when(mapping.getValueClass()).thenReturn((Class)Short.class);
        when(mapping.getValueType()).thenReturn(ValueType.SHORT);
        when(reference.exists()).thenReturn(true);
        when(reference.getInt()).thenReturn(123);
        Assert.assertEquals((short)123, root.get(null));
    }
}
