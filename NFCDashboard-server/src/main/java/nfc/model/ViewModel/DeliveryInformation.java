/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class DeliveryInformation {
    
    private BigDecimal order_amt;
    private String delivery_id;
    private String delivery_phone;
    private String delivery_addr;

    public BigDecimal getOrder_amt() {
        return order_amt;
    }

    public void setOrder_amt(BigDecimal order_amt) {
        this.order_amt = order_amt;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getDelivery_phone() {
        return delivery_phone;
    }

    public void setDelivery_phone(String delivery_phone) {
        this.delivery_phone = delivery_phone;
    }

    public String getDelivery_addr() {
        return delivery_addr;
    }

    public void setDelivery_addr(String delivery_addr) {
        this.delivery_addr = delivery_addr;
    }
    
}
