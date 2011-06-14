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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerObjectCache.CacheState;
import org.talframework.objexj.container.impl.DefaultContainerObjectCache;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;
import org.talframework.objexj.object.writer.BaseObjectWriter.ObjectWriterBehaviour;
import org.talframework.objexj.object.writer.MapObjectReader;
import org.talframework.objexj.object.writer.MapObjectWriter;
import org.talframework.objexj.object.writer.TypeConvertor;
import org.talframework.tal.aspects.annotations.Profile;
import org.talframework.tal.aspects.annotations.Trace;

import com.intersys.gds.Connection;
import com.intersys.gds.Document;
import com.intersys.gds.DocumentMap;
import com.intersys.gds.DocumentType;
import com.intersys.gds.schema.DocumentImpl;

/**
 * This class represents the middleware when using the Globals DB. We
 * use here the GDS project (or an edited version of it) to store the
 * objects inside Globals
 *
 * @author Tom Spencer
 */
public final class GlobalsMiddleware implements ContainerMiddleware {

    /** Member holds the document map for this type of document */
    private final DocumentMap documentMap;
    
    /** The ID of the container (in its constituent parts) - if null the container is new */
    private String[] id;
    /** The version of the container at the point it was opened */
    private long version;
    /** Member holds if we are currently open or not */
    private boolean open;
    
    /** If the container is new, holds the last ID dished out */
    private long lastId = 1;
    
    /** Member holds the cached object reader for this class - do not use directly */
    private MapObjectReader reader;
    /** Member holds the cache object writer for this class - do not use directly */
    private MapObjectWriter writer;
    
    public GlobalsMiddleware(String[] id, boolean open, DocumentMap documentMap) {
        this.id = id;
        this.version = 1;
        this.open = open;
        this.documentMap = documentMap;
    }
    
    /**
     * Helper to check the container already exists in Globals. If 
     * it does not then an {@link IllegalStateException} is thrown 
     * if throwIfNot is true. 
     * 
     * @param throwIfNot If true an exception is thrown if not existing
     * @return True if the container is existing, false otherwise
     */
    private boolean checkExisting(boolean throwIfNot) {
        boolean ret = id != null;
        if( !ret && throwIfNot ) throw new IllegalStateException("The container is not an existing contianer");
        return ret;
    }
    
    /**
     * Helper to check the container is open. If it is not then an
     * {@link IllegalStateException} is thrown if throwIfNot is true.
     * Note: the container might be new, this does not mean it 
     * exists in Globals yet.
     * 
     * @param throwIfNot If true an exception is thrown if not open
     * @return True if the container is open, false otherwise
     */
    private boolean checkOpen(boolean throwIfNot) {
        if( !open && throwIfNot ) throw new IllegalStateException("The container is not open");
        return open;
    }
    
    /**
     * @return The map object reader to use for retrieving objects from storage
     */
    private MapObjectReader getReader() {
        if( reader == null ) {
            reader = new MapObjectReader(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
            reader.addConvertor(Date.class, GlobalsDateConvertor.INSTANCE);
            reader.addConvertor(Boolean.class, GlobalsBooleanConvertor.INSTANCE);
            reader.addConvertor(boolean.class, GlobalsBooleanConvertor.INSTANCE);
        }
        
        return reader;
    }
    
    /**
     * @return The map object writer to use for storing objects
     */
    private MapObjectWriter getWriter() {
        if( writer == null ) {
            writer = new MapObjectWriter(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
            writer.addConvertor(Date.class, GlobalsDateConvertor.INSTANCE);
            writer.addConvertor(Boolean.class, GlobalsBooleanConvertor.INSTANCE);
            writer.addConvertor(boolean.class, GlobalsBooleanConvertor.INSTANCE);
        }
        
        return writer;
    }
    
    /////////////////////////////////////////////////////////////
    // Middleware Methods
    
    @Override
    public ContainerObjectCache init(Container container) {
        // FUTURE: When supporting suspending, need to get existing objects
        return new DefaultContainerObjectCache(version);
    }
    
    /**
     * This implementation builds the ID from it's constituent parts
     * and returns it.
     */
    @Override
    public String getContainerId() {
        if( !checkExisting(false) ) return null;
        
        StringBuilder builder = new StringBuilder();
        for( Object o : id ) {
            if( builder.length() > 0 ) builder.append('/');
            builder.append(o.toString()); 
        }
        
        return builder.toString();
    }
    
    /**
     * This implementation directly goes to the node to see if the
     * container exists. Due to the way globals actually stores data
     * there is a very high chance that this is already cached in
     * memory anyway so it's cost is low. 
     */
    @Override
    @Trace
    @Profile
    public boolean exists(ObjexID id, boolean accurate) {
        if( !checkExisting(false) ) return false;
        
        return documentMap.containsObjectKey(this.id, id.getType(), id.getId());
    }
    
    /**
     * The object, if it exists, is loaded from the globals node
     */
    @Override
    @Trace
    @Profile
    public ObjexObj loadObject(ObjexObj obj) {
        if( !checkExisting(false) ) return null;
        
        ObjexID objectId = obj.getId();
        
        DocumentType gdsObjectType = documentMap.getConnection().getDocumentType(obj.getType());
        Document doc = documentMap.loadObject(id, gdsObjectType, objectId.getType(), objectId.getId());
        MapObjectReader reader = getReader();
        reader.readObject(doc, obj);
        
        // If successful return the object
        return obj;
    }
    
    /**
     * In this implementation we just call loadObject, there is no
     * method to get more than 1 node at a time and given that it
     * is likely the whole document is stored together it is probably
     * in cache anyway. 
     */
    @Override
    @Profile
    public Map<ObjexID, ObjexObj> loadObjects(ObjexObj... objs) {
        Map<ObjexID, ObjexObj> ret = new HashMap<ObjexID, ObjexObj>();
        
        for( ObjexObj obj : objs ) {
            obj = loadObject(obj);
            if( obj != null ) ret.put(obj.getId(), obj);
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return !checkExisting(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return checkOpen(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Trace
    public void open() {
        if( open ) throw new IllegalStateException("Cannot open as container already open: " + getContainerId());
        // FUTURE: Log we are open inside the container, perhaps even prevent if open by someone else
        open = true;
    }
    
    /**
     * This method uses the increment functionality in Globals. This
     * will waste IDs if the container is not saved, but this is no
     * real issue.
     */
    @Override
    @Trace
    public ObjexID getNextObjectId(String type) {
        checkOpen(true);
        
        if( isNew() ) return new DefaultObjexID(type, ++lastId);
        else return new DefaultObjexID(type, documentMap.nextObjectId(id));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Trace
    @Profile
    public String save(ContainerObjectCache cache, String status, Map<String, String> header) {
        checkOpen(true);
        
        Connection connection = documentMap.getConnection();
        
        MapObjectWriter writer = getWriter();
        Document gdsObject = new DocumentImpl();
        
        try {
            connection.startTransaction();
            
            // a. See if new container, if it is, create the new one
            if( id == null ) {
                id = new String[]{Long.toString(documentMap.nextDocumentId())};
                version = 1;
            }
            else {
                ++version;
            }
            
            Document doc = new DocumentImpl();
            doc.put("version", version);
            documentMap.store(doc, id);
            
            // b. Save any new or changed objects
            Set<ObjexObj> objs = cache.getObjects(CacheState.NEWORCHANGED);
            for( ObjexObj obj : objs ) {
                gdsObject.clear();
                
                ObjexID objId = obj.getId();
                DocumentType gdsObjectType = documentMap.getConnection().getDocumentType(obj.getType());
                writer.writeObject(gdsObject, null, obj);
                documentMap.storeObject(id, gdsObjectType, gdsObject, objId.getType(), objId.getId());
            }
            
            // c. Removed any deleted objects
            objs = cache.getObjects(CacheState.REMOVED);
            for( ObjexObj obj : objs ) {
                ObjexID objId = obj.getId();
                documentMap.removeObject(id, objId.getType(), objId.getId());
            }
            
            connection.commit();
        }
        catch( Throwable t ) {
            connection.rollback();
        }
        
        return getContainerId();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Trace
    @Profile
    public String suspend(ContainerObjectCache cache) {
        checkOpen(true);
        
        throw new UnsupportedOperationException("Suspending not supported currently");
        // FUTURE: Save into a new transaction
    }
    
    /**
     * FUTURE: When we save transactions, if transaction then create new
     */
    @Override
    public void clear(ContainerObjectCache cache) {
        checkOpen(true);
        
        // FUTURE: Remove the transaction
        
        open = false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerListener(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerListenerForTransaction(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    //////////////////////////////
    
    /**
     * Convertor class for storing dates as longs
     *
     * @author Tom Spencer
     */
    private static class GlobalsDateConvertor implements TypeConvertor {
        public static final GlobalsDateConvertor INSTANCE = new GlobalsDateConvertor();
        
        @Override
        public Object toStorage(Object val) {
            return Date.class.cast(val).getTime();
        }
        
        @Override
        public <T> T fromStorage(Class<T> requiredType, Object val) {
            if( val instanceof Long ) return requiredType.cast(new Date((Long)val));
            else throw new IllegalArgumentException("A date is not being stored as a long, cannot read back!!!");
        }
    }
    
    /**
     * Convertor class for storing booleans as integers
     *
     * @author Tom Spencer
     */
    private static class GlobalsBooleanConvertor implements TypeConvertor {
        public static final GlobalsBooleanConvertor INSTANCE = new GlobalsBooleanConvertor();
        
        @Override
        public Object toStorage(Object val) {
            int ret = 0;
            if( Boolean.class.isInstance(val) ) ret = ((Boolean)val).booleanValue() ? 1 : 0;
            return ret;
        }
        
        @Override
        public <T> T fromStorage(Class<T> requiredType, Object val) {
            boolean ret = false;
            if( val instanceof Number ) ret = ((Number)val).intValue() == 1;
            return requiredType.cast(ret);
        }
    }
}
