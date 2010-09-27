package org.talframework.objexj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a method as being an enrichment
 * method that should be called during validation. The
 * call is made either before the intra-object validation
 * or after it (and before the inter-object validation).
 * The method this is applied to should have the
 * following signature:
 * 
 * <p><code>
 * public void xxx();
 * </code></p>
 *
 * @author Tom Spencer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ObjexEnrich {
    /** Determines if called before intra-object validation (default) or after it */
    public boolean postObjectCheck() default false;
}
