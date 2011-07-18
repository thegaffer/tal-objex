package org.talframework.objexj.runtimes.globals.wrapper.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.runtimes.globals.wrapper.Node;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping;
import org.talframework.objexj.runtimes.globals.wrapper.RootNode;
import org.talframework.objexj.runtimes.globals.wrapper.impl.NodeService;

/**
 * Standard (and simple) implementation of the {@link NodeMapping}
 * interface.
 *
 * @author Tom Spencer
 */
public class NodeMappingImpl implements NodeMapping {

    /** Holds the types name */
    private final String name;
    /** Holds the nodes type */
    private final NodeType type;
    /** Holds the map of sub nodes */
    private Map<String, NodeMapping> subNodes;
    /** Holds the map of properties held in the list */
    private Map<String, NodeValue> properties;
    /** Max holds the highest amount of properties, though not all neccessarily still exist */
    private int maxProperties = 0;
    
    public NodeMappingImpl(String name, NodeType type) {
        this.name = name;
        this.type = type;
    }
    
    public void addSubNode(String name, NodeMapping mapping) {
        if( subNodes == null ) subNodes = new HashMap<String, NodeMapping>();
        subNodes.put(name, mapping);
    }
    
    public void addProperty(NodeValue value) {
        if( properties == null ) properties = new HashMap<String, NodeMapping.NodeValue>();
        properties.put(value.getName(), value);
        
        if( value.getPosition() >= maxProperties ) maxProperties = value.getPosition() + 1;
    }
    
    /**
     * Call to load the type from the Globals DB Store
     * 
     * @param service The NodeService to use
     * @param docType The type of document the mapping is within
     */
    public void load(NodeService service, String docType) {
        RootNode root = null; 
        
        try {
            root = service.getNode(docType);
            Node node = root.getSubNode("_schema").getSubNode(this.name);
            
            // Props
            Iterator<Node> it = node.iterator();
            while( it.hasNext() ) {
                Node n = it.next();
                Object val = n.get(null);
                if( val != null && val instanceof Number ) {
                    addProperty(new NodeValueImpl(n.getName(), ((Number)val).intValue()));
                }
            }
            
            // Sub-nodes
        }
        finally {
            if( root != null ) root.release();
        }
    }
    
    /**
     * Call to load the type from the Globals DB Store
     * 
     * @param service The NodeService to use
     * @param docType The type of document the mapping is within
     */
    public void save(NodeService service, String docType) {
        RootNode root = null; 
        
        try {
            root = service.getNode(docType);
            Node node = root.getSubNode("_schema").getSubNode(this.name);
            
            // Properties
            Node props = node.getSubNode("props");
            for( String prop : properties.keySet() ) {
                NodeValue value = properties.get(prop);
                props.put(value.getName(), value.getPosition());
                // TODO: Also store type information???
            }
            
            // Sub-Nodes
             
            root.commit(true);
        }
        finally {
            root.release();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public NodeType getNodeType() {
        return type;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getKeyNames() {
        Set<String> ret = new HashSet<String>();
        if( subNodes != null ) ret.addAll(subNodes.keySet());
        if( properties != null ) ret.addAll(properties.keySet());
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidSubNode(String name) {
        return subNodes != null ? subNodes.containsKey(name) : null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public NodeValue getNodeValue(String name) {
        return properties != null ? properties.get(name) : null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalFieldsInList() {
        return maxProperties;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public NodeMapping getSubNodeMapping(String subNode) {
        return subNodes != null ? subNodes.get(subNode) : null;
    }
}
