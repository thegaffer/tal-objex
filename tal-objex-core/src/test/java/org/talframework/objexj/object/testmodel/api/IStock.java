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