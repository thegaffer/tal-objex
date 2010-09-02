package org.talframework.objexj.generator.roo;

import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.generator.roo.fields.CollectionPropHelper;
import org.talframework.objexj.generator.roo.fields.ObjexObjProperty;
import org.talframework.objexj.generator.roo.fields.PropHelper;
import org.talframework.objexj.generator.roo.fields.ReferencePropHelper;
import org.talframework.objexj.generator.roo.fields.SimplePropHelper;
import org.talframework.objexj.generator.roo.utils.ConstructorMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * Metadata Item for an Objex Object. This object in the code contains
 * the real business behaviour.
 * 
 * TODO: Set methods on owned members that copy fields into existing (creating first as necc)
 * 
 * @author Tom Spencer
 */
public class ObjexObjMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
    
    private static final String PROVIDES_TYPE_STRING = ObjexObjMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private final ObjexObjAnnotationValues annotationValues;
    
    public ObjexObjMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, ObjexObjAnnotationValues values, List<ObjexObjProperty> properties) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        this.annotationValues = values;
        
        boolean baseObj = TypeDetailsUtil.isExtending(governorTypeDetails, "org.talframework.objexj.object.BaseObjexObj");
        boolean objexObj = TypeDetailsUtil.isExtending(governorTypeDetails, "org.talframework.objexj.object.SimpleObjexObj"); 
        
        // FUTURE: Should we consider full re-implementing of ObjexObj in aspect?
        if( !baseObj && !objexObj ) {
            
            // Add base objex obj if we've defined a bean
            if( TypeDetailsUtil.getField(governorTypeDetails, "bean") != null ) {
                builder.addExtendsType(new JavaType("org.talframework.objexj.object.BaseObjexObj"));
                baseObj = true;
            }
            
            // Otherwise add simple objex obj
            else {
                builder.addExtendsType(new JavaType("org.talframework.objexj.object.SimpleObjexObj"));
                objexObj = true;
            }
        }
        
        if( objexObj ) {
            addStrategy();
            addDefaultCons();
        }
        else if( baseObj ) {
            addStateAccessors();
            addValidate();
        }
        
        // Add in the methods for the properties
        Iterator<ObjexObjProperty> it = properties.iterator();
        while( it.hasNext() ) {
            ObjexObjProperty prop = it.next();
            
            PropHelper helper = null;
            if( prop.isMapReferenceProp() ) helper = new CollectionPropHelper(prop);
            else if( prop.isListReferenceProp() ) helper = new CollectionPropHelper(prop);
            else if( prop.isReferenceProp() ) helper = new ReferencePropHelper(prop);
            else helper = new SimplePropHelper(prop);
            
            if( helper != null ) helper.build(builder, governorTypeDetails, getId());
        }
        
        itdTypeDetails = builder.build();
    }
    
    /**
     * @return The metadata item for the ID field
     */
    private void addStateAccessors() {
        MethodMetadataWrapper localGetter = new MethodMetadataWrapper(new JavaSymbolName("getLocalState"), annotationValues.getValue());
        localGetter.addBody("return bean;");
        localGetter.addMetadata(builder, governorTypeDetails, getId());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper("getStateObject", ObjexObjStateBean.class.getName());
        getter.addBody("if( isUpdateable() ) return bean;");
        getter.addBody("else return bean.cloneState();");
        getter.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addValidate() {
        MethodMetadataWrapper validate = new MethodMetadataWrapper(new JavaSymbolName("validate"), JavaType.VOID_PRIMITIVE);
        validate.addParameter("request", "org.talframework.objexj.ValidationRequest", null);
        // TODO: Default validation
        validate.addBody("return;");
        validate.addMetadata(builder, governorTypeDetails, getId());
    }
    
    private void addStrategy() {
        
        // Get name
        
        // Get state bean
        
        // Static create
        
        // Static clone
        
        
        
        // TODO: Can't do this because Roo does not yet support non-default constructors on initialisers
        return;
        
        /*
        PropertyMetadataWrapper wrapper = new PropertyMetadataWrapper("STRATEGY", "org.talframework.objexj.object.ObjectStrategy");
        wrapper.addModifier(Modifier.STATIC);
        wrapper.addModifier(Modifier.FINAL);
        ...
        */
    }
    
    private void addDefaultCons() {
        ConstructorMetadataWrapper cons = new ConstructorMetadataWrapper();
        cons.addParameter("bean", "org.talframework.objexj.ObjexObjStateBean", null);
        cons.addBody("super(STRATEGY, bean);");
        
        cons.addMetadata(builder, governorTypeDetails, getId());
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
