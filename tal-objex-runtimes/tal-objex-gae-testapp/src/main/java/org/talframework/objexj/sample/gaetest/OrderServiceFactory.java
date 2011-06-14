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
package org.talframework.objexj.sample.gaetest;

import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.runtime.gae.GAEMiddlewareFactory;
import org.talframework.objexj.sample.api.repository.OrderService;

public class OrderServiceFactory {
    private static final OrderServiceFactory INSTANCE = new OrderServiceFactory();
    
    private OrderService service = null;
    
    private OrderServiceFactory() {
        try {
            ContainerMiddlewareFactory middlewareFactory = new GAEMiddlewareFactory();
            
            Class<?> cls = Class.forName("org.talframework.objexj.sample.model.order.impl.OrderServiceImpl");
            service = (OrderService)cls.getConstructor(ContainerMiddlewareFactory.class).newInstance(middlewareFactory);
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot initialise order service, likely a coding or config error", e);
        }
    }
    
    public static OrderServiceFactory getInstance() {
        return INSTANCE;
    }
    
    public OrderService getService() {
        return service;
    }
}
