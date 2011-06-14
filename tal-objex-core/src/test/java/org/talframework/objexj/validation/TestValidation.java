/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.objexj.ValidationError;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.ValidationRequest.ValidationType;
import org.talframework.objexj.exceptions.ObjectErrorException;
import org.talframework.objexj.object.testmodel.objex.Product;
import org.talframework.objexj.object.testmodel.objex.Stock;
import org.talframework.objexj.validation.groups.ChildGroup;
import org.talframework.objexj.validation.groups.FieldGroup;
import org.talframework.objexj.validation.groups.InterObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.InterObjectGroup;
import org.talframework.objexj.validation.groups.IntraObjectEnrichmentGroup;
import org.talframework.objexj.validation.groups.IntraObjectGroup;

/**
 * This class tests the basic validation strategy available
 * inside the validator.
 *
 * @author Tom Spencer
 */
public class TestValidation {
    
    private Mockery context = new JUnit4Mockery();
    
    /**
     * This test mocks out the validator to ensure the basic
     * method works
     */
    @Test
    public void basic() {
        final ValidationRequest request = context.mock(ValidationRequest.class);
        final Validator validator = context.mock(Validator.class);
        final Stock obj = new Stock();
        
        context.checking(new Expectations() {{
            allowing(request).getValidator(); will(returnValue(validator));
            
            // Intra Object Level
            oneOf(request).getValidationType(); will(returnValue(ValidationType.INTRA_OBJECT));
            oneOf(validator).validate(obj, IntraObjectEnrichmentGroup.class); will(returnValue(null));
            oneOf(validator).validate(obj, IntraObjectGroup.class, FieldGroup.class, Default.class); will(returnValue(null));
            
            // Inter Object Level
            oneOf(request).getValidationType(); will(returnValue(ValidationType.INTER_OBJECT));
            oneOf(validator).validate(obj, InterObjectEnrichmentGroup.class); will(returnValue(null));
            oneOf(validator).validate(obj, InterObjectGroup.class); will(returnValue(null));
            
            // Child Level
            oneOf(request).getValidationType(); will(returnValue(ValidationType.CHILDREN));
            oneOf(validator).validate(obj, ChildGroup.class); will(returnValue(null));
        }});
        
        obj.validate(request); // Intra
        obj.validate(request); // Inter
        obj.validate(request); // Child
        
        context.assertIsSatisfied();
    }
    
    /**
     * This tests that we validate correctly when setting a method. Note
     * that there is a dependency here on the class checking values as
     * we set them.
     */
    @Test(expected=ObjectErrorException.class)
    public void onSet() {
        Product obj = new Product("Product1", "Product1", 1, 10);
        
        obj.setName(null);
    }
    
    /**
     * This tests that the object checks itself
     */
    @Test
    public void selfIntraObjectChecks() {
        Product obj = new Product("Product1", "Product1", 1, 10);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final javax.validation.Validator validator = factory.getValidator();
        final ValidationRequest request = context.mock(ValidationRequest.class);
        final ValidationError expected = new SimpleValidationRequest.SimpleValidationError(null, "invalid.object.internal", null, new Object[]{obj});
        
        context.checking(new Expectations() {{
            oneOf(request).getValidator(); will(returnValue(validator));
            oneOf(request).getValidationType(); will(returnValue(ValidationType.INTRA_OBJECT));
            oneOf(request).addError(with(expected));
        }});
        
        obj.validate(request);
    }
}
