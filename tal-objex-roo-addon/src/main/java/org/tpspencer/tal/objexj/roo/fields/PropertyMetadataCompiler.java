package org.tpspencer.tal.objexj.roo.fields;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.tpspencer.tal.objexj.annotations.ObjexRefProp;
import org.tpspencer.tal.objexj.roo.ObjexRefPropAnnotationValues;
import org.tpspencer.tal.objexj.roo.utils.MethodMetadataWrapper;
import org.tpspencer.tal.objexj.roo.utils.PropertyMetadataWrapper;
import org.tpspencer.tal.objexj.roo.utils.TypeDetailsUtil;

/**
 * This class scans the fields of the type and creates
 * {@link ObjexObjPropertyMetadata} instances for each
 * property.
 * 
 * @author Tom Spencer
 */
public class PropertyMetadataCompiler {
    
    /** Holds the type details */
    private ClassOrInterfaceTypeDetails typeDetails;
    /** Holds all the properties found */
    private List<PropertyMetadataWrapper> props;
    /** Holds all the methods against the properties for state bean */
    private List<MethodMetadataWrapper> propertyMethods;
    
    public PropertyMetadataCompiler(ClassOrInterfaceTypeDetails typeDetails) {
        this.typeDetails = typeDetails;
    }
    
    public PropertyMetadataCompiler(ClassOrInterfaceTypeDetails typeDetails, List<PropertyMetadataWrapper> props) {
        this.typeDetails = typeDetails;
        this.props = props;
    }
    
    public void compileStateBean() {
        props = new ArrayList<PropertyMetadataWrapper>();
        propertyMethods = new ArrayList<MethodMetadataWrapper>();
        
        List<? extends FieldMetadata> fields = typeDetails.getDeclaredFields();
        if( fields != null ) {
            Iterator<? extends FieldMetadata> it = fields.iterator();
            while( it.hasNext() ) {
                FieldMetadata fm = it.next();
                if( Modifier.isStatic(fm.getModifier()) ) continue;
                
                PropertyMetadataWrapper prop = addField(fm);
                addGetter(prop);
                addSetter(prop);
            }
        }
    }
    
    public void compileObjexObj() {
        if( props == null ) props = new ArrayList<PropertyMetadataWrapper>();
        propertyMethods = new ArrayList<MethodMetadataWrapper>();
        
        Iterator<PropertyMetadataWrapper> it = props.iterator();
        while( it.hasNext() ) {
            PropertyMetadataWrapper prop = it.next();
            
            ObjexRefPropAnnotationValues refProp = prop.getRefProp();
            
            if( refProp != null && !prop.getType().isCommonCollectionType() &&
                    !prop.getType().getFullyQualifiedTypeName().equals(String.class.getName()) ) {
                refProp = null; // Treat as simple
            }
            
            if( refProp != null ) {
                JavaType type = prop.getType();
                
                if( type.equals(JavaType.STRING_OBJECT) ) {
                    addObjexRefProp(prop, refProp);
                }
                else if( type.isArray() ) {
                    addObjexArrayProp(prop, refProp);
                }
                else if( type.getFullyQualifiedTypeName().equals(List.class.getName()) ) {
                    addObjexListProp(prop, refProp);
                }
                else {
                    // TODO: ???
                }
            }
            else {
                addObjexGetter(prop);
                addObjexSetter(prop);
            }
        }
    }
    
    public List<PropertyMetadataWrapper> getProperties() {
        return props;
    }
    
    public List<MethodMetadataWrapper> getPropertyMethods() {
        return propertyMethods;
    }
    
    public PropertyMetadataWrapper addField(FieldMetadata fm) {
        PropertyMetadataWrapper prop = new PropertyMetadataWrapper(fm);
        props.add(prop);
        return prop;
    }
    
    public MethodMetadataWrapper addGetter(PropertyMetadataWrapper prop) {
        // TODO: Boolean is!
        JavaSymbolName name = new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getType());
        getter.addBody("return this." + prop.getName().getSymbolName() + ";");
        propertyMethods.add(getter);
        return getter;
    }
    
    public MethodMetadataWrapper addSetter(PropertyMetadataWrapper prop) {
        JavaSymbolName name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("this." + prop.getName().getSymbolName() + " = val;");
        propertyMethods.add(setter);
        return setter;
    }
    
    public MethodMetadataWrapper addObjexGetter(PropertyMetadataWrapper prop) {
        // TODO: Boolean is!
        JavaSymbolName name = new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper getter = new MethodMetadataWrapper(name, prop.getType());
        getter.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        propertyMethods.add(getter);
        return getter;
    }
    
    public MethodMetadataWrapper addObjexSetter(PropertyMetadataWrapper prop) {
        JavaSymbolName name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        
        MethodMetadataWrapper setter = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
        setter.addParameter(new JavaSymbolName("val"), prop.getType(), null);
        setter.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
        setter.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
        propertyMethods.add(setter);
        return setter;
    }
    
    /**
     * Adds in the methods for a reference property
     */
    public void addObjexRefProp(PropertyMetadataWrapper prop, ObjexRefPropAnnotationValues refProp) {
        // a. Standard getter
        JavaSymbolName name = new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter());
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, refProp.getType());
        method.addBody("return getContainer().getObject(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "());");
        propertyMethods.add(method);
        
        // b. Reference getter
        name = new JavaSymbolName("get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "Ref");
        method = new MethodMetadataWrapper(name, prop.getType());
        method.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        propertyMethods.add(method);
        
        if( refProp.isOwned() ) {
            // c. Create (if owned)
            String newType = refProp.getNewType();
            
            name = new JavaSymbolName("create" + prop.getName().getSymbolNameCapitalisedFirstLetter());
            if( newType == null ) method.addParameter("type", String.class.getName(), null);
            method = new MethodMetadataWrapper(name, refProp.getType());
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            if( newType == null ) method.addBody("// " + refProp.getType().getFullyQualifiedTypeName() + "val = createObjexHelper(type);");
            else method.addBody("// " + refProp.getType().getFullyQualifiedTypeName() + "val = createObjexHelper(\"" + refProp.getNewType() + "\");");
            method.addBody("String ref = getObjexId(val);");
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(ref);");
            propertyMethods.add(method);
        }
        else {
            // d. Standard setter
            name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter());
            method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), refProp.getType(), null);
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            method.addBody("String ref = getObjexId(val);");
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(ref);");
            propertyMethods.add(method);
            
            // e. Reference setter
            name = new JavaSymbolName("set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "Ref");
            method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), prop.getType(), null);
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
            propertyMethods.add(method);
        }
    }
    
    /**
     * Adds in the methods for a reference property
     */
    public void addObjexArrayProp(PropertyMetadataWrapper prop, ObjexRefPropAnnotationValues refProp) {
        String baseProp = prop.getName().getSymbolNameCapitalisedFirstLetter();
        String singleProp = baseProp;
        if( singleProp.endsWith("s") && singleProp.length() > 1 ) singleProp = singleProp.substring(0, singleProp.length() - 1);
        
        String standardGetter = "get" + baseProp;           // getItems
        String refGetter = "get" + singleProp + "Refs";     // getItemRefs
        String create = "create" + singleProp;              // createItem
        String standardSetter = "set" + baseProp;           // setItems
        String refSetter = "set" + singleProp + "Refs";     // setItemRefs
        
        // a. Standard getter
        JavaSymbolName name = new JavaSymbolName(standardGetter);
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, refProp.getType());
        method.addBody("return getContainer().getObject(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "());");
        propertyMethods.add(method);
        
        // b. Reference getter
        name = new JavaSymbolName(refGetter);
        method = new MethodMetadataWrapper(name, prop.getType());
        method.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        propertyMethods.add(method);
        
        if( refProp.isOwned() ) {
            // c. Create (if owned)
            String newType = refProp.getNewType();
            
            name = new JavaSymbolName(create);
            if( newType == null ) method.addParameter("type", String.class.getName(), null);
            method = new MethodMetadataWrapper(name, refProp.getType());
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            if( newType == null ) method.addBody("// " + refProp.getType().getFullyQualifiedTypeName() + "val = createObjexHelper(type);");
            else method.addBody("// " + refProp.getType().getFullyQualifiedTypeName() + "val = createObjexHelper(\"" + refProp.getNewType() + "\");");
            method.addBody("String ref = getObjexId(val);");
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(ref);");
            propertyMethods.add(method);
        }
        else {
            // d. Standard setter
            name = new JavaSymbolName(standardSetter);
            method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), refProp.getType(), null);
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            method.addBody("String ref = getObjexId(val);");
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(ref);");
            propertyMethods.add(method);
            
            // e. Reference setter
            name = new JavaSymbolName(standardGetter);
            method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), prop.getType(), null);
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
            propertyMethods.add(method);
        }
    }
    
    /**
     * Adds in the methods for a reference property
     */
    public void addObjexListProp(PropertyMetadataWrapper prop, ObjexRefPropAnnotationValues refProp) {
        String baseProp = prop.getName().getSymbolNameCapitalisedFirstLetter();
        String singleProp = baseProp;
        if( singleProp.endsWith("s") && singleProp.length() > 1 ) singleProp = singleProp.substring(0, singleProp.length() - 1);
        
        String standardGetter = "get" + baseProp;           // getItems
        String refGetter = "get" + singleProp + "Refs";     // getItemRefs
        String create = "create" + singleProp;              // createItem
        String standardSetter = "set" + baseProp;           // setItems
        String refSetter = "set" + singleProp + "Refs";     // setItemRefs
        
        // a. Standard getter
        JavaSymbolName name = new JavaSymbolName(standardGetter);
        MethodMetadataWrapper method = new MethodMetadataWrapper(name, refProp.getType());
        method.addBody("return getContainer().getObjectList(bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "());");
        propertyMethods.add(method);
        
        // b. Reference getter
        name = new JavaSymbolName(refGetter);
        method = new MethodMetadataWrapper(name, prop.getType());
        method.addBody("return bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "();");
        propertyMethods.add(method);
        
        if( refProp.isOwned() ) {
            // c. Create (if owned)
            String newType = refProp.getNewType();
            
            name = new JavaSymbolName(create);
            if( newType == null ) method.addParameter("type", String.class.getName(), null);
            method = new MethodMetadataWrapper(name, refProp.getType());
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            if( newType == null ) method.addBody("// " + refProp.getType().getFullyQualifiedTypeName() + "val = createObjexHelper(type);");
            else method.addBody("// " + refProp.getType().getFullyQualifiedTypeName() + "val = createObjexHelper(\"" + refProp.getNewType() + "\");");
            method.addBody("String ref = getObjexId(val);");
            method.addBody("if( bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "() == null ) bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(new ArrayList<String>());");
            method.addBody("bean.get" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "().add(ref);");
            propertyMethods.add(method);
        }
        else {
            // d. Standard setter
            /*name = new JavaSymbolName(standardSetter);
            method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), refProp.getType(), null);
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            method.addBody("String ref = getObjexId(val);");
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(ref);");
            propertyMethods.add(method);*/
            
            // e. Reference setter
            /*name = new JavaSymbolName(refSetter);
            method = new MethodMetadataWrapper(name, JavaType.VOID_PRIMITIVE);
            method.addParameter(new JavaSymbolName("val"), prop.getType(), null);
            method.addBody("checkUpdateable();"); // TODO: Or Annotation and Aspect!!??
            method.addBody("bean.set" + prop.getName().getSymbolNameCapitalisedFirstLetter() + "(val);");
            propertyMethods.add(method);*/
        }
    }
}
