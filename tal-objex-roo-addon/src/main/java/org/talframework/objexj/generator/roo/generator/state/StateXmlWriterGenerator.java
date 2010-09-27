package org.talframework.objexj.generator.roo.generator.state;

import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;

/**
 * Writes out the state bean in XML notation
 * 
 * TODO: Write out state bean generator
 *
 * @author Tom Spencer
 */
public class StateXmlWriterGenerator extends BaseGenerator {
    
    public StateXmlWriterGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the reader/writer method for XML
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        
    }

}
