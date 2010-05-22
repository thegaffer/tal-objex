package org.tpspencer.tal.gaetest.views;

import java.util.Map;

import org.tpspencer.tal.gaetest.GAEObjexSampleApp;
import org.tpspencer.tal.gaetest.OrderServiceFactory;
import org.tpspencer.tal.gaetest.GAEObjexSampleApp.SampleAppState;
import org.tpspencer.tal.objexj.sample.api.repository.OrderRepository;
import org.tpspencer.tal.objexj.sample.api.repository.OrderService;

public class OrderViews {

    public static class OrderView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repository = null;
            
            if( state.getCurrentOrderTransaction() != null ) {
                repository = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            }
            else {
                repository = service.getRepository(state.getCurrentOrder());
            }
            
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
            OrderRepository repo = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            
            model.put("order", repo.getOrder());
            
            return "/html/order/orderForm.jsp";
        }
    }
    
    public static class NewOrderItemView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repo = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            
            model.put("order", repo.getOrder());
            model.put("newOrderItem", true);
            
            return "/html/order/orderItemForm.jsp";
        }
    }
    
    public static class EditOrderItemView implements GAEObjexSampleApp.View {
        public String prepare(SampleAppState state, Map<String, Object> model) {
            OrderService service = OrderServiceFactory.getInstance().getService();
            OrderRepository repo = service.getOpenRepository(state.getCurrentOrder(), state.getCurrentOrderTransaction());
            
            model.put("order", repo.getOrder());
            model.put("item", repo.getOrder().getItem(state.getCurrentOrderItem()));
            
            return "/html/order/orderItemForm.jsp";
        }
    }
}
