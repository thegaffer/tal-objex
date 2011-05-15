package org.talframework.objexj.runtimes.globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectReader;

import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;

/**
 * Very simple Globals reader that reads an object in from Globals.
 * 
 * TODO: Use the new GDS when available - might need customisation!
 *
 * @author Tom Spencer
 */
public class GlobalsObjectReader extends BaseObjectReader {
    
    private NodeReference node;
    private Map<String, Object> currentObject;

    public GlobalsObjectReader() {
        super(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
    }
    
    /**
     * Call to read an object in from Globals.
     * 
     * @param strategy
     * @param node
     * @param obj
     */
    public void readObject(GlobalsObjectStrategy strategy, NodeReference node, ObjexObj obj) {
        this.node = node;
        
        // Extract out the basic properties and using the strategy put them in the map
        if( currentObject == null ) currentObject = new HashMap<String, Object>();
        else currentObject.clear();
        
        ValueList lst = node.getList();
        if( lst != null ) {
            int i = 0;
            for( String field : strategy.getFields() ) {
                if( i < lst.length() ) currentObject.put(field, lst.getNextObject());
                i++;
            }
            
            lst.clear();
        }
        
        super.read(obj);
    }
    
    @Override
    protected boolean shouldSet(String name) {
        return true;
    }
    
    @Override
    protected boolean propertyExists(String name, Class<?> expected) {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        return currentObject.get(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getListProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        List<String> ret = null;
        
        ValueList lst = node.getList(name);
        if( lst != null ) {
            ret = new ArrayList<String>();
            for( int i = 0 ; i < lst.length() ; i++ ) {
                ret.add(lst.getNextString());
            }
            
            lst.clear();
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getSetProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        Set<String> ret = null;
        
        ValueList lst = node.getList(name);
        if( lst != null ) {
            ret = new HashSet<String>();
            for( int i = 0 ; i < lst.length() ; i++ ) {
                ret.add(lst.getNextString());
            }
            
            lst.clear();
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getMapProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        Map<String, String> ret = null;
        
        ValueList lst = node.getList(name);
        if( lst != null ) {
            ret = new HashMap<String, String>();
            for( int i = 0 ; i < lst.length() ; i++ ) {
                String ref = lst.getNextString();
                String[] refs = ref.split("=");
                if( refs != null && refs.length == 2 ) ret.put(refs[0], refs[1]);
            }
            
            lst.clear();
        }
        
        return ret;
    }
}
