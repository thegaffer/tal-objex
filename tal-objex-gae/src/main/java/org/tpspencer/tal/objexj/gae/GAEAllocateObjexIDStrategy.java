package org.tpspencer.tal.objexj.gae;

import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.gae.object.ContainerBean;
import org.tpspencer.tal.objexj.object.DefaultObjexID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;

/**
 * This ID Strategy creates an ObjexID by allocating it
 * using the GAE Data Service.
 * 
 * FUTURE: New implementation that obtains blocks in a transaction
 * 
 * @author Tom Spencer
 */
public final class GAEAllocateObjexIDStrategy implements ObjexIDStrategy {
    private static final GAEAllocateObjexIDStrategy INSTANCE = new GAEAllocateObjexIDStrategy();
    
    /**
     * Hidden constructor, there need only be 1 {@link GAEAllocateObjexIDStrategy} 
     * instance in the classloader.
     */
    private GAEAllocateObjexIDStrategy() {   
    }
    
    /**
     * @return The single {@link GAEAllocateObjexIDStrategy} instance in classloader
     */
    public static GAEAllocateObjexIDStrategy getInstance() {
        return INSTANCE;
    }

    /**
     * Gets and allocates ID from range. 
     * 
     * TODO: Should we just pass through a hardcoded type here?
     */
    public ObjexID createId(EditableContainer container, Class<? extends ObjexObjStateBean> stateType, String type, ObjexObj obj) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        
        Key root = KeyFactory.createKey(ContainerBean.class.getSimpleName(), container.getId());
        
        KeyRange range = service.allocateIds(root, stateType.getSimpleName(), 1);
        return new DefaultObjexID(type, range.getStart().getId());
    }
    
    public Key createContainerId(String id) {
        if( id != null ) {
            return KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        }
        else {
            DatastoreService service = DatastoreServiceFactory.getDatastoreService();
            KeyRange range = service.allocateIds(ContainerBean.class.getSimpleName(), 1);
            return range.getStart();
        }
    }
}
