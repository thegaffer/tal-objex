/*
 * Copyright 2010 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.objexj.runtime.rs.service;

import javax.ws.rs.Path;

import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.container.middleware.InMemoryMiddlewareFactory;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SimpleContainerFactory;
import org.talframework.objexj.runtime.rs.service.ObjexDocumentResource;
import org.talframework.objexj.sample.model.stock.impl.CategoryImpl;
import org.talframework.objexj.sample.model.stock.impl.ProductImpl;

/**
 * This class is the derived sample container resource that
 * exposes the sample stock container as a Restful WebService.
 *
 * @author Tom Spencer
 */
@Path("/test")
public class SampleContainerResource extends ObjexDocumentResource {
    
    private static ContainerStrategy strategy;
    
    static {
        strategy = new SimpleContainerStrategy("Stock", "Stock", "Category", CategoryImpl.STRATEGY, ProductImpl.STRATEGY);
    }
    
    @Override
    protected ContainerFactory getFactory() {
        return new SimpleContainerFactory(strategy, new InMemoryMiddlewareFactory());
    }
}
