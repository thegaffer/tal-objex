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
package org.talframework.objexj.runtime.rs.test.container;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.talframework.objexj.runtime.rs.DocumentResult;
import org.talframework.objexj.sample.model.order.impl.OrderImpl;
import org.talframework.objexj.sample.model.order.impl.OrderItemImpl;
import org.talframework.objexj.sample.model.stock.impl.CategoryImpl;
import org.talframework.objexj.sample.model.stock.impl.ProductImpl;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

/**
 * This is the default context resolver for JSON. We override
 * this because the default mapped JSON configuration does not
 * handle numbers and primitives as anything but a string and
 * will make a difference between single and multi-valued lists
 * and arrays.
 *
 * @author Tom Spencer
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JSONContextResolver implements ContextResolver<JAXBContext> {

    private static final Class<?>[] types = {
        DocumentResult.class,
        CategoryImpl.class, 
        ProductImpl.class, 
        OrderImpl.class, 
        OrderItemImpl.class};
    private final JAXBContext context;

    public JSONContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        return context;
    } 

}
