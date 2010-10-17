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

package org.talframework.objexj.runtime.rs.service.middleware;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.talframework.objexj.runtime.rs.MiddlewareRequest;
import org.talframework.objexj.runtime.rs.MiddlewareResult;
import org.talframework.objexj.sample.beans.order.OrderBean;
import org.talframework.objexj.sample.beans.order.OrderItemBean;
import org.talframework.objexj.sample.beans.stock.CategoryBean;
import org.talframework.objexj.sample.beans.stock.ProductBean;

/**
 *
 *
 * @author Tom Spencer
 */
@Provider
@Produces(MediaType.APPLICATION_XML)
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private final JAXBContext context;

    public JAXBContextResolver() throws Exception {
        this.context = JAXBContext.newInstance(
                MiddlewareResult.class, 
                MiddlewareRequest.class, 
                CategoryBean.class,
                ProductBean.class,
                OrderBean.class,
                OrderItemBean.class);
    }

    public JAXBContext getContext(Class<?> objectType) {
        return context;
    } 


}