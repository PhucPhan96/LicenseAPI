/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name="fg_payment_meta")
public class PaymentMeta {
    private long meta_id;
    private String meta_key;
    private String meta_value;
    private int payment_id;
    private String payment_type;
    private String meta_map_key;
    
    @Id
    @Column(name="meta_id")
    @GeneratedValue
    public long getMeta_id() {
        return meta_id;
    }

    public void setMeta_id(long meta_id) {
        this.meta_id = meta_id;
    }

    @Column(name="meta_key")
    public String getMeta_key() {
        return meta_key;
    }

    public void setMeta_key(String meta_key) {
        this.meta_key = meta_key;
    }

    @Column(name="meta_value")
    public String getMeta_value() {
        return meta_value;
    }

    public void setMeta_value(String meta_value) {
        this.meta_value = meta_value;
    }

    @Column(name="payment_id")
    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    @Column(name="payment_type")
    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    @Column(name="meta_map_key")
    public String getMeta_map_key() {
        return meta_map_key;
    }

    public void setMeta_map_key(String meta_map_key) {
        this.meta_map_key = meta_map_key;
    }
    
    
    
    
}
