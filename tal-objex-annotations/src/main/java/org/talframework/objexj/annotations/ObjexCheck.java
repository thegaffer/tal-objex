package org.talframework.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a method as being an a business
 * level validation routine that should be called as part
 * of either intra, inter or child level validation. 
 * 
 * <p><code>
 * public boolean xxx();
 * </code></p>
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexCheck {
    /** Holds the message to display on failure */
    public String message();
    /** Determines if validates children, if false inspects postObjectCheck field */
    public boolean childObjectCheck() default false;
    /** Determines if part of intra (default) or inter-object validation */
    public boolean postObjectCheck() default false;
}