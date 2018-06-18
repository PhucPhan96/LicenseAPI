/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration.endpoint;

import nfc.messages.OrderMessage;
import nfc.model.ViewModel.OrderView;
import nfc.socket.DataQueue;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

/**
 *
 * @author Admin
 */

@MessageEndpoint
public class StoreEndpoint {
    
    public void processMessage(Message<OrderView> message) {
//        OrderView orderView =  message.getPayload();
//        OrderMessage orderMessage = new OrderMessage(orderView.getOrder().getSuppl_id());
//        orderMessage.setCustomer_name(orderView.getCustomer_name());
//        orderMessage.setLstOrderDetail(orderView.getLstOrderDetail());
//        orderMessage.setOrder(orderView.getOrder());
//        DataQueue.getInstance().addDataQueue(orderMessage);
//        System.err.println("order from store endpoint " + orderView.getClass());
        //logger.debug("In StoreEndpoint.  title='{}'  quantity={}  orderType={}", 
//                new Object[] { order.getTitle(), 
//                               order.getQuantity(),
//                               order.getOrderType() });
    }
}
