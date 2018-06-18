package nfc.serviceImpl.integration.endpoint;

import java.util.stream.Collectors;
import nfc.messages.request.OrderStatusRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import nfc.model.ViewModel.OrderView;
import nfc.serviceImpl.common.Utils;

public class OrderRouter {
    
    public String routeOrder(OrderView orderView) {
        String result = "paymentOutputChannel"; 
        if(orderView.getOrder().getOrder_status().trim().equals(Utils.ORDER_FAILED)){
            result = "paymentOutputChannel";
        }
        else{
            result = "paymentChannel";
        }
        return result;	
    }
    
    
    public String routerOrderStatus(OrderStatusRequest orderStatusMessage){
        String channel = "orderStatusOutputChannel";
        String orderStatus = orderStatusMessage.getStatus();
        if(orderStatus.equals(Utils.ORDER_FAILED)){
            channel = "orderStatusOutputChannel";
        }
        else if(orderStatus.equals(Utils.ORDER_COMPLETE)){
            channel = "orderComplete";
        }
        else if(orderStatus.equals(Utils.ORDER_CANCEL)){
            channel = "orderCancel";
        }
        else if(orderStatus.equals(Utils.ORDER_REQUEST_CANCEL)){
            channel = "orderRequestCancel";
        }
        else if(orderStatus.equals(Utils.ORDER_COOKING)){
            channel = "orderCooking";
        }
        return channel;	
    }
}
