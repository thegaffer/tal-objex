package org.talframework.objexj.runtimes.globals;

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
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.exceptions.ObjectTypeNotFoundException;

import com.intersys.globals.Connection;
import com.intersys.globals.ConnectionContext;
import com.intersys.globals.NodeReference;

/**
 * This class represents the middleware when using the Globals DB.
 *
 * @author Tom Spencer
 */
public class GlobalsMiddleware implements ContainerMiddleware {

    /** Contains the map of object strategies we use in this container */
    private Map<String, GlobalsObjectStrategy> globalsStrategies;
    
    /** The type of this container */
    private final String type;
    /** Holds the root node for the container (if it exists) */
    private NodeReference node;
    /** This is the subscript count at the root of the containers node */
    private int containerNodeCount;
    
    /** The ID of the container - if null the container is new */
    private Object[] id;
    /** The version of the container at the point it was opened */
    private final long version;
    
    /** If the container is new, holds the last ID dished out */
    private long lastId = 1;
    
    private NodeReference getContainerNode() {
        // TODO: Implement!!
        node.setSubscriptCount(containerNodeCount);
        return node;
    }
    
    private NodeReference getObjectNode(ObjexID id) {
        NodeReference node = getContainerNode();
        node.appendSubscript("objects");
        node.appendSubscript(id.getType());
        if( id.getId() instanceof Long ) node.appendSubscript((Long)id.getId());
        else node.appendSubscript(id.getId().toString());
        return node;
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
        // TODO: Implement
        return false;
    }
    
    /////////////////////////////////////////////////////////////
    // Middleware Methods
    
    @Override
    public ContainerObjectCache init(Container container) {
        // TODO Auto-generated method stub
        return null;
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
    public boolean exists(ObjexID id, boolean accurate) {
        if( !checkExisting(false) ) return false;
        
        return getContainerNode().exists("objects", id.getType(), id.getId());
    }
    
    /**
     * The object, if it exists, is loaded from the globals node
     */
    @Override
    public ObjexObj loadObject(ObjexObj obj) {
        if( !checkExisting(false) ) return null;
        
        GlobalsObjectStrategy strategy = globalsStrategies.get(obj.getType());
        if( strategy == null ) throw new ObjectTypeNotFoundException(obj.getType(), new RuntimeException("No mapping for object type in Globals container"));
        
        NodeReference node = getObjectNode(obj.getId());
        
        GlobalsObjectReader reader = new GlobalsObjectReader();
        reader.readObject(strategy, node, obj);
        
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
    public void open() {
        
        // TODO Auto-generated method stub
        
    }
    
    /**
     * This method uses the increment functionality in Globals. This
     * will waste IDs if the container is not saved, but this is no
     * real issue.
     */
    @Override
    public ObjexID getNextObjectId(String type) {
        checkOpen(true);
        
        long newId = getContainerNode().increment(1, "objects");
        return new DefaultObjexID(type, newId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String save(ContainerObjectCache cache, String status, Map<String, String> header) {
        Connection connection = ConnectionContext.getConnection();
        
        try {
            connection.startTransaction();
            
            // a. See if new container, if it is, create the new one
            
            // b. Save any new or changed objects
            GlobalsObjectWriter writer = new GlobalsObjectWriter();
            Set<ObjexObj> objs = cache.getObjects(CacheState.NEWORCHANGED);
            for( ObjexObj obj : objs ) {
                GlobalsObjectStrategy strategy = globalsStrategies.get(obj.getType());
                NodeReference node = getObjectNode(obj.getId());
                writer.writeObject(strategy, connection, node, obj);
            }
            
            // c. Removed any deleted objects
            objs = cache.getObjects(CacheState.REMOVED);
            for( ObjexObj obj : objs ) {
                NodeReference node = getObjectNode(obj.getId());
                node.kill();
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
    public String suspend(ContainerObjectCache cache) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * FUTURE: When we save transactions, if transaction then create new
     */
    @Override
    public void clear(ContainerObjectCache cache) {
        // TODO Auto-generated method stub
        
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
}
