package org.talframework.objexj.runtimes.gae;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectWriter;

import com.google.appengine.api.datastore.Entity;

/**
 * This class writes a single object out into an entity that can
 * be added or updated in the datastore.
 *
 * @author Tom Spencer
 */
public class GAEObjectWriter extends BaseObjectWriter {
    
    /** Transient internal member holding the current entity */
    private Entity currentEntity;
    
    public GAEObjectWriter() {
        super(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
    }
    
    /**
     * Call to write a single object into a GAE entity
     * 
     * @param entity The entity to read from
     * @param obj The object to read into
     */
    public void writeObject(Entity entity, ObjexObj obj) {
        currentEntity = entity;
        super.write(obj);
        currentEntity = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldWrite(String name, Object val) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeProperty(String name, Object val, ObjexFieldType type, boolean persistent) {
        if( !persistent ) return;
        
        switch(type) {
        case MEMO:
        case BLOB:
        case SHORT_BLOB:
            currentEntity.setUnindexedProperty(name, val);
            
        default:
            currentEntity.setProperty(name, val);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeListProperty(String name, List<String> refs, ObjexFieldType type, boolean persistent) {
        writeProperty(name, refs, type, persistent);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeSetProperty(String name, Set<String> refs, ObjexFieldType type, boolean persistent) {
        writeProperty(name, refs, type, persistent);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeMapProperty(String name, Map<String, String> refs, ObjexFieldType type, boolean persistent) {
        writeProperty(name, refs, type, persistent);
    }
    
}
