/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.runtimes.globals;

import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.runtimes.globals.wrapper.Node;
import org.talframework.objexj.runtimes.globals.wrapper.NodeMapping;
import org.talframework.objexj.runtimes.globals.wrapper.RootNode;
import org.talframework.objexj.runtimes.globals.wrapper.impl.NodeService;

public class GlobalsMiddlewareFactory implements ContainerMiddlewareFactory {
    
    /** The name of the container type */
    private final String name;
    /** Holds the NodeService we should use */
    private final NodeService service;
    /** Member holds the NodeMappings for all types of Objex objects we have */
    private final Map<String, NodeMapping> nodeMappings;
    
    /**
     * Constructs the middleware and ensures all types of objects are described
     * to GDS.
     * 
     * @param strategy The strategy for the container this instance will be connected to
     */
    public GlobalsMiddlewareFactory(NodeService service, ContainerStrategy strategy) {
        this.service = service != null ? service : new NodeService();
        this.name = strategy.getContainerName();
        
        // Ensure we have a schema for each object type
        this.nodeMappings = new HashMap<String, NodeMapping>();
        for( String objectName : strategy.getObjectNames() ) {
            this.nodeMappings.put(objectName, ObjectMappingFactory.createMapping(service, name, strategy.getObjectStrategy(objectName)));
        }
    }
    
    /**
     * Helper to get the document type root node or a specific
     * documents node.
     * 
     * <p>Note: The first part of the ID is the type of this
     * container.</p>
     * 
     * @param id The id of the container to get
     * @return The root node for the container or container type
     */
    public Node getContainerNode(Object[] id) {
        RootNode root = service.getNode(name);
        Node ret = root;
        
        if( id != null ) {
            for( int i = 1 ; i < id.length ; i++ ) {
                ret = ret.getSubNode(id[i].toString());
            }
        }
        
        return ret;
    }
    
    /**
     * @return The name of this container type
     */
    public String getType() {
        return name;
    }
    
    /**
     * Helper for the middleware to get hold of an object mapping for
     * an object type.
     * 
     * @param objectType The type of object
     * @return
     */
    public NodeMapping getObjectMapping(String objectType) {
        if( !nodeMappings.containsKey(objectType) ) throw new IllegalArgumentException("Unknown object type: " + objectType);
        return nodeMappings.get(objectType);
    }
    
    //////////////////////////////////////////////////
    
    @Override
    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        return new GlobalsMiddleware(this, null, true, null);
    }
    
    @Override
    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        ContainerID containerId = new ContainerID(id);
        return new GlobalsMiddleware(this, containerId.getContainerId(), false, containerId.getTransactionId());
    }
    
    @Override
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId) {
        ContainerID containerId = new ContainerID(transactionId);
        if( !containerId.isTransaction() ) throw new IllegalArgumentException("The transaction [" + transactionId + "] is not a valid transactionId for type [" + getType() + "]");
        
        return new GlobalsMiddleware(this, containerId.getContainerId(), true, containerId.getTransactionId());
    }
    
    private class ContainerID {
        private final String[] containerId;
        private final String transactionId;
        private final boolean transaction;
        
        public ContainerID(String id) {
            String[] ids = id.split("/");
            
            if( ids.length < 2 ) throw new IllegalArgumentException("The given ID [" + id + "] is not valid");
            if( !(ids[0].equals(name)) ) throw new IllegalArgumentException("The given ID [" + id + "] is not for this container type [" + getType() + "]");
            
            // See if transaction
            if( ids.length >= 4 && ids[ids.length - 2].equals("trans") ) {
                transaction = true;
                containerId = new String[ids.length -2];
                System.arraycopy(ids, 0, containerId, 0, containerId.length);
                transactionId = ids[ids.length - 1];
            }
            else {
                transaction = false;
                containerId = ids;
                transactionId = null;
            }
            
            // Now check container part exists
            Node node = getContainerNode(containerId);
            boolean validContainer = node.isDataNode() || node.isParentNode();
            boolean validTrans = false;
            if( transaction ) {
                node = node.getSubNode("trans").getSubNode(transactionId);
                validTrans = node.isDataNode() || node.isParentNode();
            }
            node.getRootNode().release();

            // Check exists
            if( !validContainer ) throw new IllegalArgumentException("The container [" + id + "] does not appear to exist for type [" + getType() + "]");
            if( transaction && !validTrans ) throw new IllegalArgumentException("The transaction [" + id + "] does not appear to exist for type [" + getType() + "]");
        }
        
        public boolean isTransaction() {
            return transaction;
        }
        
        public String[] getContainerId() {
            return containerId;
        }
        
        public String getTransactionId() {
            return transactionId;
        }
    }
}
