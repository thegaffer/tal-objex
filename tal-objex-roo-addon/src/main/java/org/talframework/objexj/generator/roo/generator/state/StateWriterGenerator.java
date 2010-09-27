package org.talframework.objexj.generator.roo.generator.state;

import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.fields.FieldVisitor;
import org.talframework.objexj.generator.roo.fields.ListReferenceField;
import org.talframework.objexj.generator.roo.fields.MapReferenceField;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.fields.SimpleField;
import org.talframework.objexj.generator.roo.fields.SimpleReferenceField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;

/**
 * Writers out a state bean writer using basic HTTP
 * notation (property=value)
 *
 * @author Tom Spencer
 */
public class StateWriterGenerator extends BaseGenerator implements FieldVisitor {
    
    private MethodMetadataWrapper method = null;
    private boolean includeIterator = false;
    
    public StateWriterGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        includeIterator = false;
        
        method = new MethodMetadataWrapper(new JavaSymbolName("writeBean"), JavaType.VOID_PRIMITIVE);
        method.addParameter("writer", "java.io.Writer", null);
        method.addParameter("id", TypeConstants.OBJEXID, null);
        method.addParameter(new JavaSymbolName("prefix"), JavaType.STRING_OBJECT, null);
        
        method.addBody("StringBuilder builder = new StringBuilder();");
        method.addBody("builder.append(prefix).append(\".id=\").append(id.toString()).append('\\n');");
        method.addBody("if( parentId != null ) builder.append(prefix).append(\".parentId=\").append(parentId).append('\\n');");
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField field = it.next();
            if( field.isNaturalBeanField() ) {
                field.accept(this);
            }
        }
        
        if( includeIterator ) builder.getImportRegistrationResolver().addImport(new JavaType("java.util.Iterator"));
        
        method.addMetadata(builder, typeDetails, typeId);
    }

    public void visitSimple(SimpleField prop) {
        String name = prop.getBeanName().getSymbolName();
        boolean primitive = prop.getBeanType().isPrimitive();
        if( primitive ) method.addBody("builder.append(prefix).append(\"." + name + "=\").append(" + name + ").append('\\n');");
        else method.addBody("if( " + name + " != null ) builder.append(prefix).append(\"." + name + "=\").append(" + name + ").append('\\n');");
    }
    
    public void visitReference(SimpleReferenceField prop) {
        String name = prop.getBeanName().getSymbolName();
        method.addBody("if( " + name + " != null ) builder.append(prefix).append(\"." + name + "=\").append(" + name + ").append('\\n');");
    }
    
    public void visitList(ListReferenceField prop) {
        includeIterator = true;
        
        String name = prop.getBeanName().getSymbolName();
        method.addBody("if( " + name + " != null ) {");
        method.addBody("\tIterator<String> it = " + name + ".iterator();");
        method.addBody("\tint index = 0;");
        method.addBody("\twhile( it.hasNext() ) {");
        method.addBody("\t\tbuilder.append(prefix).append(\"." + name + "[index]=\").append(it.next()).append('\\n');");
        method.addBody("\t\tindex++;");
        method.addBody("\t}");
        method.addBody("}");
    }
    
    public void visitMap(MapReferenceField prop) {
        includeIterator = true;
        
        String name = prop.getBeanName().getSymbolName();
        method.addBody("if( " + name + " != null ) {");
        method.addBody("\tIterator<String> it = " + name + ".iterator();");
        method.addBody("\tint index = 0;");
        method.addBody("\twhile( it.hasNext() ) {");
        method.addBody("\t\tString key = it.next();");
        method.addBody("\t\tbuilder.append(prefix).append(\"." + name + "[key]=\").append(" + name + ".get(key)).append('\\n');");
        method.addBody("\t\tindex++;");
        method.addBody("\t}");
        method.addBody("}");
    }
}
