/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import java.util.Date;
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
@Table(name="fg_delivery")
public class Delivery {
    private int delivery_id;
    private String delivery_name;
    private String delivery_url;
    private String delivery_desc;
    private Date date_create;
    private String delivery_method;

    @Id
    @Column(name="delivery_id")
    @GeneratedValue
    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    @Column(name="delivery_name")
    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    @Column(name="delivery_url")
    public String getDelivery_url() {
        return delivery_url;
    }

    public void setDelivery_url(String delivery_url) {
        this.delivery_url = delivery_url;
    }

    @Column(name="delivery_desc")
    public String getDelivery_desc() {
        return delivery_desc;
    }

    public void setDelivery_desc(String delivery_desc) {
        this.delivery_desc = delivery_desc;
    }

    @Column(name="date_create")
    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }
    
    
    
}
