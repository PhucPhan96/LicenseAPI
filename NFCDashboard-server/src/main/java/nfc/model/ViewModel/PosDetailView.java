package nfc.model.ViewModel;

import java.util.List;
import nfc.model.Customer;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.User;

public class PosDetailView {
    private Order order;
    private Customer customer;
    //private User user;
    private List<OrderDetailViewModel> listOrderDetailView;
    private String payment_code;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetailViewModel> getListOrderDetailView() {
        return listOrderDetailView;
    }

    public void setListOrderDetailView(List<OrderDetailViewModel> listOrderDetailView) {
        this.listOrderDetailView = listOrderDetailView;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }    
	
}
