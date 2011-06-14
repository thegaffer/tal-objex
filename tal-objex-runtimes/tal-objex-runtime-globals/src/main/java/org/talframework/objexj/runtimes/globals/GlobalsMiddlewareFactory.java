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

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.container.ObjectStrategy.PropertyCharacteristic;
import org.talframework.objexj.container.ObjectStrategy.PropertyStrategy;

import com.intersys.gds.Connection;
import com.intersys.gds.DocumentType;
import com.intersys.gds.ElementType.Type;
import com.intersys.gds.schema.DocumentTypeImpl;
import com.intersys.gds.schema.GDSConnectionImpl;

public class GlobalsMiddlewareFactory implements ContainerMiddlewareFactory {
    
    /** The name of the container type */
    private final String name;
    /** Member holds the GDS connection for this factory */
    private final Connection connection;
    
    /**
     * Constructs the middleware and ensures all types of objects are described
     * to GDS.
     * 
     * @param strategy The strategy for the container this instance will be connected to
     */
    public GlobalsMiddlewareFactory(Connection connection, ContainerStrategy strategy) {
        this.connection = connection;
        if( !connection.isConnected() ) this.connection.connect("", "", "");
        this.name = strategy.getContainerName();
        
        // Create the base document type
        DocumentType docType = connection.getDocumentType(strategy.getContainerName());
        if( docType == null ) docType = DocumentTypeImpl.createDocumentType(strategy.getContainerName());
        docType.setDatatype("version", Type.INTEGER);
        connection.saveDocumentType(docType);
        
        // Ensure we have a schema for each object type
        for( String objectName : strategy.getObjectNames() ) {
            DocumentType objType = connection.getDocumentType(objectName);
            if( objType == null ) objType = DocumentTypeImpl.createDocumentType(objectName);
            
            objType.setDatatype("id", Type.STRING);
            objType.setDatatype("parentId", Type.STRING);
            objType.setDatatype("objexType", Type.STRING);
            
            ObjectStrategy objectStrategy = strategy.getObjectStrategy(objectName);
            for( String prop : objectStrategy.getPropertyNames() ) {
                PropertyStrategy propStrategy = objectStrategy.getProperty(prop);
                
                if( !propStrategy.isCharacteristic(PropertyCharacteristic.PERSISTENT) ) continue;
                
                switch(propStrategy.getObjexType()) {
                case REFERENCE:
                case OWNED_REFERENCE:
                case STRING:
                case MEMO:
                case BLOB_REFERENCE:
                case USER:
                    objType.setDatatype(prop, Type.STRING);
                    break;
                    
                case NUMBER:
                    switch(propStrategy.getPropertyType()) {
                    case DOUBLE:
                        if( double.class.equals(propStrategy.getRawType()) ) objType.setDatatype(prop, Type.DOUBLE);
                        else if( Double.class.equals(propStrategy.getRawType()) ) objType.setDatatype(prop, Type.DOUBLE_WRAPPER);
                        break;
                        
                    case LONG:
                        if( long.class.equals(propStrategy.getRawType()) ) objType.setDatatype(prop, Type.LONG);
                        else if( Long.class.equals(propStrategy.getRawType()) ) objType.setDatatype(prop, Type.LONG_WRAPPER);
                        break;
                        
                    case INT:
                        if( int.class.equals(propStrategy.getRawType()) ) objType.setDatatype(prop, Type.INTEGER);
                        else if( Integer.class.equals(propStrategy.getRawType()) ) objType.setDatatype(prop, Type.INTEGER_WRAPPER);
                        break;
                        
                    default:
                        throw new IllegalArgumentException("The Globals runtime cannot supported this property type yet! " + objectName + ":" + prop);
                    }
                    
                    break;
                    
                case BOOL:
                    objType.setDatatype(prop, Type.INTEGER);
                    break;
                    
                case BLOB:
                    objType.setDatatype(prop, Type.LONG_BYTE_ARRAY);
                    break;
                    
                case SHORT_BLOB:
                    objType.setDatatype(prop, Type.BYTE_ARRAY);
                    break;
                    
                case DATE:
                    objType.setDatatype(prop, Type.LONG);
                    break;
                    
                case PARENT_ID:
                    objType.setDatatype(prop, Type.STRING);
                    break;
                    
                case OBJECT:
                    throw new IllegalArgumentException("The Globals runtime cannot support embedded objects yet! " + objectName + ":" + prop);
                }
            }
            
            connection.saveDocumentType(objType);
        }
    }
    
    @Override
    public ContainerMiddleware createContainer(ContainerStrategy strategy) {
        return new GlobalsMiddleware(null, true, connection.getDocumentMap(name));
    }
    
    @Override
    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        String[] ids = id.split("/");
        if( ids.length < 2 ) throw new IllegalArgumentException("The container ID is not valid for this container, must have 2 parts and type and an ID: " + id);
        if( !(ids[0].equals(name)) ) throw new IllegalArgumentException("The container ID does not appear to be for this middleware type [" + name + "]: " + id);
            
        String[] containerIds = new String[ids.length - 1];
        System.arraycopy(ids, 1, containerIds, 0, containerIds.length);
        
        return new GlobalsMiddleware(containerIds, false, connection.getDocumentMap(name));
    }
    
    @Override
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId) {
        throw new UnsupportedOperationException("Currently it is not possible to have transactions on Globals containers");
        
        // Break down transaction id on /
        // First part must match container type
        // Last part will be a unique transaction id (node) stored under the doc with user name etc
        // Middle parts (must be 1) will be actual id
    }
}
