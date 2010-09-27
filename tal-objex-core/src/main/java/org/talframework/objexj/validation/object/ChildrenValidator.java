package org.talframework.objexj.validation.object;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.validation.groups.ChildGroup;

/**
 * This is the validator for custom inter object validation.
 * This is used by the Objex generators and will call any
 * @ObjexCheck method (marked as belonging to the 
 * {@link ChildGroup} group). It typically does nothing
 * if you have no such methods.
 *
 * @author Tom Spencer
 */
public class ChildrenValidator implements ConstraintValidator<ChildrenValid, SelfChildValidator> {

    public void initialize(ChildrenValid constraintAnnotation) {
    }
    
    public boolean isValid(SelfChildValidator value, ConstraintValidatorContext context) {
        return value.validateChildren(context);
    }
}
