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
@Table(name="fg_payments")
public class Payment {
    private int payment_id;
    private String url_payment;
    private String url_cancel;
    private String payment_name;
    private String payment_description;
    private Boolean is_default;
    private String username;
    private String password;
    private Boolean is_oauth;
    private String oauth_url;
    private String grant_type;
    
    @Id
    @Column(name="payment_id")
    @GeneratedValue
    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
  
    @Column(name="url_payment")
    public String getUrl_payment() {
        return url_payment;
    }


    public void setUrl_payment(String url_payment) {
        this.url_payment = url_payment;
    }
    
    @Column(name="url_cancel")
    public String getUrl_cancel() {
        return url_cancel;
    }

    public void setUrl_cancel(String url_cancel) {
        this.url_cancel = url_cancel;
    }
    
    @Column(name="payment_name")
    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }
    
    @Column(name="payment_description")
    public String getPayment_description() {
        return payment_description;
    }

    public void setPayment_description(String payment_description) {
        this.payment_description = payment_description;
    }

    @Column(name="is_default")
    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="is_oauth")
    public Boolean getIs_oauth() {
        return is_oauth;
    }

    public void setIs_oauth(Boolean is_oauth) {
        this.is_oauth = is_oauth;
    }

    @Column(name="oauth_url")
    public String getOauth_url() {
        return oauth_url;
    }

    public void setOauth_url(String oauth_url) {
        this.oauth_url = oauth_url;
    }

    @Column(name="grant_type")
    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
    
    
    
    
    
}
