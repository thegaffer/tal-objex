package org.talframework.objexj.generator.roo.generator.state;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.EnumAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.model.EnumDetails;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.annotations.ObjexObjStateBeanAnnotation;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.ConstructorMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.PropertyMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * Generates out all of the boiler plate state bean methods
 * and adds in the ID/ParentID fields.
 *
 * @author Tom Spencer
 */
public class StateGenerator extends BaseGenerator {
    
    private ObjexObjStateBeanAnnotation annotationValues;

    public StateGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    public void generate(ObjexObjStateBeanAnnotation annotationValues) {
        this.annotationValues = annotationValues;
        
        // Make the object a JAXB one
        if( TypeDetailsUtil.getAnnotation(typeDetails, TypeConstants.XML_ROOT) == null ) {
            List<AnnotationAttributeValue<?>> values = new ArrayList<AnnotationAttributeValue<?>>();
            values.add(new StringAttributeValue(new JavaSymbolName("name"), annotationValues.getName()));
            builder.addTypeAnnotation(new DefaultAnnotationMetadata(TypeConstants.XML_ROOT_TYPE, (List<AnnotationAttributeValue<?>>)new ArrayList<AnnotationAttributeValue<?>>()));
        }
        
        addIdField();
        addParentIdField();
        addEditable();
        addTypeGetter();
        addInit();
        addDefaultCons();
    }
    
    private void addDefaultCons() {
        ConstructorMetadataWrapper con = new ConstructorMetadataWrapper();
        
        con.addBody("super();");
        con.addBody("_editable = false;");
        
        con.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addIdField() {
        PropertyMetadataWrapper prop = new PropertyMetadataWrapper(new JavaSymbolName("id"), JavaType.STRING_OBJECT);
        prop.addAnnotation("javax.jdo.annotations.PrimaryKey");
        prop.addAnnotation("javax.jdo.annotations.Persistent", 
                new EnumAttributeValue(new JavaSymbolName("valueStrategy"), new EnumDetails(new JavaType("javax.jdo.annotations.IdGeneratorStrategy"), new JavaSymbolName("IDENTITY"))));
        prop.addAnnotation("javax.jdo.annotations.Extension",
                new StringAttributeValue(new JavaSymbolName("vendorName"), "datanucleus"), 
                new StringAttributeValue(new JavaSymbolName("key"), "gae.encoded-pk"),
                new StringAttributeValue(new JavaSymbolName("value"), "true"));
        prop.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName("getId"), prop.getType());
        getter.addAnnotation(TypeConstants.XML_ATTRIBUTE);
        getter.addAnnotation(TypeConstants.XML_ID);
        getter.addBody("return this.id;");
        getter.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName("setId"), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("if( this.id != null ) throw new IllegalArgumentException(\"You cannot set a parent ID on an object once it is set\");");
        setter.addBody("this.id = val;");
        setter.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addParentIdField() {
        PropertyMetadataWrapper prop = new PropertyMetadataWrapper(new JavaSymbolName("parentId"), JavaType.STRING_OBJECT);
        prop.addAnnotation("javax.jdo.annotations.Persistent", 
                new StringAttributeValue(new JavaSymbolName("column"), "parentId"));
        prop.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName("getParentId"), prop.getType());
        getter.addAnnotation(TypeConstants.XML_ATTRIBUTE);
        getter.addBody("return this.parentId;");
        getter.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(new JavaSymbolName("setParentId"), JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("if( this.parentId != null ) throw new IllegalArgumentException(\"You cannot set a parent ID on an object once it is set\");");
        setter.addBody("this.parentId = val;");
        setter.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addEditable() {
        // editable property
        PropertyMetadataWrapper prop = new PropertyMetadataWrapper(new JavaSymbolName("_editable"), JavaType.BOOLEAN_PRIMITIVE);
        prop.addAnnotation("javax.jdo.annotations.NotPersistent");
        prop.addModifier(Modifier.PRIVATE);
        prop.addModifier(Modifier.TRANSIENT);
        prop.addMetadata(builder, typeDetails, typeId);
        
        // isEditable method
        MethodMetadataWrapper isMethod = new MethodMetadataWrapper(new JavaSymbolName("isEditable"), JavaType.BOOLEAN_PRIMITIVE);
        isMethod.addAnnotation(TypeConstants.XML_TRANSIENT);
        isMethod.addBody("return _editable;");
        isMethod.addMetadata(builder, typeDetails, typeId);
        
        // setEditable method
        MethodMetadataWrapper setMethod = new MethodMetadataWrapper(new JavaSymbolName("setEditable"), JavaType.VOID_PRIMITIVE);
        setMethod.addBody("_editable = true;");
        setMethod.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addTypeGetter() {
        MethodMetadata mm = TypeDetailsUtil.getMethod(typeDetails, "getObjexObjType", null);
        if( mm != null ) return;
        
        String type = annotationValues.getName();
        
        // Backup, although I don't possible for this to be true
        if( type == null ) {
            type = typeDetails.getName().getSimpleTypeName();
            if( type.endsWith("Bean") && type.length() > 4 ) type = type.substring(0, type.length() - 4);
            if( type.endsWith("State") && type.length() > 5 ) type = type.substring(0, type.length() - 5);
        }
        
        MethodMetadataWrapper typeMethod = new MethodMetadataWrapper(new JavaSymbolName("getObjexObjType"), JavaType.STRING_OBJECT);
        typeMethod.addAnnotation(TypeConstants.XML_TRANSIENT);
        typeMethod.addBody("return \"" + type + "\";");
        typeMethod.addMetadata(builder, typeDetails, typeId);
    }
    
    private void addInit() {
        MethodMetadata mm = TypeDetailsUtil.getMethod(typeDetails, "preSave", null);
        if( mm != null ) return;
        
        MethodMetadataWrapper create = new MethodMetadataWrapper(new JavaSymbolName("create"), JavaType.VOID_PRIMITIVE);
        create.addParameter("parentId", TypeConstants.OBJEXID, null);
        create.addBody("this.parentId = parentId != null ? parentId.toString() : null;");
        create.addMetadata(builder, typeDetails, typeId);
        
        MethodMetadataWrapper preSave = new MethodMetadataWrapper(new JavaSymbolName("preSave"), JavaType.VOID_PRIMITIVE);
        preSave.addParameter("id", "java.lang.Object", null);
        preSave.addBody("this.id = id != null ? id.toString() : null;");
        preSave.addMetadata(builder, typeDetails, typeId);
    }
}
