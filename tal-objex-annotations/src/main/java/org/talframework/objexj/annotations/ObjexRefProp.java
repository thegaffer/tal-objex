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
 * 
 * 
 * @author Tom Spencer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexRefProp {
    /** Holds the optional name of the external property */
    String externalName() default "";
    /** Indicates if the property is considered owned */
    boolean owned() default true;
    /** The type to expose the references as */
    Class<?> type();
    /** The type of object to create against this property if fixed */
    String newType() default "";
    /** Determines if the field is gettable externally */
    boolean gettable() default true;
    /** Determines if the field is settable externally */
    boolean settable() default true;
}
