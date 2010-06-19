package org.tpspencer.tal.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates the class is an Objex behaviour
 * bean and includes as its value the state bean. The Objex
 * generators then ensures there are appropriate accessor
 * methods around all the members.
 * 
 * @author Tom Spencer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexObj {
    Class<?> value();
}
