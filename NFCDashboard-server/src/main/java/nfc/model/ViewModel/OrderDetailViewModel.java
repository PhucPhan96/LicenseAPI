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
public class OrderDetailViewModel {
    
    private String order_id;
    private int prod_id;
    private BigDecimal unit_price;
    private int prod_qty;
    private BigDecimal disc_amt;
    private BigDecimal disc_rate;
    private BigDecimal deliver_amt;
    private BigDecimal prod_amt;
    private String prod_name;
    private String lstOption;
    private String lstQty_Option;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public int getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(int prod_qty) {
        this.prod_qty = prod_qty;
    }

    public BigDecimal getDisc_amt() {
        return disc_amt;
    }

    public void setDisc_amt(BigDecimal disc_amt) {
        this.disc_amt = disc_amt;
    }

    public BigDecimal getDisc_rate() {
        return disc_rate;
    }

    public void setDisc_rate(BigDecimal disc_rate) {
        this.disc_rate = disc_rate;
    }

    public BigDecimal getDeliver_amt() {
        return deliver_amt;
    }

    public void setDeliver_amt(BigDecimal deliver_amt) {
        this.deliver_amt = deliver_amt;
    }

    public BigDecimal getProd_amt() {
        return prod_amt;
    }

    public void setProd_amt(BigDecimal prod_amt) {
        this.prod_amt = prod_amt;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getLstOption() {
        return lstOption;
    }

    public void setLstOption(String lstOption) {
        this.lstOption = lstOption;
    }

    public String getLstQty_Option() {
        return lstQty_Option;
    }

    public void setLstQty_Option(String lstQty_Option) {
        this.lstQty_Option = lstQty_Option;
    }
    
    
    
}
