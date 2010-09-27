package org.talframework.objexj.generator.roo.generator.object;

import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;

/**
 * Writes out the object in XML notation
 * 
 * TODO: Implement XML Writer
 *
 * @author Tom Spencer
 */
public class ObjexXmlWriterGenerator extends BaseGenerator {
    
    public ObjexXmlWriterGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the XML writer for all the
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
    }
}
