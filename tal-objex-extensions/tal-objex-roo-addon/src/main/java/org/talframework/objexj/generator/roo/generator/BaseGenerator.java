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
package org.talframework.objexj.generator.roo.generator;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultItdTypeDetailsBuilder;

/**
 * This class is the base class for any generator and
 * holds details about the type we are generating for.
 *
 * @author Tom Spencer
 */
public abstract class BaseGenerator {

    protected final DefaultItdTypeDetailsBuilder builder;
    protected final ClassOrInterfaceTypeDetails typeDetails;
    protected final String typeId;
    
    /**
     * Constructs a base generator holding the type details
     * given.
     * 
     * @param builder The builder
     * @param typeDetails The type details
     * @param typeId The type ID
     */
    public BaseGenerator(DefaultItdTypeDetailsBuilder builder, ClassOrInterfaceTypeDetails typeDetails, String typeId) {
        this.builder = builder;
        this.typeDetails = typeDetails;
        this.typeId = typeId;
    }
}
