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
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.EnumAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.EnumDetails;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;
import org.tpspencer.tal.objexj.roo.fields.ObjexObjProperty;
import org.tpspencer.tal.objexj.roo.fields.PropertyCompiler;
import org.tpspencer.tal.objexj.roo.utils.ConstructorMetadataWrapper;
import org.tpspencer.tal.objexj.roo.utils.MethodMetadataWrapper;
import org.tpspencer.tal.objexj.roo.utils.PropertyMetadataWrapper;
import org.tpspencer.tal.objexj.roo.utils.TypeDetailsUtil;

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
    
    private final ObjexObjStateBeanAnnotationValues annotationValues;
    private List<ObjexObjProperty> properties = null;
    
    public ObjexStateBeanMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, ObjexObjStateBeanAnnotationValues annotationValues) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        this.annotationValues = annotationValues;
        logger.debug(">>> Creating ObjexStateBean metadata for: " + governorTypeDetails.getName());
        
        // Annotation, UID and ObjexObj interface
        builder.addTypeAnnotation(getJDOAnnotation());
        if( !TypeDetailsUtil.isImplementing(governorTypeDetails, "org.tpspencer.tal.objexj.ObjexObjStateBean") ) 
            builder.addImplementsType(new JavaType("org.tpspencer.tal.objexj.ObjexObjStateBean"));
        builder.addField(getSerialVersionField());
        
        // Build the list of properties on this object
        PropertyCompiler compiler = new PropertyCompiler();
        this.properties = compiler.compile(governorTypeDetails);
        
        // Add extra bits for the state bean
        addIdField();
        addParentIdField();
        addTypeGetter();
        addInit();
        addTempRefs();
        
        // Add in constructors
        addDefaultCons();
        addCopyCons();
        addNewCons(true); // TODO: Determine if we should have parent
        
        // Create the ITD Type
        itdTypeDetails = builder.build();
    }
    
    /**
     * @return The properties against this type
     */
    public List<ObjexObjProperty> getProperties() {
        return properties;
    }
    
    /**
     * Finds the target annotation on the type or from another metadata item
     * or creates it if is does not exist.
     * 
     * @return The target annotation metadata
     */
    private AnnotationMetadata getJDOAnnotation() {
        AnnotationMetadata ret = TypeDetailsUtil.getAnnotation(governorTypeDetails, "javax.jdo.annotations.PersistenceCapable");
        
        if( ret == null ) {
            List<AnnotationAttributeValue<?>> attrs = new ArrayList<AnnotationAttributeValue<?>>();
            ret = new DefaultAnnotationMetadata(new JavaType("javax.jdo.annotations.PersistenceCapable"), attrs);
        }
        
        return ret;
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private FieldMetadata getSerialVersionField() {
        FieldMetadata ret = TypeDetailsUtil.getField(governorTypeDetails, "serialVersionUID");
        
        if( ret == null ) {
            List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
            ret = new DefaultFieldMetadata(getId(), Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE, new JavaSymbolName("serialVersionUID"), JavaType.LONG_PRIMITIVE, null, annotations);
        }
        
        return ret;
    }
    
    private void addDefaultCons() {
        ConstructorMetadataWrapper con = new ConstructorMetadataWrapper();
        
        con.addBody("super();");
        con.addBody("// Nothing");
        
        con.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addNewCons(boolean includeParent) {
        ConstructorMetadataWrapper newCon = new ConstructorMetadataWrapper();
        newCon.addParameter("parentId", ObjexID.class.getName(), null);
        
        newCon.addBody("super();");
        if( includeParent ) newCon.addBody("this.parentId = parentId != null ? parentId.toString() : null;");
        
        newCon.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addCopyCons() {
        ConstructorMetadataWrapper copyCon = new ConstructorMetadataWrapper();
        copyCon.addParameter(new JavaSymbolName("src"), governorTypeDetails.getName(), null);
        
        copyCon.addBody("super();");
        
        boolean idFound = false;
        boolean parentFound = false;
        
        Iterator<ObjexObjProperty> it = properties.iterator();
        while( it.hasNext() ) {
            ObjexObjProperty prop = it.next();
            String name = prop.getName().getSymbolName();
            
            copyCon.addBody("this." + name + " = src." + name + ";");
            
            if( name.equals("id") ) idFound = true;
            else if( name.equals("parentId") ) parentFound = true;
        }
        
        if( !idFound ) copyCon.addBody("this.id = src.id;");
        if( !parentFound ) copyCon.addBody("this.parentId = src.parentId;");
        
        copyCon.addMetadata(builder, governorTypeDetails, getId());
    }
    
    /**
     * Adds a getter/setter to the property
     * 
     * @param prop
     */
    public void addGetterSetter(ObjexObjProperty prop) {
        JavaSymbolName name = new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getType());
        getter.addBody("return this." + prop.getName().getSymbolName() + ";");
        getter.addMetadata(builder, governorTypeDetails, getId());
        
        name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("this." + prop.getName().getSymbolName() + " = val;");
        setter.addMetadata(builder, governorTypeDetails, getId());
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
        prop.addMetadata(builder, governorTypeDetails, getId());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName("getId"), prop.getType());
        getter.addBody("return this.id;");
        getter.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addParentIdField() {
        PropertyMetadataWrapper prop = new PropertyMetadataWrapper(new JavaSymbolName("parentId"), JavaType.STRING_OBJECT);
        prop.addMetadata(builder, governorTypeDetails, getId());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(new JavaSymbolName("getParentId"), prop.getType());
        getter.addBody("return this.parentId;");
        getter.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addTypeGetter() {
        MethodMetadata mm = TypeDetailsUtil.getMethod(governorTypeDetails, "getObjexObjType", null);
        if( mm != null ) return;
        
        String type = annotationValues.getName();
        
        // Backup, although I don't possible for this to be true
        if( type == null ) {
            type = governorTypeDetails.getName().getSimpleTypeName();
            if( type.endsWith("Bean") && type.length() > 4 ) type = type.substring(0, type.length() - 4);
            if( type.endsWith("State") && type.length() > 5 ) type = type.substring(0, type.length() - 5);
        }
        
        MethodMetadataWrapper typeMethod = new MethodMetadataWrapper(new JavaSymbolName("getObjexObjType"), JavaType.STRING_OBJECT);
        typeMethod.addBody("return \"" + type + "\";");
        typeMethod.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addInit() {
        MethodMetadata mm = TypeDetailsUtil.getMethod(governorTypeDetails, "init", null);
        if( mm != null ) return;
        
        MethodMetadataWrapper typeMethod = new MethodMetadataWrapper(new JavaSymbolName("init"), JavaType.VOID_PRIMITIVE);
        typeMethod.addParameter("id", "java.lang.Object", null);
        typeMethod.addBody("this.id = id != null ? id.toString() : null;");
        typeMethod.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addTempRefs() {
        MethodMetadata mm = TypeDetailsUtil.getMethod(governorTypeDetails, "updateTemporaryReferences", null);
        if( mm != null ) return;
        
        List<JavaType> enclosingTypeParams = new ArrayList<JavaType>();
        enclosingTypeParams.add(new JavaType(ObjexID.class.getName()));
        enclosingTypeParams.add(new JavaType(ObjexID.class.getName()));
        JavaType refMap = new JavaType("java.util.Map", 0, null, null, enclosingTypeParams); // List<MyInterface>
        
        MethodMetadataWrapper typeMethod = new MethodMetadataWrapper(new JavaSymbolName("updateTemporaryReferences"), JavaType.VOID_PRIMITIVE);
        typeMethod.addParameter(new JavaSymbolName("refs"), refMap, null);
        
        // Update all reference properties
        builder.getImportRegistrationResolver().addImport(new JavaType("org.tpspencer.tal.objexj.object.ObjectUtils"));
        typeMethod.addBody("parentId = ObjectUtils.updateTempReferences(parentId, refs);");
        Iterator<ObjexObjProperty> it = this.properties.iterator();
        while( it.hasNext() ) {
            ObjexObjProperty prop = it.next();
            if( prop.isReferenceProp() || prop.isListReferenceProp() || prop.isMapReferenceProp() ) {
                typeMethod.addBody(prop.getName() + " = ObjectUtils.updateTempReferences(" + prop.getName() + ", refs);");
            }
        }
        
        typeMethod.addMetadata(builder, governorTypeDetails, getId());
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