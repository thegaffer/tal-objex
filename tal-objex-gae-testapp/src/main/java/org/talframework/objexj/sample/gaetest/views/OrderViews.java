/*
 * Copyright 2009 Thomas Spencer
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
            model.put("item", repo.getOrder().getItemById(state.getCurrentOrderItem()));
            
            return "/html/order/orderItemForm.jsp";
        }
    }
}
