package org.talframework.objexj.service.beans.rules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.talframework.objexj.object.testmodel.api.ICategory;

public class CategoryValidator implements ConstraintValidator<CategoryValid, ICategory> {

    public void initialize(CategoryValid constraintAnnotation) {
    }
    
    public boolean isValid(ICategory value, ConstraintValidatorContext context) {
        if( value != null &&
                value.getName() != null && 
                value.getName().equals("RootCat_edited") ) return false;
        
        return true;
    }
}
