package org.talframework.objexj.runtimes.globals;

import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyStrategy;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeType;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping.NodeValue;
import org.talframework.objexj.runtimes.globals.wrapper.impl.NodeService;
import org.talframework.objexj.runtimes.globals.wrapper.mapping.NodeMappingImpl;
import org.talframework.objexj.runtimes.globals.wrapper.mapping.NodeValueImpl;

/**
 * This class reads an Objex object and create a Globals Wrapper mapping
 * for it.
 *
 * @author Tom Spencer
 */
public class ObjectMappingFactory {

    /**
     * Gets or creates the NodeMapping for the given object in this document type
     * 
     * @param service The {@link NodeService} to use
     * @param docType The type of document this object is within
     * @param strategy The object strategy
     * @return The node mapping
     */
    public static NodeMapping createMapping(NodeService service, String docType, ObjectStrategy strategy) {
        NodeMappingImpl mapping = new NodeMappingImpl(strategy.getMainClass().getName(), NodeType.COMPLEX_OBJECT);
        mapping.load(service, docType);
        
        for( String p : strategy.getPropertyNames() ) {
            NodeValue value = mapping.getNodeValue(p);
            if( value != null ) {
                // TODO: Check it is compatible!!
                continue;
            }
            
            // Otherwise it is a new property we have not seen before
            PropertyStrategy prop = strategy.getProperty(p);
            
            // Currently highly simplified!!
            switch(prop.getObjexType()) {
            case STRING:
            case USER:
            case NUMBER:
            case BLOB_REFERENCE:
            case DATE:
            case BOOL:
            case SHORT_BLOB:
                mapping.addProperty(new NodeValueImpl(p, mapping.getTotalFieldsInList()));
                break;
                
            case MEMO:
            case BLOB:
            case REFERENCE:
            case OWNED_REFERENCE:
                mapping.addProperty(new NodeValueImpl(p, -1));
                break;
                
            case OBJECT:
                // TODO: Add a sub-node with mapping for this type of object!!
                break;
            }
            
            mapping.save(service, docType);
        }
        
        return mapping;
    }
}
