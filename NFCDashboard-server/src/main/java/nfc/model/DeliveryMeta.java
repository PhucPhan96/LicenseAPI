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
@Table(name="fg_delivery_meta")
public class DeliveryMeta {
    private String meta_key;
    private String meta_value;
    private int delivery_id;
    private long meta_id;

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

    @Column(name="delivery_id")
    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }
    
}
