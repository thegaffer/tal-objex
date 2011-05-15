package org.talframework.objexj.runtimes.globals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectWriter;

import com.intersys.globals.Connection;
import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;

/**
 * Very simple implementation of the writer to write into a Globals DB
 *
 * @author Tom Spencer
 */
public class GlobalsObjectWriter extends BaseObjectWriter {
    
    private Connection connection;
    private NodeReference node;
    private Map<String, Object> currentObject;

    public GlobalsObjectWriter() {
        super(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
    }
    
    public void writeObject(GlobalsObjectStrategy strategy, Connection connection, NodeReference node, ObjexObj obj) {
        this.connection = connection;
        this.node = node;
        
        if( currentObject != null ) currentObject = new HashMap<String, Object>();
        else currentObject.clear();
        
        super.write(obj);
        
        // Now write into list using strategy
        ValueList lst = connection.createList();
        for( String field : strategy.getFields() ) {
            lst.append(currentObject.get(field));
        }
        
        this.connection = null;
        this.node = null;
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
        currentObject.put(name, val);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeListProperty(String name, List<String> refs, ObjexFieldType type, boolean persistent) {
        ValueList lst = connection.createList(refs.size());
        for( String ref : refs ) lst.append(ref);
        node.set(lst, name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeSetProperty(String name, Set<String> refs, ObjexFieldType type, boolean persistent) {
        ValueList lst = connection.createList(refs.size());
        for( String ref : refs ) lst.append(ref);
        node.set(lst, name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeMapProperty(String name, Map<String, String> refs, ObjexFieldType type, boolean persistent) {
        ValueList lst = connection.createList(refs.size());
        for( String ref : refs.keySet() ) lst.append(ref + "=" + refs.get(ref));
        node.set(lst, name);
    }
}
