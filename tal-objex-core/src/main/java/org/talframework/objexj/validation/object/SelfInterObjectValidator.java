package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.InterObjectGroup;

/**
 * Implementing this interface on an Objex object means it
 * will be called as part of the inter-object validation
 * step. Typically you do not use or implement this interface
 * directly, but use the Objex generators to generate out
 * the relevant code connecting to your @ObjexCheck methods.
 *
 * @author Tom Spencer
 */
@InterObjectValid(groups={InterObjectGroup.class})
public interface SelfInterObjectValidator {

    /**
     * Called on the object exposing this interface to actually
     * validate itself with respect to its references, siblings
     * and parent.
     * 
     * @param context The validation context so errors can be raised
     * @return True if the object is valid, false otherwise
     */
    public boolean validateObjectAgainstOthers(ConstraintValidatorContext context);
}
