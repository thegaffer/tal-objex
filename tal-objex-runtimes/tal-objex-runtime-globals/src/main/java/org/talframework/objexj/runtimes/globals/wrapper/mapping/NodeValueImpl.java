package org.talframework.objexj.runtimes.globals.wrapper.mapping;

import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeValue;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.ValueType;

/**
 * Basic and simple implementation of the NodeValue interface
 *
 * @author Tom Spencer
 */
public class NodeValueImpl implements NodeValue {
    
    private final String name;
    private final Class<?> valueClass;
    private final ValueType valueType;
    private final boolean subNode;
    private final int position;
    
    public NodeValueImpl(String name, Class<?> valueClass, ValueType valueType, int position) {
        this.name = name;
        this.valueClass = valueClass;
        this.valueType = valueType;
        this.subNode = position < 0;
        this.position = position;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Class<?> getExpected() {
        return valueClass;
    }
    
    @Override
    public ValueType getType() {
        return valueType;
    }
    
    @Override
    public int getPosition() {
        return position;
    }
    
    @Override
    public boolean isSubNode() {
        return subNode;
    }
}
