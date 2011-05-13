package org.talframework.objexj.object.testmodel.api;

import java.util.List;

/**
 * This represents an interface to a stock object which we use in our
 * tests.
 * 
 * <p><b>Authors Note: </b>I don't like the use of the prefix 'I', but
 * I want to name the interfaces and the objects the same in the tests.
 * Normally you would name the interfaces pure and suffix the impl
 * classes in some way.</p>
 *
 * @author Tom Spencer
 */
public interface IStock {

    /**
     * @return the categories
     */
    public List<ICategory> getCategories();

    /**
     * Setter for the categories field
     *
     * @param categories the categories to set
     */
    public void setCategories(List<ICategory> categories);

}