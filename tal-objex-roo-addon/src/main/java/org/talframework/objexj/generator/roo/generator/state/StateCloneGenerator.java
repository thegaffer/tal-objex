package org.talframework.objexj.generator.roo.generator.state;

import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;

/**
 * Writes out the state bean clone method given the
 * proeprties.
 *
 * @author Tom Spencer
 */
public class StateCloneGenerator extends BaseGenerator {
    
    public StateCloneGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        MethodMetadataWrapper clone = new MethodMetadataWrapper("cloneState", TypeConstants.OBJEXSTATEBEAN);
        
        String beanName = typeDetails.getName().getSimpleTypeName(); 
        clone.addBody(beanName + " ret = new " + beanName + "();");
        
        boolean idFound = false;
        boolean parentFound = false;
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField prop = it.next();
            
            if( prop.isNaturalBeanField() ) {
                String name = prop.getName().getSymbolName();
                
                clone.addBody("ret." + name + " = this." + name + ";");
                
                if( name.equals("id") ) idFound = true;
                else if( name.equals("parentId") ) parentFound = true;
            }
        }
        
        if( !idFound ) clone.addBody("ret.id = this.id;");
        if( !parentFound ) clone.addBody("ret.parentId = this.parentId;");
        
        clone.addBody("return ret;");
        clone.addMetadata(builder, typeDetails, typeId);
    }
}
