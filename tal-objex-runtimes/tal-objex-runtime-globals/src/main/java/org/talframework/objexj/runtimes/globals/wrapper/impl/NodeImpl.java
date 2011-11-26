package org.talframework.objexj.runtimes.globals.wrapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talframework.objexj.runtimes.globals.wrapper.Node;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeType;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeValue;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.ValueType;

import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;

/**
 * Implements the node wrapper
 *
 * @author Tom Spencer
 */
public class NodeImpl implements Node {
    private static final Logger logger = LoggerFactory.getLogger(NodeImpl.class);

    /** The root node */
    private final RootNodeImpl root;
    /** The parent node to this one */
    private final NodeImpl parent;
    /** The subscript of this node */
    protected final Object subscript;
    /** The mapping for this node */
    private NodeMapping mapping;
    
    /** Holds any sub-nodes we have changed the value of */
    protected Map<String, NodeImpl> subNodes;
    /** Holds the value of this node */
    protected Object currentValue;
    /** Holds the type of the current value */
    protected ValueType currentType;
    /** Determine what to do at commit time */
    protected UpdateType updateType;
    
    protected NodeImpl(RootNodeImpl root, NodeImpl parent, Object subscript, NodeMapping mapping) {
        this.root = root;
        this.parent = parent;
        this.subscript = subscript;
        this.mapping = mapping;
    }
    
    /**
     * @return The root node
     */
    public RootNodeImpl getRootNode() {
        return root;
    }
    
    /**
     * @return The parent node to this one (will be null if this node is the root)
     */
    protected NodeImpl getParentNode() {
        return parent;
    }
    
    /**
     * Helper to get the nodes type
     * 
     * @return The type of this node
     */
    protected NodeType getNodeType() {
        return (mapping != null && mapping.getNodeType() != null) ? mapping.getNodeType() : NodeType.NONE;
    }
    
    /**
     * Internal helper to append the subscript for this node into
     * the given builder.
     * 
     * @param builder The StringBuilder to add into
     */
    protected void appendPath(StringBuilder builder) {
        if( parent != null ) parent.appendPath(builder);
        else getRootNode().appendPath(builder);
        builder.append('/').append(subscript);
    }
    
    /**
     * Internal helper to append the subscript for this node into
     * the given list.
     * 
     * @param lst The list to add into
     */
    protected void appendPath(List<String> lst) {
        if( parent != null ) parent.appendPath(lst);
        else getRootNode().appendPath(lst);
        lst.add(this.subscript.toString());
    }
    
    /**
     * Call to get the node reference for this node. This will
     * simply call the root to get the information.
     * 
     * @return The actual node reference
     */
    protected NodeReference getNode() {
        return getRootNode().getNode(this);
    }
    
    /**
     * This is called when a child node has its value changed. This
     * enables us to save the NodeImpl and use it in the future.
     * 
     * @param node The node that has changed
     */
    protected void changedSubNode(NodeImpl node) {
        if( updateType == UpdateType.KILL_ALL ) return;
        
        if( subNodes == null ) subNodes = new HashMap<String, NodeImpl>();
        subNodes.put(node.getName(), node);
        if( parent != null ) parent.changedSubNode(this);
    }
    
    /**
     * Called when we need to commit the values
     */
    protected void onCommit() {
        // Save the current value if it has been set
        // If array is null then it has been unchanged
        switch( updateType ) {
        case KILL_ALL:
            getNode().kill();
            break;
        
        case KILL:
            getNode().killNode();
            break;
            
        case UPDATE:
            NodeReference node = getNode();
            
            switch(currentType) {
            case OBJECT:
            case STRING_LIST:
            case STRING_ARRAY:
            case DOUBLE_ARRAY:
            case FLOAT_ARRAY:
            case LONG_ARRAY:
            case INT_ARRAY:
            case SHORT_ARRAY:
            case CHAR_ARRAY:
                ValueList lst = getRootNode().getTempList();
                lst.append(currentValue);
                node.set(lst);
                break;
            
            case BYTE_ARRAY:
                node.set((byte[])currentValue);
                break;
                
            case STRING:
                node.set((String)currentValue);
                break;
                
            case DOUBLE:
            case FLOAT:
                node.set((Double)currentValue);
                break;
                
            case LONG:
                node.set((Long)currentValue);
                break;
                
            case INT:
            case SHORT:
            case CHAR:
            case BYTE:
            case BOOL:
                node.set((Integer)currentValue);
                break;
            }
        }
        
        // Tell children to change themselves
        if( subNodes != null ) {
            for( String k : subNodes.keySet() ) {
                subNodes.get(k).onCommit();
            }
        }
    }
    
    /**
     * @return Determines based on mapping if this node holds a list
     */
    protected boolean isListNode() {
        boolean ret = false;
        
        switch (getNodeType()) {
        case NONE:
        case LISTING:
        case SMALL_FOLDER:
        case LEAF_OBJECT:
            ret = false;
            break;
        
        case VALUE:
            ret = false;
            // TODO: Inspect type in mapping
            break;
            
        case SIMPLE_OBJECT:
        case COMPLEX_OBJECT:
            ret = true;
            break;
        }
        
        return ret;
    }
    
    /**
     * This helper determines if we have a property that is in the list
     * held by this node.
     * 
     * @param name The name of the property
     * @return True if it is held in the list, false (meaning its a sub-node) otherwise
     */
    protected boolean isPropertyInList(String name) {
        boolean ret = false;
        
        switch (getNodeType()) {
        case SIMPLE_OBJECT:
        case COMPLEX_OBJECT:
            NodeValue value = mapping.getNodeValue(name);
            ret = !value.isSubNode() && value.getPosition() >= 0;
            break;
        }
        
        return ret;
    }
    
    /**
     * Helper method to get this nodes current value as an array
     * 
     * @return The nodes current value
     */
    @SuppressWarnings("unchecked")
    protected <T> T getCurrentValue(ValueType type, Class<T> expected) {
        // If we already have it, serve it up
        if( currentValue != null &&
                currentType == type &&
                expected.isInstance(currentValue) ) {
            return expected.cast(currentValue);
        }
        
        // If no data in node then it does not exist
        NodeReference node = getNode();
        if( !node.exists() ) return null;
        
        // Otherwise we need to get it
        NodeType nodeType = getNodeType();
        switch( nodeType ) {
        case COMPLEX_OBJECT:
        case SIMPLE_OBJECT:
            currentValue = getRawValue(ValueType.OBJECT);
            currentType = ValueType.OBJECT;
            
        default:
            currentValue = getRawValue(type);
            currentType = type;
        }
        
        return (T)convertValue(currentValue, expected, currentType);
    }
        
    /**
     * Internal helper to get the raw value held in the node 
     */
    protected Object getRawValue(ValueType type) {
        NodeReference node = getNode();
        if( !node.exists() ) return null;
        
        ValueList lst = null;
        Object ret = null;
        switch( type ) {
        case STRING:
            ret = node.getString();
            break;
            
        case DOUBLE:
            ret = node.getDouble();
            break;
            
        case LONG:
            ret = node.getLong();
            break;
            
        case INT:
            ret = node.getInt();
            break;
        
        case SHORT:
            ret = (short)node.getInt();
            break;
          
        case CHAR:
            ret = node.getString();
            if( ret != null ) ret = ret.toString().charAt(0);
            break;
            
        case BYTE:
            byte[] bytes = node.getBytes();
            if( bytes != null && bytes.length > 0 ) ret = bytes[0];
            break;
            
        case BOOL:
            ret = node.getInt() == 1;
            break;
            
        case STRING_LIST:
            lst = node.getList();
            List<String> realList = new ArrayList<String>();
            for( int i = 0 ; i < lst.length() ; i++ ) {
                String s = lst.getNextString();
                if( s != null ) realList.add(s);
            }
            ret = realList;
            break;
            
        case STRING_ARRAY:
            lst = node.getList();
            String[] strarr = new String[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) strarr[i] = lst.getNextString();
            ret = strarr;
            lst.close();
            break;
            
        case DOUBLE_ARRAY:
            lst = node.getList();
            double[] dblarr = new double[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) dblarr[i] = lst.getNextDouble();
            ret = dblarr;
            lst.close();
            break;
            
        case FLOAT_ARRAY:
            lst = node.getList();
            float[] fltarr = new float[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) fltarr[i] = (float)lst.getNextDouble();
            ret = fltarr;
            lst.close();
            break;
            
        case LONG_ARRAY:
            lst = node.getList();
            long[] lngarr = new long[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) lngarr[i] = lst.getNextLong();
            ret = lngarr;
            lst.close();
            break;
            
        case INT_ARRAY:
            lst = node.getList();
            int[] intarr = new int[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) intarr[i] = lst.getNextInt();
            ret = intarr;
            lst.close();
            break;
            
        case SHORT_ARRAY:
            lst = node.getList();
            short[] shtarr = new short[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) shtarr[i] = (short)lst.getNextInt();
            ret = shtarr;
            lst.close();
            break;
            
        case CHAR_ARRAY:
            lst = node.getList();
            char[] chrarr = new char[lst.length()];
            for( int i = 0 ; i < lst.length() ; i++ ) chrarr[i] = (char)lst.getNextInt();
            ret = chrarr;
            lst.close();
            break;
            
        case BYTE_ARRAY:
            ret = node.getBytes();
            break;
            
        case OBJECT:
            lst = node.getList();
            ret = lst.getAll();
            if( lst.length() == 1 ) ret = ((Object[])ret)[0];
            lst.close();
            break;
        }
        
        return ret;
    }
    
    /**
     * This internal helper method converts a raw value stored in the node
     * into an external type.
     * 
     * FUTURE: Expand so this is ane extensible mechanism?
     * 
     * @param value The value to convert as necc
     * @param expected The expected type
     * @param type The storage type
     * @return The value to return
     */
    private static Object convertValue(Object value, Class<?> expected, ValueType type) {
        // If null then return it
        if( value == null ) return null;
        
        // If matching, return it
        else if( expected.isInstance(value) ) return expected.cast(value);
        
        // Primitive conversions
        else if( expected.isPrimitive() ) {
            if( !(value instanceof Number) ) {
                logger.warn("Expected primitive, but did not get Number {}", value);
                return null;
            }
            
            // TODO: Trusting that types match!!
            return value;
        }
        
        // Simple date conversion
        else if( Date.class.equals(expected) ) {
            if( !(value instanceof Long) ) {
                logger.warn("Expected date, but value {} is not a long", value);
                return null;
            }
            else {
                return new Date(((Long)value).longValue());
            }
        }
        
        // All else fails
        else {
            logger.warn("Unable to convert value {} to expected type: {}", value, expected);
            return null;
        }
    }
    
    /////////////////////////////////////////
    // Node
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        StringBuilder buf = getRootNode().getTempBuffer();
        appendPath(buf);
        return buf.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPathList() {
        ArrayList<String> ret = new ArrayList<String>();
        appendPath(ret);
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return subscript.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapping(NodeMapping mapping) {
        this.mapping = mapping;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataNode() {
        return getNode().exists();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isParentNode() {
        return getNode().hasSubnodes();
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>If the value of the sub-node has changed this will be the same
     * instance each time, otherwise this will be a new instance.</p>
     */
    @Override
    public NodeImpl getSubNode(String name) {
        if( subNodes != null && subNodes.containsKey(name) ) return subNodes.get(name);
        else return new NodeImpl(getRootNode(), this, name, mapping != null ? mapping.getSubNodeMapping(name) : null);
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p><b>Note: </b>No checking is performed to make sure this is valid
     * for the node!!</p>
     */
    @Override
    public long increment(int number) {
        return getNode().increment(1);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator(this, "", false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Node> iterator(String start) {
        return new NodeIterator(this, start, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Node> reverseIterator() {
        return new NodeIterator(this, "", true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Node> reverseIterator(String start) {
        return new NodeIterator(this, start, true);
    }
    
    @Override
    public void kill() {
        currentValue = null;
        subNodes = null;
        updateType = UpdateType.KILL_ALL;
        if( parent != null ) parent.changedSubNode(this);
    }
    
    ////////////////////////////////////////////////
    // Map Operations
    
    /**
     * {@inheritDoc}
     * 
     * <p>Simply determines if the key exists. If the key is null then this
     * purely determines if this node has a data value. If this node has
     * a mapping and that states this the key is not a valid subnode then
     * this method returns false regardless.
     */
    public boolean containsKey(Object key) {
        NodeReference node = getNode();
        
        if( key == null ) return node.exists(); 
        else if( this.mapping != null && !this.mapping.isValidSubNode(key.toString()) ) return false;
        else return node.exists(key) || node.hasSubnodes(key);
        // TODO: If property in list we should determine this!!  
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This is not supported at all</p>
     */
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Cannot determine if value exists on a node");
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Entry<String, Object>> entrySet() {
        
        Set<Entry<String, Object>> ret = new HashSet<Map.Entry<String,Object>>();
        for( String s : keySet() ) {
            final String key = s;
            ret.add(new Entry<String, Object>() {
                
                public String getKey() { 
                    return key;
                }

                public Object getValue() {
                    return get(key);
                }

                public Object setValue(Object value) {
                    throw new UnsupportedOperationException("Setting via EntrySet is not possible");
                }
            } );
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Note this just detects if this node has subnodes</p>
     */
    public boolean isEmpty() {
        return !getNode().hasSubnodes();
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>The key set is only returned when the node represents an
     * object, otherwise this is null. If you want to walk a node
     * then use the iterator methods.</p>
     */
    public Set<String> keySet() {
        Set<String> ret = null;
        
        switch(getNodeType()) {
        case LEAF_OBJECT:
        case COMPLEX_OBJECT:
        case SIMPLE_OBJECT:
            ret = mapping.getKeyNames();
            break;
            
        default:
            ret = new HashSet<String>();
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Uses keySet to determine the valid keys and get those values.
     * This is probably useless!</p>
     */
    public Collection<Object> values() {
        List<Object> ret = new ArrayList<Object>();
        
        for( String k : keySet() ) {
            ret.add(get(k));
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>We simply return the size of the keyset if there is one. This
     * may not match the number of items actually stored of which we do
     * not neccessarily know.</p>
     */
    public int size() {
        return keySet().size();
    }
    
    /**
     * {@inheritDoc}
     * 
     * Return the value against the key. If the key is null then this will
     * always get the raw value of this node. Otherwise it will get that
     * specific property
     */
    public Object get(Object key) {
        ValueType type = ValueType.OBJECT;
        Class<?> storedType = Object.class;
        
        if( key == null && mapping != null ) {
            type = mapping.getValueType();
            storedType = mapping.getValueClass();
        }
        else if( key != null && mapping != null ) {
            type = mapping.getNodeValue(key.toString()).getType();
            storedType = mapping.getNodeValue(key.toString()).getExpected();
        }
        
        return get(key != null ? key.toString() : null, storedType, type);
    }
    
    /**
     * Helper to use when the client knows the type of object held at key.
     * 
     * @param <T>
     * @param key
     * @param expected
     * @return
     */
    public <T> T get(Object key, Class<T> expected) {
        ValueType type = null;
        Class<?> storedType = expected;
        
        if( key != null && mapping != null ) {
            NodeValue value = mapping.getNodeValue(key.toString());
            if( value != null ) {
                type = value.getType();
                storedType = value.getExpected();
            }
        }
        
        // If type not set try to work out from the expected type
        if( type == null ) type = getAppropriateValueType(expected);
        
        Object ret = get(key != null ? key.toString() : null, storedType, type);
        
        if( ret == null ) return null;
        else if( expected.isInstance(ret) ) return expected.cast(ret);
        else throw new IllegalArgumentException("Type [" + expected + "] does not match mapping type: " + ret);
        // FUTURE: Common conversions!?!
    }
    
    /**
     * Works out the most appropriate value type given the expected type
     * of a value.
     * 
     * @param cls
     * @return
     */
    protected ValueType getAppropriateValueType(Class<?> cls) {
        if( String.class.equals(cls) ) return ValueType.STRING;
        else if( Double.class.equals(cls) || double.class.equals(cls) ) return ValueType.DOUBLE;
        else if( Float.class.equals(cls) || float.class.equals(cls) ) return ValueType.FLOAT;
        else if( Long.class.equals(cls) || long.class.equals(cls) ) return ValueType.LONG;
        else if( Date.class.equals(cls) ) return ValueType.LONG;
        else if( Integer.class.equals(cls) || int.class.equals(cls) ) return ValueType.INT;
        else if( Short.class.equals(cls) || short.class.equals(cls) ) return ValueType.SHORT;
        else if( Character.class.equals(cls) || char.class.equals(cls) ) return ValueType.CHAR;
        else if( Byte.class.equals(cls) || byte.class.equals(cls) ) return ValueType.BYTE;
        else if( Boolean.class.equals(cls) || boolean.class.equals(cls) ) return ValueType.BOOL;
        else if( String[].class.equals(cls) ) return ValueType.STRING_ARRAY;
        else if( Object[].class.equals(cls) ) return ValueType.OBJECT;
        else throw new IllegalArgumentException("Unsupported expected type: " + cls);
    }
    
    /**
     * This is the method that gets a nodes value as a specific type.
     * This method is not exposed to the outside world.
     * 
     * @param <T>
     * @param key The key of the property (null if it is this nodes natural value)
     * @param expected The expected type
     * @return The type of the node
     */
    protected <T> T get(String key, Class<T> expected, ValueType type) {
        T ret = null;
        
        // Directly get this nodes value
        if( key == null ) {
            ret = getCurrentValue(type, expected);
        }
        
        // Get the value from the list
        else if( isPropertyInList(key.toString()) ) {
            Object[] value = getCurrentValue(ValueType.OBJECT, Object[].class);   // Ensures this is set
            
            int position = mapping.getNodeValue(key.toString()).getPosition();
            if( value != null && position < value.length ) ret = expected.isInstance(value[position]) ? expected.cast(value[position]) : null;
        }
        
        // Get the sub-node and get value from there
        else {
            NodeImpl sub = getSubNode(key.toString());
            ret = sub.get(null, expected, type);
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    public void clear() {
        Set<String> keys = keySet();
        for( String k : keys ) {
            remove(k);
        }
        remove(null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object put(String key, Object value) {
        if( value == null ) return remove(key);
        
        ValueType naturalType = null;
        Class<?> storageType = null;
        
        if( mapping != null ) {
            if( key == null ) {
                naturalType = mapping.getValueType();
                storageType = mapping.getValueClass();
            }
            else if( mapping.getNodeValue(key) != null ) {
                storageType = mapping.getNodeValue(key).getExpected();
                naturalType = mapping.getNodeValue(key).getType();
            }
            
            if( storageType != null && !storageType.isInstance(value) ) throw new IllegalArgumentException("The type of value [" + key + ":" + value + "] does not match expected for node: " + storageType);
        }
        else {
            storageType = value.getClass();
            naturalType = getAppropriateValueType(storageType);
        }
        
        return put(key, value, storageType, naturalType);
    }
    
    /**
     * Internal method to set a value given it's type. It is assumed
     * in this method the value's type matches the ValueType sent
     * in.
     * 
     * @param key The name of the field
     * @param value The value
     * @param expected The type of the value (needed if value is null)
     * @param type The value type to store as
     * @return The existing value
     */
    protected Object put(String key, Object value, Class<?> expected, ValueType type) {
        if( value == null ) return remove(key);
        
        boolean flag = false;   // Set to true if we have changed
        Object ret = null;
        
        // a. Handle direct node value
        if( key == null ) {
            ret = getCurrentValue(type, expected);
            currentValue = value;
            currentType = type;
            flag = true;
        }
        
        // b. Handle list in node value
        else if( isPropertyInList(key) ) {
            int position = mapping.getNodeValue(key).getPosition();
            
            Object[] arr = getCurrentValue(ValueType.OBJECT, Object[].class);
            if( arr == null || arr.length < position ) {
                Object[] newarr = new Object[mapping.getTotalFieldsInList() > position ? mapping.getTotalFieldsInList() : position];
                if( arr != null ) System.arraycopy(arr, 0, newarr, 0, arr.length);
                arr = newarr;
            }
            
            arr[position] = value;
            currentValue = arr;
            currentType = ValueType.OBJECT;
            flag = true;
        }
        
        // c. Handle regular sub-node
        else {
            ret = getSubNode(key).put(null, value, expected, type);
        }
        
        if( flag ) parent.changedSubNode(this);
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    public void putAll(Map<? extends String, ? extends Object> t) {
        for( String s : t.keySet() ) {
            put(s, t.get(s));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object remove(Object key) {
        boolean flag = false;   // Set to true if we have changed
        Object oldValue = get(key);
        
        if( key == null ) {
            oldValue = get(key);
            currentValue = null;
            updateType = UpdateType.KILL;
            flag = true;
        }
        else if( isPropertyInList(key.toString()) ) {
            int position = mapping.getNodeValue(key.toString()).getPosition();
            Object[] arr = getCurrentValue(ValueType.OBJECT, Object[].class);
            if( arr != null && arr.length > position ) {
                oldValue = arr[position];
                arr[position] = null;
                updateType = UpdateType.UPDATE;
                flag = true;
            }
        }
        else {
            oldValue = getSubNode(key.toString()).remove(null);
        }
        
        if( flag ) parent.changedSubNode(this);
        return oldValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mapping == null) ? 0 : mapping.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((root == null) ? 0 : root.hashCode());
        result = prime * result + ((subscript == null) ? 0 : subscript.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        NodeImpl other = (NodeImpl)obj;
        if( mapping == null ) {
            if( other.mapping != null ) return false;
        }
        else if( !mapping.equals(other.mapping) ) return false;
        if( parent == null ) {
            if( other.parent != null ) return false;
        }
        else if( !parent.equals(other.parent) ) return false;
        if( root == null ) {
            if( other.root != null ) return false;
        }
        else if( !root.equals(other.root) ) return false;
        if( subscript == null ) {
            if( other.subscript != null ) return false;
        }
        else if( !subscript.equals(other.subscript) ) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Node: ");
        appendPath(buf);
        return buf.toString();
    }
    
    /////////////////////////////////////////
    // Helper Classes
    
    /**
     * This class implements the iterator interface around the current
     * node to get at it's children.
     */
    private final static class NodeIterator implements Iterator<Node> {
        
        public final NodeImpl node;
        public String current;
        public final boolean reverse;
        
        public NodeIterator(NodeImpl node, String current, boolean reverse) {
            this.node = node;
            this.current = current;
            this.reverse = reverse;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            String next = null;
            
            if( reverse ) next = node.getNode().previousSubscript(current);
            else next = node.getNode().nextSubscript(current);
            
            return next != null && !"".equals(next);
        }
        
        /**
         * {@inheritDoc}
         */
        public Node next() {
            if( reverse ) current = node.getNode().previousSubscript(current);
            else current = node.getNode().nextSubscript(current);
            
            return current != null || !"".equals(current) ? node.getSubNode(current) : null;
        }
        
        /**
         * {@inheritDoc}
         */
        public void remove() {
            String previous = current;
            
            if( reverse ) current = node.getNode().previousSubscript(current);
            else current = node.getNode().nextSubscript(current);
            
            if( current != null || !"".equals(current) ) node.getNode().kill(previous);
        }
        
        /**
         * {@inheritDoc}
         * 
         * <p>Indicates where at and node iterating through</p>
         */
        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            if( reverse ) buf.append("Reverse");
            buf.append("NodeIterator@");
            buf.append(current);
            buf.append(" from ");
            buf.append(node);
            return buf.toString();
        }
    }
    
    /**
     * Enum indicates what to do at commit time (if anything)
     *
     * @author Tom Spencer
     */
    private static enum UpdateType {
        /** Indicates to update the nodes value */
        UPDATE,
        /** Indicates to blank the nodes value */
        KILL,
        /** Indicates to kill the node and sub-nodes */
        KILL_ALL;
    }
}
