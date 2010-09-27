package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * Implementing this interface on an Objex object means it
 * will be called as part of the intra-object validation
 * step. Typically you do not use or implement this interface
 * directly, but use the Objex generators to generate out
 * the relevant code connecting to your @ObjexCheck methods.
 *
 * @author Tom Spencer
 */
@IntraObjectValid(groups={IntraObjectGroup.class})
public interface SelfIntraObjectValidator {

    /**
     * Called on the object exposing this interface to actually
     * validate itself with respect to its internal state.
     * 
     * @param context The validation context so errors can be raised
     * @return True if the object is valid, false otherwise
     */
    public boolean validateObject(ConstraintValidatorContext context);
}
