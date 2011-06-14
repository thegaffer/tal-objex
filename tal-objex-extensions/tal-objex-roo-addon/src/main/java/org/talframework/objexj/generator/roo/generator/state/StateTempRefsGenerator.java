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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
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
import org.talframework.objexj.generator.roo.utils.TypeDetailsUtil;

/**
 * Generates out all of the boiler plate state bean methods
 * and adds in the ID/ParentID fields.
 *
 * @author Tom Spencer
 */
public class StateTempRefsGenerator extends BaseGenerator implements FieldVisitor {
    
    /** The method we are populating in the generator */
    private MethodMetadataWrapper method = null;
    
    public StateTempRefsGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    public void generate(List<ObjexField> fields) {
        MethodMetadata mm = TypeDetailsUtil.getMethod(typeDetails, "updateTemporaryReferences", null);
        if( mm != null ) return;
        
        List<JavaType> enclosingTypeParams = new ArrayList<JavaType>();
        enclosingTypeParams.add(new JavaType(TypeConstants.OBJEXID));
        enclosingTypeParams.add(new JavaType(TypeConstants.OBJEXID));
        JavaType refMap = new JavaType("java.util.Map", 0, null, null, enclosingTypeParams); // List<MyInterface>
        
        method = new MethodMetadataWrapper(new JavaSymbolName("updateTemporaryReferences"), JavaType.VOID_PRIMITIVE);
        method.addParameter(new JavaSymbolName("refs"), refMap, null);
        
        // Update all reference properties
        builder.getImportRegistrationResolver().addImport(new JavaType("org.talframework.objexj.object.utils.StateBeanUtils"));
        
        method.addBody("parentId = StateBeanUtils.updateTempReferences(parentId, refs);");
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            it.next().accept(this);
        }
        
        method.addMetadata(builder, typeDetails, typeId);
    }
    
    public void visitSimple(SimpleField prop) {
        // Nothing, ignore
    }
    
    public void visitReference(SimpleReferenceField prop) {
        method.addBody(prop.getName() + " = StateBeanUtils.updateTempReferences(" + prop.getName() + ", refs);");
    }
    
    public void visitList(ListReferenceField prop) {
        method.addBody(prop.getName() + " = StateBeanUtils.updateTempReferences(" + prop.getName() + ", refs);");
    }
    
    public void visitMap(MapReferenceField prop) {
        method.addBody(prop.getName() + " = StateBeanUtils.updateTempReferences(" + prop.getName() + ", refs);");
    }
}
