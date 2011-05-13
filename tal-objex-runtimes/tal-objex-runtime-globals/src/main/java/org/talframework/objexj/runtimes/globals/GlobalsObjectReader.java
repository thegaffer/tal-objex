package org.talframework.objexj.runtimes.globals;

import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectReader;

import com.intersys.globals.ValueList;

public class GlobalsObjectReader extends BaseObjectReader {
    
    private GlobalsObjectStrategy strategy;
    private Map<String, >

    public GlobalsObjectReader() {
        super(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
    }
    
    @Override
    protected boolean shouldSet(String name) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    protected boolean propertyExists(String name, Class<?> expected) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    protected Object getProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected Object getListProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected Object getSetProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected Object getMapProperty(String name, Class<?> expected, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        return null;
    }
}
