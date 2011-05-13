package org.talframework.objexj.runtimes.globals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexObj.ObjexFieldType;
import org.talframework.objexj.object.writer.BaseObjectWriter;

public class GlobalsObjectWriter extends BaseObjectWriter {

    public GlobalsObjectWriter() {
        super(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
    }
    
    @Override
    protected boolean shouldWrite(String name, Object val) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    protected void writeProperty(String name, Object val, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeListProperty(String name, List<String> refs, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeSetProperty(String name, Set<String> refs, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeMapProperty(String name, Map<String, String> refs, ObjexFieldType type, boolean persistent) {
        // TODO Auto-generated method stub
        
    }
}
