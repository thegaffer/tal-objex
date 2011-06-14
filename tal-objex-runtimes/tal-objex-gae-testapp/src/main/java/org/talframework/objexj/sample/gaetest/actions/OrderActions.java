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
package org.talframework.objexj.sample.gaetest.actions;

import javax.servlet.http.HttpServletRequest;

import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.api.order.OrderItem;
import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.api.repository.OrderService;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp;
import org.talframework.objexj.sample.gaetest.OrderServiceFactory;
import org.talframework.objexj.sample.gaetest.GAEObjexSampleApp.SampleAppState;
import org.talframework.objexj.sample.gaetest.util.RequestUtil;

public class OrderActions {
    
    public static class ViewOrdersAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            return "/viewOrders";
        }
    }
    
    public static class ViewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            state.setCurrentOrder(RequestUtil.getParameter(request, "orderId"));
            state.setCurrentOrderItem(null);
            
            return "/viewOrder";
        }
    }
    
    public static class NewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            state.setCurrentOrder(null);
            request.setAttribute("newOrder", true);
            
            return "/newOrderForm";
        }
    }
    
    public static class EditOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            return "/orderForm";
        }
    }
    
    public static class NewOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            request.setAttribute("newOrderItem", true);
            return "/newOrderItem";
        }
    }
    
    public static class EditOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            String item = RequestUtil.getParameter(request, "item");
            if( item != null ) state.setCurrentOrderItem(item);
            
            if( state.getCurrentOrderItem() != null ) {
                return "editOrderItem";
            }
            else {
                return "/viewOrder";
            }
            
        }
    }
    
    public static class OpenOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder());
            state.setCurrentOrder(repository.suspend());
            state.setInTransaction(true);
            
            return "/viewOrder";
        }
    }
    
    public static class SaveOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder());
            repository.persist();
            state.setInTransaction(false);
            state.setCurrentOrder(repository.getId());
            
            return "/viewOrder";
        }
    }
    
    public static class SubmitOrderItemAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder());
            
            Order order = repository.getOrder();
            
            String itemId = RequestUtil.getParameter(request, "id");
            
            OrderItem item = null;
            if( itemId == null ) item = order.createItem();
            else item = repository.getOrderItem(itemId);
            
            // Bind
            item.setName(RequestUtil.getParameter(request, "name"));
            item.setDescription(RequestUtil.getParameter(request, "description"));
            item.setPrice(RequestUtil.getDoubleParameter(request, "price"));
            item.setCurrency(RequestUtil.getParameter(request, "currency"));
            item.setQuantity(RequestUtil.getDoubleParameter(request, "quantity"));
            item.setMeasure(RequestUtil.getParameter(request, "measure"));
            
            state.setCurrentOrder(repository.suspend());
            state.setInTransaction(true);
            
            return "/viewOrder";
        }
    }
    
    public static class SubmitNewOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            
            String account = RequestUtil.getParameter(request, "account");
            
            OrderRepository repository = service.createNewOrder();
            repository.getOrder().setAccount(Long.parseLong(account));
            
            state.setCurrentOrder(repository.suspend());
            state.setInTransaction(true);
            
            return "/viewOrder";
        }
    }
    
    public static class SubmitOrderAction implements GAEObjexSampleApp.Action {
        public String execute(HttpServletRequest request, SampleAppState state) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = service.getOpenRepository(state.getCurrentOrder());
            
            // Bind
            Order order = repository.getOrder();
            order.setAccount(RequestUtil.getLongParameter(request, "account"));
            
            state.setCurrentOrder(repository.suspend());
            state.setInTransaction(true);
            
            return "/viewOrder";
        }
    }
}
