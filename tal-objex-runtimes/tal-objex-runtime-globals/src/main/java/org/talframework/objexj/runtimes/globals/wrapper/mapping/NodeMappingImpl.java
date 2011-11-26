package org.talframework.objexj.runtimes.globals.wrapper.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(NodeMappingImpl.class);
    
    /** Holds the types name */
    private final String name;
    /** Holds the nodes type */
    private final NodeType type;
    /** The java type of this node if this node holds a single value */
    private final Class<?> nodeClass;
    /** The type of this node if this node holds a single value */
    private final ValueType nodeType;
    /** Holds the map of sub nodes */
    private Map<String, NodeMapping> subNodes;
    /** Holds the map of properties held in the list */
    private Map<String, NodeValue> properties;
    /** Max holds the highest amount of properties, though not all neccessarily still exist */
    private int maxProperties = 0;
    
    /**
     * Constructor for simple or complex objects
     * 
     * @param name
     * @param type
     */
    public NodeMappingImpl(String name, NodeType type) {
        this.name = name;
        this.type = type;
        this.nodeClass = Object[].class;
        this.nodeType = ValueType.OBJECT;
    }
    
    /**
     * Constructor for other custom nodes
     * 
     * @param name
     * @param type
     * @param nodeClass
     * @param nodeType
     */
    public NodeMappingImpl(String name, NodeType type, Class<?> nodeClass, ValueType nodeType) {
        this.name = name;
        this.type = type;
        this.nodeClass = nodeClass;
        this.nodeType = nodeType;
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
            Iterator<Node> it = node.getSubNode("props").iterator();
            while( it.hasNext() ) {
                Node n = it.next();
                
                String name = n.getName();
                int position = n.get("position", int.class);
                Class<?> javaType = Class.forName(n.get("class", String.class));
                ValueType valueType = ValueType.valueOf(n.get("type", String.class));
                
                addProperty(new NodeValueImpl(name, javaType, valueType, position));
            }
            
            // Sub-nodes
        }
        catch( Exception e ) {
            logger.error("Failure [{}] loading Node Schema: {}", name, e.getMessage());
            throw new RuntimeException(e);
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
                
                Node propNode = props.getSubNode(value.getName());
                propNode.put("position", value.getPosition());
                propNode.put("class", value.getExpected().getName());
                propNode.put("type", value.getType().toString());
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getValueClass() {
        return nodeClass;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ValueType getValueType() {
        return nodeType;
    }
}
