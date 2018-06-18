package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import nfc.messages.base.PaymentRequestPacket;
import nfc.messages.request.PayRequest;
import nfc.model.Customer;

import nfc.model.Order;
import nfc.model.OrderDetail;

public class OrderView {
    
    private Order order = new Order();
    private List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
    //private PayRequest payment_request;
    private LinkedHashMap<String, String> payment_request;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    
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
        
//    public PayRequest getPayment_request() {
//        return payment_request;
//    }
//
//    public void setPayment_request(PayRequest payment_request) {
//        this.payment_request = payment_request;
//    }

    public LinkedHashMap<String, String> getPayment_request() {
        return payment_request;
    }

    public void setPayment_request(LinkedHashMap<String, String> payment_request) {
        this.payment_request = payment_request;
    }
    
    
    
}
