/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name="fg_payment_order_history")
public class PaymentOrderHistory {
    
    private String order_id;
    private String payment_unique_number;
    private String card_nm;
    private String card_id;
    private String payment_code;

    @Id
    @Column(name="order_id")
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Column(name="payment_unique_number")
    public String getPayment_unique_number() {
        return payment_unique_number;
    }

    public void setPayment_unique_number(String payment_unique_number) {
        this.payment_unique_number = payment_unique_number;
    }

    @Column(name="card_nm")
    public String getCard_nm() {
        return card_nm;
    }

    public void setCard_nm(String card_nm) {
        this.card_nm = card_nm;
    }

    @Column(name="card_id")
    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    @Column(name="payment_code")
    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }
    
    
    
    
}
