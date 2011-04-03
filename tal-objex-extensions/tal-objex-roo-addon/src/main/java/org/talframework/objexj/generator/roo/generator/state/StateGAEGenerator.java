package org.talframework.objexj.generator.roo.generator.state;

import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;

/**
 * Writes out a Google App Engine read/write method
 * 
 * TODO: Implement GAE Reader/Writer
 *
 * @author Tom Spencer
 */
public class StateGAEGenerator extends BaseGenerator {
    
    public StateGAEGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the read/writer methods for the given
     * bean.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        
    }
}
