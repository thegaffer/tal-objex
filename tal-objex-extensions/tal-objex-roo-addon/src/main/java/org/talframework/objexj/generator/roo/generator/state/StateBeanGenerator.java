package org.talframework.objexj.generator.roo.generator.state;

import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;

/**
 * Writes out the other JavaBean methods such as toString,
 * hashcode and equals.
 *
 * @author Tom Spencer
 */
public class StateBeanGenerator extends BaseGenerator {
    
    public StateBeanGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        MethodMetadataWrapper toStringMethod = startToString();
        MethodMetadataWrapper hashCodeMethod = startHashCode();
        MethodMetadataWrapper equalsMethod = startEquals();
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField prop = it.next();
            
            if( prop.isNaturalBeanField() ) {
                String name = prop.getName().getSymbolName();
                
                if( !name.equals("id") && !name.equals("parentId") ) {
                    toStringMethod.addBody("builder.append(\"" + name + "=\").append(this." + name + ");");
                    if( prop.getBeanType().isPrimitive() ) {
                        hashCodeMethod.addBody("long " + name + "_temp = Double.doubleToRawLongBits(this." + name + ");");
                        hashCodeMethod.addBody("result = prime * result + (int)(" + name + "_temp ^ (" + name + "_temp >>> 32));");
                    }
                    else hashCodeMethod.addBody("result = prime * result + ((this." + name + " == null) ? 0 : this." + name + ".hashCode());");
                    equalsMethod.addBody("\tsame = BeanComparison.equals(same, this." + name + ", other." + name + ");");
                }
            }
        }
        
        toStringMethod.addBody("return builder.append(\" }\").toString();");
        hashCodeMethod.addBody("return result;");
        equalsMethod.addBody("}");
        equalsMethod.addBody("return same;");
        
        toStringMethod.addMetadata(builder, typeDetails, typeId);
        hashCodeMethod.addMetadata(builder, typeDetails, typeId);
        equalsMethod.addMetadata(builder, typeDetails, typeId);
    }
    
    private MethodMetadataWrapper startToString() {
        MethodMetadataWrapper ret = new MethodMetadataWrapper(new JavaSymbolName("toString"), JavaType.STRING_OBJECT);
        ret.addBody("StringBuilder builder = new StringBuilder();");
        ret.addBody("builder.append(\"" + typeDetails.getName().getSimpleTypeName() + ": { \");");
        ret.addBody("builder.append(\"id=\").append(getId());");
        ret.addBody("builder.append(\"parentId=\").append(getParentId());");
        
        return ret;
    }
    
    private MethodMetadataWrapper startEquals() {
        builder.getImportRegistrationResolver().addImport(new JavaType("org.talframework.util.beans.BeanComparison"));
        
        MethodMetadataWrapper ret = new MethodMetadataWrapper(new JavaSymbolName("equals"), JavaType.BOOLEAN_PRIMITIVE);
        ret.addParameter("obj", "Object", null);
        ret.addBody(typeDetails.getName().getSimpleTypeName() + " other = BeanComparison.basic(this, obj);");
        ret.addBody("boolean same = other != null;");
        ret.addBody("if( same ) {");
        ret.addBody("\tsame = BeanComparison.equals(same, getId(), other.getId());");
        ret.addBody("\tsame = BeanComparison.equals(same, getParentId(), other.getParentId());");
        
        return ret;
    }
    
    private MethodMetadataWrapper startHashCode() {
        MethodMetadataWrapper ret = new MethodMetadataWrapper(new JavaSymbolName("hashCode"), JavaType.INT_PRIMITIVE);
        ret.addBody("final int prime = 31;");
        ret.addBody("int result = 1;");
        ret.addBody("result = prime * result + ((getId() == null) ? 0 : getId().hashCode());");
        ret.addBody("result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());");
        
        return ret;
    }
}
