package org.talframework.objexj.generator.roo.generator.object;

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
public class ObjexObjReaderGenerator extends BaseGenerator implements FieldVisitor {
    
    private MethodMetadataWrapper readerMethod = null;
    
    public ObjexObjReaderGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        readerMethod = new MethodMetadataWrapper(new JavaSymbolName("acceptReader"), JavaType.VOID_PRIMITIVE);
        readerMethod.addParameter("reader", TypeConstants.OBJEXSTATEREADER, null);
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField field = it.next();
            if( field.isNaturalBeanField() ) {
                field.accept(this);
            }
        }
        
        builder.getImportRegistrationResolver().addImport(new JavaType("org.talframework.objexj.ObjexObjStateBean.ObjexFieldType"));
        
        readerMethod.addMetadata(builder, typeDetails, typeId);
    }

    public void visitSimple(SimpleField prop) {
        readerMethod.addBody(prop.getBeanType().getSimpleTypeName() + " " + prop.getBeanName() + " = bean." + prop.getBeanGetterMethodName() + "();");
        readerMethod.addBody(prop.getBeanType().getSimpleTypeName() + " new_" + prop.getBeanName() + " = reader.read(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getBeanTypeName() + ".class, " + prop.getObjexType() + ", true);");
        readerMethod.addBody("if( new_" + prop.getBeanName() + " != " + prop.getBeanName() + " ) " + prop.getSetterMethodName() + "(new_" + prop.getBeanName() + ");");
    }
    
    public void visitReference(SimpleReferenceField prop) {
        readerMethod.addBody("String " + prop.getBeanName() + " = bean." + prop.getBeanGetterMethodName() + "();");
        readerMethod.addBody("String new_" + prop.getBeanName() + " = reader.readReference(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody("if( new_" + prop.getBeanName() + " != " + prop.getBeanName() + " ) " + prop.getSetterMethodName() + "Ref(new_" + prop.getBeanName() + ");");
    }
    
    public void visitList(ListReferenceField prop) {
        readerMethod.addBody("List<String> " + prop.getBeanName() + " = bean." + prop.getBeanGetterMethodName() + "();");
        readerMethod.addBody("List<String> new_" + prop.getBeanName() + " = reader.readReferenceList(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody("if( new_" + prop.getBeanName() + " != " + prop.getBeanName() + " ) " + "set" + prop.getItemReference() + "Refs(new_" + prop.getBeanName() + ");");
    }
    
    public void visitMap(MapReferenceField prop) {
        readerMethod.addBody("Map<String, String> " + prop.getBeanName() + " = bean." + prop.getBeanGetterMethodName() + "();");
        readerMethod.addBody("Map<String, String> new_" + prop.getBeanName() + " = reader.readReferenceMap(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody("if( new_" + prop.getBeanName() + " != " + prop.getBeanName() + " ) " + "set" + prop.getItemReference() + "Refs(new_" + prop.getBeanName() + ");");
    }
}
