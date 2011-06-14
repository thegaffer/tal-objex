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
import org.talframework.objexj.generator.roo.fields.ObjexField;
import org.talframework.objexj.generator.roo.generator.BaseGenerator;
import org.talframework.objexj.generator.roo.utils.MethodMetadataWrapper;
import org.talframework.objexj.generator.roo.utils.TypeConstants;

/**
 * Writes out the state bean clone method given the
 * proeprties.
 *
 * @author Tom Spencer
 */
public class StateCloneGenerator extends BaseGenerator {
    
    public StateCloneGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        super(builder, typeDetails, typeId);
    }
    
    /**
     * Call to generate the accessor/mutator methods for the given
     * fields.
     * 
     * @param fields The fields
     */
    public void generate(List<ObjexField> fields) {
        MethodMetadataWrapper clone = new MethodMetadataWrapper("cloneState", TypeConstants.OBJEXSTATEBEAN);
        
        String beanName = typeDetails.getName().getSimpleTypeName(); 
        clone.addBody(beanName + " ret = new " + beanName + "();");
        
        boolean idFound = false;
        boolean parentFound = false;
        
        Iterator<ObjexField> it = fields.iterator();
        while( it.hasNext() ) {
            ObjexField prop = it.next();
            
            if( prop.isNaturalBeanField() ) {
                String name = prop.getName().getSymbolName();
                
                clone.addBody("ret." + name + " = this." + name + ";");
                
                if( name.equals("id") ) idFound = true;
                else if( name.equals("parentId") ) parentFound = true;
            }
        }
        
        if( !idFound ) clone.addBody("ret.id = this.id;");
        if( !parentFound ) clone.addBody("ret.parentId = this.parentId;");
        
        clone.addBody("return ret;");
        clone.addMetadata(builder, typeDetails, typeId);
    }
}
