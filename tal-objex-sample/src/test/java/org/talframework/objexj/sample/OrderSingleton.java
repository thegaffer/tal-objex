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
package org.talframework.objexj.sample;

import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleContainerStrategy;
import org.talframework.objexj.object.ObjectStrategyCompiler;
import org.talframework.objexj.sample.orders.OrderImpl;
import org.talframework.objexj.sample.orders.OrderItemImpl;

/**
 * This class provides the configuration of the order container.
 * Ordinarily you would probably configure this via a DI tool, but
 * this serves to show how this is setup in code so we have no
 * dependencies. 
 *
 * @author Tom Spencer
 */
public class OrderSingleton {

    public static final ContainerStrategy STRATEGY;
    
    static {
        STRATEGY = new SimpleContainerStrategy("Order", "Order",
                ObjectStrategyCompiler.calculateStrategy("Order", new String[]{"items"}, null, OrderImpl.class),
                ObjectStrategyCompiler.calculateStrategy("OrderItem", null, null, OrderItemImpl.class));
    }
}
