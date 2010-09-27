package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.ChildGroup;

/**
 * Implementing this interface on an Objex object means it
 * will be called as part of the child validation step.
 * Typically you do not use or implement this interface
 * directly, but use the Objex generators to generate out
 * the relevant code connecting to your @ObjexCheck methods.
 *
 * @author Tom Spencer
 */
@ChildrenValid(groups={ChildGroup.class})
public interface SelfChildValidator {

    /**
     * Called on the object exposing this interface to actually
     * validate this objects children - at least one of which
     * is in the transaction.
     * 
     * @param context The validation context so errors can be raised
     * @return True if the children are valid, false otherwise
     */
    public boolean validateChildren(ConstraintValidatorContext context);
}
