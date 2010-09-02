package org.talframework.objexj.generator.roo.fields;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;

/**
 * This class scans the fields of the type and creates
 * {@link ObjexObjProperty} instances for each one found.
 * 
 * @author Tom Spencer
 */
public class PropertyCompiler {
    
    public PropertyCompiler() {
        
    }
    
    public List<ObjexObjProperty> compile(ClassOrInterfaceTypeDetails typeDetails) {
        List<ObjexObjProperty> ret = new ArrayList<ObjexObjProperty>();
        
        List<? extends FieldMetadata> fields = typeDetails.getDeclaredFields();
        if( fields != null ) {
            Iterator<? extends FieldMetadata> it = fields.iterator();
            while( it.hasNext() ) {
                FieldMetadata fm = it.next();
                if( Modifier.isStatic(fm.getModifier()) ) continue;
                
                ret.add(new ObjexObjProperty(fm));
            }
        }
        
        return ret;
    }
}
