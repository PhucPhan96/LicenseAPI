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
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Admin
 */
@Entity
@Table(name="fg_discount")
public class Discount {
    
    private String discount_id;
    private Date discount_date;
    private String discount_value;
    private String discount_time_from;
    private String discount_time_to;
    private int suppl_id;
    private String discount_type;

    @Id
    @Column(name="discount_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
    }
    
    @Column(name="discount_date")
    public Date getDiscount_date() {
        return discount_date;
    }

    public void setDiscount_date(Date discount_date) {
        this.discount_date = discount_date;
    }

    @Column(name="discount_value")
    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
    }

    @Column(name="discount_time_from")
    public String getDiscount_time_from() {
        return discount_time_from;
    }

    public void setDiscount_time_from(String discount_time_from) {
        this.discount_time_from = discount_time_from;
    }

    @Column(name="discount_time_to")
    public String getDiscount_time_to() {
        return discount_time_to;
    }

    public void setDiscount_time_to(String discount_time_to) {
        this.discount_time_to = discount_time_to;
    }

    @Column(name="suppl_id")
    public int getSuppl_id() {
        return suppl_id;
    }

    public void setSuppl_id(int suppl_id) {
        this.suppl_id = suppl_id;
    }

    @Column(name="discount_type")
    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }
    
    
}
