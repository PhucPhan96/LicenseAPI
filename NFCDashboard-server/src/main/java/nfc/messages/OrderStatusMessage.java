/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

import nfc.messages.base.StoreBasePacket;

/**
 *
 * @author Admin
 */
public class OrderStatusMessage extends StoreBasePacket{
    
    private String orderId;
    private String status;
    private int storeId;
    
    public OrderStatusMessage(String userId, PacketType type) {
        super(userId);
        super.pkt_type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
