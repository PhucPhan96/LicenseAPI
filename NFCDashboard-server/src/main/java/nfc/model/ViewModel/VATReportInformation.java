/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class VATReportInformation {
    private Date order_date;
    private String supplier_name;
    private String user_address;
    private String customer_address;
    private BigDecimal prod_amt;
    private BigDecimal disc_amt;

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public BigDecimal getProd_amt() {
        return prod_amt;
    }

    public void setProd_amt(BigDecimal prod_amt) {
        this.prod_amt = prod_amt;
    }

    public BigDecimal getDisc_amt() {
        return disc_amt;
    }

    public void setDisc_amt(BigDecimal disc_amt) {
        this.disc_amt = disc_amt;
    }
    
    
    
}
