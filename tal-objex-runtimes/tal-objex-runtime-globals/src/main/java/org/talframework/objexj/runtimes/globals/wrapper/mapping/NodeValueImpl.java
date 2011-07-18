package org.talframework.objexj.runtimes.globals.wrapper.mapping;

import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeValue;

/**
 * Basic and simple implementation of the NodeValue interface
 *
 * @author Tom Spencer
 */
public class NodeValueImpl implements NodeValue {
    
    private final String name;
    private final boolean subNode;
    private final int position;
    
    public NodeValueImpl(String name, int position) {
        this.name = name;
        this.subNode = position < 0;
        this.position = position;
    }

    @Override
    public String getName() {
        return name;
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
