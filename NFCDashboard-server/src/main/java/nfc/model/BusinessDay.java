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
@Table(name="fg_businessDays")
public class BusinessDay {
    private int businessDays_id;
    private String businessDays_timeFrom;
    private String businessDays_timeTo;
    private Date businessDays_date;
    private String businessDays_reason;
    private int suppl_id;

    @Id
    @Column(name="businessDays_id")
    @GeneratedValue
    public int getBusinessDays_id() {
        return businessDays_id;
    }

    public void setBusinessDays_id(int businessDays_id) {
        this.businessDays_id = businessDays_id;
    }
    @Column(name="businessDays_timeFrom")
    public String getBusinessDays_timeFrom() {
        return businessDays_timeFrom;
    }

    public void setBusinessDays_timeFrom(String businessDays_timeFrom) {
        this.businessDays_timeFrom = businessDays_timeFrom;
    }
    @Column(name="businessDays_timeTo")
    public String getBusinessDays_timeTo() {
        return businessDays_timeTo;
    }

    public void setBusinessDays_timeTo(String businessDays_timeTo) {
        this.businessDays_timeTo = businessDays_timeTo;
    }
    @Column(name="businessDays_date")
    public Date getBusinessDays_date() {
        return businessDays_date;
    }

    public void setBusinessDays_date(Date businessDays_date) {
        this.businessDays_date = businessDays_date;
    }
    @Column(name="businessDays_reason")
    public String getBusinessDays_reason() {
        return businessDays_reason;
    }

    public void setBusinessDays_reason(String businessDays_reason) {
        this.businessDays_reason = businessDays_reason;
    }
    @Column(name="suppl_id")
    public int getSuppl_id() {
        return suppl_id;
    }

    public void setSuppl_id(int suppl_id) {
        this.suppl_id = suppl_id;
    }
    
    
}
