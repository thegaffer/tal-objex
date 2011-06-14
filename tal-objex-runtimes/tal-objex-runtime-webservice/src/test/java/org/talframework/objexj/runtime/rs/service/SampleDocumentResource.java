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
package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.Path;

import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.sample.model.stock.impl.CategoryImpl;
import org.talframework.objexj.sample.model.stock.impl.ProductImpl;

/**
 * This class is the derived sample container resource that
 * exposes the sample stock container as a Restful WebService.
 *
 * @author Tom Spencer
 */
@Path("/test")
public class SampleDocumentResource extends ObjexDocumentResource {
    
    private static ContainerStrategy strategy;
    
    static {
        strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", CategoryImpl.STRATEGY, ProductImpl.STRATEGY);
    }
    
    public SampleDocumentResource() {
        super("Stock", new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory()));
    }
}
