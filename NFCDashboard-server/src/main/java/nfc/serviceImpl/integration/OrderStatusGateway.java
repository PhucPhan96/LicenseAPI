/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration;
import nfc.messages.request.OrderStatusRequest;
import org.springframework.integration.annotation.Gateway;

/**
 *
 * @author Admin
 */
public interface OrderStatusGateway {
    @Gateway(requestChannel="orderProcess")
    void sendOrderMessage(OrderStatusRequest request);
    public OrderStatusRequest receive();
    
}
