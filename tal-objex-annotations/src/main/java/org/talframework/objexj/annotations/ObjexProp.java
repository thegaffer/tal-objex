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

package org.talframework.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation represents an ObjexProperty. All
 * fields of the state bean are treated as Objex
 * properties, but using this allows finer control
 * over the publically exposed property including
 * changing it's name and type.
 * 
 * @author Tom Spencer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexProp {
    /** Holds the optional name of the external property */
    String externalName() default "";
    /** Holds the type to expose the property as (if Object then ignored) */
    Class<?> exposeAs() default Object.class;
    /** Optional class to perform transformation on, can be the same as the exposeAs if it natively support it */
    Class<?> transformerClass() default Object.class;
    /** Name of a function on static class or inside the object that performs get transformation */
    String transformerGetFunction() default "";
    /** Name of a function on transformer or target object that performs set transformation */
    String transformerSetFunction() default "";
    /** Determines if the field is gettable externally */
    boolean gettable() default true;
    /** Determines if the field is settable externally */
    boolean settable() default true;
}
