package org.talframework.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation, when applied to a class, marks it as an object that
 * has it's own identity independent of state. An identity object, unlike
 * a value object, is mutable though its identity once assigned does not
 * change. An identity object is typically richer in behaviour that a
 * value object.
 * 
 * <p>In Objex this annotation marks the object as a possible ObjexObj
 * object. You are encouraged to use Identity objects when using Objex
 * because relationships can be formed between two identity objects,
 * whereas a relationship cannot be directly formed to a value object
 * (except of course to own a value object).</p>
 *
 * @author Tom Spencer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityObject {

}
