package org.talframework.objexj.runtime.globals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ObjexObjStateBean.ObjexFieldType;
import org.talframework.objexj.ObjexStateReader;

import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;

/**
 * This class reads in all the values of an state of an object.
 * 
 * TODO: As with the writer, this should be changes so that simple properties and single references are stored in a list in the node
 *
 * @author Tom Spencer
 */
public class GlobalsObjectReader implements ObjexStateReader {

    private final NodeReference node;
    
    public GlobalsObjectReader(NodeReference node) {
        this.node = node;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T read(String name, T current, Class<T> expected, ObjexObjStateBean.ObjexFieldType type, boolean persistent) {
        T ret = null;
        
        switch(type) {
        case BOOL:
            ret = expected.cast(node.getInt(name) == 1);
            break;
            
        case SHORT_BLOB:
        case BLOB:
            ret = expected.cast(node.getBytes(name));
            break;
            
        case DATE:
            ret = expected.cast(new Date(node.getLong(name)));
            break;
            
        case OBJECT:
            throw new GlobalsObjexException("Embedded objects not currently supported");
            
        default:
            ret = expected.cast(node.getObject(name));
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    public String readReference(String name, String current, ObjexFieldType type, boolean persistent) {
        return node.getString(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> readReferenceList(String name, List<String> current, ObjexFieldType type, boolean persistent) {
        ValueList list = node.getList(name);
        
        List<String> ret = new ArrayList<String>();
        if( list != null ) {
            String val = list.getNextString();
            while( val != null ) {
                ret.add(val);
                val = list.getNextString();
            }
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<String, String> readReferenceMap(String name, Map<String, String> current, ObjexFieldType type, boolean persistent) {
        Map<String, String> ret = new HashMap<String, String>();
        
        if( node.hasSubnodes(name) ) {
            String element = node.nextSubscript(name);
            while( element != null ) {
                ret.put(element, node.getString(name, element));
                element = node.nextSubscript(name);
            }
        }

        return ret;
    }
}
