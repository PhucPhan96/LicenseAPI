/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

import nfc.messages.base.CustomerBasePacket;

/**
 *
 * @author Admin
 */
public class OrderStatusCustomerMessage extends CustomerBasePacket{
    
    private String orderId;
    
    public OrderStatusCustomerMessage(String uuid, PacketType type) {
        super(uuid);
        super.pkt_type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
