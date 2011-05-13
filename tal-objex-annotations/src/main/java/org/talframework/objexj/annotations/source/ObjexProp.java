/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.annotations.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a property that should be exposed on
 * the bean and any natural interface linked to this type. Note
 * that all properties of a class marked with {@link ObjexObj}
 * are considered object properties, but this allows finer
 * control. 
 * 
 * @author Tom Spencer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexProp {
    /** Determines if the field is gettable externally */
    boolean gettable() default true;
    /** Determines if the field is settable externally */
    boolean settable() default true;
    /** Holds the optional name of the external property, i.e. the getter/setter */
    String externalName() default "";
    /** Holds the type to expose the property as (if Object then the type of the property is used) */
    Class<?> exposeAs() default Object.class;
    /** Optional class with static methods that we can perform the transformations with */
    Class<?> transformerClass() default Object.class;
    /** Name of a function on transformer class or non-static inside this object that performs get transformation */
    String transformerGetFunction() default "";
    /** Name of a function on transformer class or non-static inside this object that performs set transformation */
    String transformerSetFunction() default "";
}
