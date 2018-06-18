/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

import java.util.ArrayList;
import java.util.List;
import nfc.messages.base.PaymentRequestPacket;
import nfc.messages.base.StoreBasePacket;
import nfc.model.Order;
import nfc.model.OrderDetail;

/**
 *
 * @author Admin
 */
public class OrderMessage extends StoreBasePacket{
    
    private Order order = new Order();
    private List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
    //private String customer_name;
    
    
    public OrderMessage(String user_id) {
        super(user_id);
        super.pkt_type = PacketType.ORDER_DATA;
    }   
	
//    public String getCustomer_name() {
//            return customer_name;
//    }
//    public void setCustomer_name(String customer_name) {
//            this.customer_name = customer_name;
//    }
    public Order getOrder() {
            return order;
    }
    public void setOrder(Order order) {
            this.order = order;
    }
    public List<OrderDetail> getLstOrderDetail() {
            return lstOrderDetail;
    }
    public void setLstOrderDetail(List<OrderDetail> lstOrderDetail) {
            this.lstOrderDetail = lstOrderDetail;
    }    
    
    
    
}
