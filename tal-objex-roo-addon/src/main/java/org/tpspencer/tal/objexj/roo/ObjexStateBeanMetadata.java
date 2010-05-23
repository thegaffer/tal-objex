package org.tpspencer.tal.objexj.roo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.DefaultFieldMetadata;
import org.springframework.roo.classpath.details.DefaultMethodMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.EnumAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.EnumDetails;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;

/**
 * This class enriches the class, holding a {@link ObjexStateBean}
 * annotation, to which it is attached with the persistence
 * tags and ensures there is a getter/setter as appropriate for
 * the bean.
 * 
 * @author Tom Spencer
 */
public class ObjexStateBeanMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
    private static final Log logger = LogFactory.getLog(ObjexStateBeanMetadata.class);
    
    private static final String PROVIDES_TYPE_STRING = ObjexStateBeanMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);

    public ObjexStateBeanMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        logger.debug(">>> Creating ObjexStateBean metadata for: " + governorTypeDetails.getName());
        
        // JDO Annotation
        builder.addTypeAnnotation(getDeclaredAnnotation(new JavaType("javax.jdo.annotations.PersistenceCapable")));
        
        // StateBean Interface
        builder.addImplementsType(new JavaType("org.tpspencer.tal.objexj.ObjexObjStateBean"));
        
        // ID Field
        builder.addField(getIdField());
        builder.addMethod(getIdGetter());
        builder.addMethod(getIdSetter());
        
        // TODO: The parent field should be optional
        builder.addField(getParentField());
        builder.addMethod(getParentIdGetter());
        builder.addMethod(getParentIdSetter());
        
        // Type
        builder.addMethod(getTypeMethod());
        
        // Add serial version UID
        // TODO: Add version to our own annotation to get it from
        builder.addField(getSerialVersionField());
        
        // Create the ITD Type
        itdTypeDetails = builder.build();
    }
    
    /**
     * Finds the target annotation on the type or from another metadata item
     * or creates it if is does not exist.
     * 
     * @return The target annotation metadata
     */
    private AnnotationMetadata getDeclaredAnnotation(JavaType target) {
        List<? extends AnnotationMetadata> annotations = governorTypeDetails.getTypeAnnotations();
        
        AnnotationMetadata ret = null;
        if( annotations != null ) {
            Iterator<? extends AnnotationMetadata> it = annotations.iterator();
            while( it.hasNext() && ret == null ) {
                AnnotationMetadata am = it.next();
                if( am.getAnnotationType().equals(target) ) ret = am;
            }
        }
        
        if( ret == null ) {
            List<AnnotationAttributeValue<?>> attrs = new ArrayList<AnnotationAttributeValue<?>>();
            ret = new DefaultAnnotationMetadata(target, attrs);
        }
        
        return ret;
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private FieldMetadata getIdField() {
        FieldMetadata ret = getDeclaredField("id");
        
        if( ret == null ) {
            List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
            
            // Primary Key
            List<AnnotationAttributeValue<?> > attrs = new ArrayList<AnnotationAttributeValue<?>>();
            annotations.add(new DefaultAnnotationMetadata(new JavaType("javax.jdo.annotations.PrimaryKey"), attrs));
            
            // ID Generation - TODO: not sure if this is required
            attrs = new ArrayList<AnnotationAttributeValue<?>>();
            attrs.add(new EnumAttributeValue(new JavaSymbolName("valueStrategy"), new EnumDetails(new JavaType("javax.jdo.annotations.IdGeneratorStrategy"), new JavaSymbolName("IDENTITY"))));
            annotations.add(new DefaultAnnotationMetadata(new JavaType("javax.jdo.annotations.Persistent"), attrs));
            
            // GAE Extensions - TODO: can this be moved out!?!
            attrs = new ArrayList<AnnotationAttributeValue<?>>();
            attrs.add(new StringAttributeValue(new JavaSymbolName("vendorName"), "datanucleus"));
            attrs.add(new StringAttributeValue(new JavaSymbolName("key"), "gae.encoded-pk"));
            attrs.add(new StringAttributeValue(new JavaSymbolName("value"), "true"));
            annotations.add(new DefaultAnnotationMetadata(new JavaType("javax.jdo.annotations.Extension"), attrs));
            
            ret = new DefaultFieldMetadata(getId(), Modifier.PRIVATE, new JavaSymbolName("id"), new JavaType(String.class.getName()), null, annotations);
        }
        
        return ret;
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private FieldMetadata getParentField() {
        FieldMetadata ret = getDeclaredField("id");
        
        if( ret == null ) {
            List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
            ret = new DefaultFieldMetadata(getId(), Modifier.PRIVATE, new JavaSymbolName("parentId"), new JavaType(String.class.getName()), null, annotations);
        }
        
        return ret;
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private FieldMetadata getSerialVersionField() {
        FieldMetadata ret = getDeclaredField("serialVersionUID");
        
        // TODO: Create this when Roo supports field initialisers as primitives
        if( ret == null ) {
            // List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
            // ret = new DefaultFieldMetadata(getId(), Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE, new JavaSymbolName("serialVersionUID"), JavaType.LONG_PRIMITIVE, null, annotations);
        }
        
        return ret;
    }
    
    /**
     * Helper to get the field if declared by another metadata item or the physical
     * class directly.
     * 
     * @param fieldName The name of the field to get
     * @return The field metadata or null
     */
    private FieldMetadata getDeclaredField(String fieldName) {
        List<? extends FieldMetadata> fields = governorTypeDetails.getDeclaredFields();
        
        FieldMetadata ret = null;
        if( fields != null ) {
            Iterator<? extends FieldMetadata> it = fields.iterator();
            while( it.hasNext() && ret == null ) {
                FieldMetadata fm = it.next();
                if( fm.getFieldName().getSymbolName().equals(fieldName) ) ret = fm;
            }
        }
        
        return ret;
    }
    
    private MethodMetadata getIdGetter() {
        JavaSymbolName name = new JavaSymbolName("getId");
        JavaType returnType = new JavaType(Object.class.getName());
        List<JavaType> params = new ArrayList<JavaType>();
        List<JavaSymbolName> paramNames = new ArrayList<JavaSymbolName>();
        List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
        List<JavaType> throwTypes = new ArrayList<JavaType>();
        
        MethodMetadata mm = getDeclaredMethod(name, null, AnnotatedJavaType.convertFromJavaTypes(params));
        if( mm == null ) {
            InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
            bodyBuilder.appendFormalLine("return this.id;");
            
            mm = new DefaultMethodMetadata(getId(), Modifier.PUBLIC, name, returnType, AnnotatedJavaType.convertFromJavaTypes(params), paramNames, annotations, throwTypes, bodyBuilder.getOutput());
        }
        
        return mm;
    }
    
    private MethodMetadata getIdSetter() {
        JavaSymbolName name = new JavaSymbolName("setId");
        JavaType returnType = JavaType.VOID_PRIMITIVE;
        List<JavaType> params = new ArrayList<JavaType>();
        List<JavaSymbolName> paramNames = new ArrayList<JavaSymbolName>();
        List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
        List<JavaType> throwTypes = new ArrayList<JavaType>();
        
        params.add(new JavaType(Object.class.getName()));
        paramNames.add(new JavaSymbolName("val"));
        
        MethodMetadata mm = getDeclaredMethod(name, returnType, AnnotatedJavaType.convertFromJavaTypes(params));
        if( mm == null ) {
            InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
            bodyBuilder.appendFormalLine("if( this.id != null ) throw new IllegalArgumentException(\"You cannot reset a beans ID\");");
            bodyBuilder.appendFormalLine("this.id = id != null ? id.toString() : null;");
            
            mm = new DefaultMethodMetadata(getId(), Modifier.PUBLIC, name, returnType, AnnotatedJavaType.convertFromJavaTypes(params), paramNames, annotations, throwTypes, bodyBuilder.getOutput());
        }
        
        return mm;
    }
    
    private MethodMetadata getParentIdGetter() {
        JavaSymbolName name = new JavaSymbolName("getParentId");
        JavaType returnType = new JavaType(Object.class.getName());
        List<JavaType> params = new ArrayList<JavaType>();
        List<JavaSymbolName> paramNames = new ArrayList<JavaSymbolName>();
        List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
        List<JavaType> throwTypes = new ArrayList<JavaType>();
        
        MethodMetadata mm = getDeclaredMethod(name, null, AnnotatedJavaType.convertFromJavaTypes(params));
        if( mm == null ) {
            InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
            bodyBuilder.appendFormalLine("return this.parentId;");
            
            mm = new DefaultMethodMetadata(getId(), Modifier.PUBLIC, name, returnType, AnnotatedJavaType.convertFromJavaTypes(params), paramNames, annotations, throwTypes, bodyBuilder.getOutput());
        }
        
        return mm;
    }
    
    private MethodMetadata getParentIdSetter() {
        JavaSymbolName name = new JavaSymbolName("setParentId");
        JavaType returnType = JavaType.VOID_PRIMITIVE;
        List<JavaType> params = new ArrayList<JavaType>();
        List<JavaSymbolName> paramNames = new ArrayList<JavaSymbolName>();
        List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
        List<JavaType> throwTypes = new ArrayList<JavaType>();
        
        params.add(new JavaType(Object.class.getName()));
        paramNames.add(new JavaSymbolName("val"));
        
        MethodMetadata mm = getDeclaredMethod(name, returnType, AnnotatedJavaType.convertFromJavaTypes(params));
        if( mm == null ) {
            InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
            bodyBuilder.appendFormalLine("this.parentId = parentId != null ? parentId.toString() : null;");
            
            mm = new DefaultMethodMetadata(getId(), Modifier.PUBLIC, name, returnType, AnnotatedJavaType.convertFromJavaTypes(params), paramNames, annotations, throwTypes, bodyBuilder.getOutput());
        }
        
        return mm;
    }
    
    private MethodMetadata getTypeMethod() {
        JavaSymbolName name = new JavaSymbolName("getType");
        JavaType returnType = new JavaType(String.class.getName());
        List<JavaType> params = new ArrayList<JavaType>();
        List<JavaSymbolName> paramNames = new ArrayList<JavaSymbolName>();
        List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
        List<JavaType> throwTypes = new ArrayList<JavaType>();
        
        MethodMetadata mm = getDeclaredMethod(name, null, AnnotatedJavaType.convertFromJavaTypes(params));
        if( mm == null ) {
            // TODO: Attribute on ObjexStateBean annotation
            String typeName = governorTypeDetails.getName().getSimpleTypeName();
            if( typeName.endsWith("Bean") ) typeName = typeName.substring(0, typeName.length() - 4);
            
            InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
            bodyBuilder.appendFormalLine("return \"" + typeName + "\";");
            
            mm = new DefaultMethodMetadata(getId(), Modifier.PUBLIC, name, returnType, AnnotatedJavaType.convertFromJavaTypes(params), paramNames, annotations, throwTypes, bodyBuilder.getOutput());
        }
        
        return mm;
    }
    
    /**
     * Helper to get the field if declared by another metadata item or the physical
     * class directly.
     * 
     * @param fieldName The name of the field to get
     * @return The field metadata or null
     */
    private MethodMetadata getDeclaredMethod(JavaSymbolName methodName, JavaType returnType, List<AnnotatedJavaType> expectedParams) {
        List<? extends MethodMetadata> methods = governorTypeDetails.getDeclaredMethods();
        
        MethodMetadata ret = null;
        if( methods != null ) {
            Iterator<? extends MethodMetadata> it = methods.iterator();
            while( it.hasNext() && ret == null ) {
                MethodMetadata mm = it.next();
                
                if( !mm.getMethodName().equals(methodName) ) continue;
                if( returnType != null && !mm.getReturnType().equals(returnType) ) continue;
                List<AnnotatedJavaType> params = mm.getParameterTypes();
                if( params.size() != expectedParams.size() ) continue;
                else {
                    boolean match = true;
                    for( int i = 0 ; i < params.size() ; i++ ) {
                        AnnotatedJavaType p = params.get(i);
                        AnnotatedJavaType ep = expectedParams.get(i);
                        if( !p.equals(ep) ) {
                            match = false;
                            break;
                        }
                    }
                    
                    if( match ) ret = mm;
                }
            }
        }
        
        return ret;
    }
    
    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, Path path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }
    
    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final Path getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
}
