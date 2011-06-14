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
package org.talframework.objexj.sample.gaetest.views;

import java.util.Map;

import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.api.repository.OrderService;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp;
import org.talframework.objexj.sample.gaetest.OrderServiceFactory;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp.SampleAppState;

public class OrderViews {

    public static class OrderView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getRepository(state.getCurrentOrder());
            
            model.put("order", repository.getOrder());
            
            return "/html/order/order.jsp";
        }
    }
    
    public static class NewOrderFormView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            return "/html/order/newOrderForm.jsp";
        }
    }
    
    public static class OrderFormView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repo = service.getOpenRepository(state.getCurrentOrder());
            
            model.put("order", repo.getOrder());
            
            return "/html/order/orderForm.jsp";
        }
    }
    
    public static class NewOrderItemView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repo = service.getOpenRepository(state.getCurrentOrder());
            
            model.put("order", repo.getOrder());
            model.put("newOrderItem", true);
            
            return "/html/order/orderItemForm.jsp";
        }
    }
    
    public static class EditOrderItemView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repo = service.getOpenRepository(state.getCurrentOrder());
            
            model.put("order", repo.getOrder());
            model.put("item", repo.getOrderItem(state.getCurrentOrderItem()));
            
            return "/html/order/orderItemForm.jsp";
        }
    }
}
