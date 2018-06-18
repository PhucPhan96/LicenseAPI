/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration.endpoint;

import nfc.messages.OrderMessage;
import nfc.model.Order;
import nfc.model.PaymentOrderHistory;
import nfc.model.ViewModel.OrderView;
import nfc.service.IOrderService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.payment.PaymentFactory;
import nfc.socket.DataQueue;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class PaymentServiceEndpoint {
    @Autowired
    private IOrderService orderDAO;
    @Autowired
    private IUserService userDAO;
    
    public OrderView payment(OrderView orderView)
    {
        Order order = orderView.getOrder();
        boolean resultPayment = PaymentFactory.getPaymentApi(orderView.getPayment_request().get("payment_code").toString()).payment(orderView.getPayment_request(), order.getOrder_id());
        if(resultPayment){
            updatePaymentStatus(order.getOrder_id(), Utils.ORDER_PAID);
            order.setOrder_status(Utils.ORDER_PAID);
            sendOrderToStore(orderView);
        }   
        else{
            order.setOrder_status(Utils.ORDER_FAILED);
            orderDAO.updateOrderStatus(order.getOrder_id(), Utils.ORDER_FAILED);
            sendOrderToStore(orderView);
        }
        return orderView;
    }
    
    private void updatePaymentStatus(String orderId, String status){
        orderDAO.updateOrderStatus(orderId, status);
    }
    
    private void sendOrderToStore(OrderView orderView){
        OrderMessage orderMessage = new OrderMessage(userDAO.getUserIdOfSupplier(orderView.getOrder().getSuppl_id()));
        //orderMessage.setCustomer_name(orderView.getCustomer_name());
        orderMessage.setLstOrderDetail(orderView.getLstOrderDetail());
        orderMessage.setOrder(orderView.getOrder());
        DataQueue.getInstance().addDataQueue(orderMessage);
    }
}
