/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
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
    
    private MethodMetadataWrapper writerMethod = null;
    private MethodMetadataWrapper readerMethod = null;
    
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
        readerMethod = new MethodMetadataWrapper(new JavaSymbolName("acceptReader"), JavaType.VOID_PRIMITIVE);
        readerMethod.addParameter("reader", TypeConstants.OBJEXSTATEREADER, null);
        
        writerMethod = new MethodMetadataWrapper(new JavaSymbolName("acceptWriter"), JavaType.VOID_PRIMITIVE);
        writerMethod.addParameter("writer", TypeConstants.OBJEXSTATEWRITER, null);
        writerMethod.addParameter(new JavaSymbolName("includeNonPersistent"), JavaType.BOOLEAN_PRIMITIVE, null);
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField field = it.next();
            if( field.isNaturalBeanField() ) {
                field.accept(this);
            }
        }
        
        builder.getImportRegistrationResolver().addImport(new JavaType("org.talframework.objexj.ObjexObjStateBean.ObjexFieldType"));
        
        readerMethod.addMetadata(builder, typeDetails, typeId);
        writerMethod.addMetadata(builder, typeDetails, typeId);
    }

    public void visitSimple(SimpleField prop) {
        writerMethod.addBody("writer.write(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody(prop.getBeanName() + " = reader.read(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getBeanTypeName() + ".class, " + prop.getObjexType() + ", true);");
    }
    
    public void visitReference(SimpleReferenceField prop) {
        writerMethod.addBody("writer.writeReference(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody(prop.getBeanName() + " = reader.readReference(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
    }
    
    public void visitList(ListReferenceField prop) {
        writerMethod.addBody("writer.writeReferenceList(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody(prop.getBeanName() + " = reader.readReferenceList(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
    }
    
    public void visitMap(MapReferenceField prop) {
        writerMethod.addBody("writer.writeReferenceMap(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
        readerMethod.addBody(prop.getBeanName() + " = reader.readReferenceMap(\"" + prop.getBeanName() + "\", " + prop.getBeanName() + ", " + prop.getObjexType() + ", true);");
    }
}
