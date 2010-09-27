package org.talframework.objexj.validation.groups;

/**
 * This interface is used to mark validation annotations
 * to apply only when the field actually changes. 
 * 
 * </p>These validations are not applied when validating  
 * the object prior to change and only stop you actually 
 * changing the field. Future dates are a good candidate 
 * because you might want to enforce a user can only set 
 * it to a date in the future, but when viewing a record 
 * in the past and not changing the date then no probs -
 * of course you may equally say it's invalid to edit
 * an object that is in the past, but with this group
 * the choice is yours!</p>
 *
 * @author Tom Spencer
 */
public interface FieldChangeGroup {
}
