package nfc.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import nfc.model.PKModel.OrderDetailPK;

@Entity
@Table(name="fg_order_details")
@IdClass(OrderDetailPK.class)
public class OrderDetail {
    private String order_id;
    private int prod_id;
    private BigDecimal unit_price;
    private int prod_qty;
    private BigDecimal disc_amt;
    private BigDecimal disc_rate;
    private BigDecimal deliver_amt;
    private BigDecimal prod_amt;
    private String lstOption;
    private String lstQty_Option;
    private String lstAdd;
    private String lstQty_Add;
    
    @Id
    @Column(name="order_id")
    public String getOrder_id() {
            return order_id;
    }
    public void setOrder_id(String order_id) {
            this.order_id = order_id;
    }
    @Id
    @Column(name="prod_id")
    public int getProd_id() {
            return prod_id;
    }
    public void setProd_id(int prod_id) {
            this.prod_id = prod_id;
    }
    @Column(name="unit_price")
    public BigDecimal getUnit_price() {
            return unit_price;
    }
    public void setUnit_price(BigDecimal unit_price) {
            this.unit_price = unit_price;
    }
    @Column(name="prod_qty")
    public int getProd_qty() {
            return prod_qty;
    }
    public void setProd_qty(int prod_qty) {
            this.prod_qty = prod_qty;
    }
    @Column(name="disc_amt")
    public BigDecimal getDisc_amt() {
            return disc_amt;
    }
    public void setDisc_amt(BigDecimal disc_amt) {
            this.disc_amt = disc_amt;
    }
    @Column(name="disc_rate")
    public BigDecimal getDisc_rate() {
            return disc_rate;
    }
    public void setDisc_rate(BigDecimal disc_rate) {
            this.disc_rate = disc_rate;
    }
    @Column(name="deliver_amt")
    public BigDecimal getDeliver_amt() {
            return deliver_amt;
    }
    public void setDeliver_amt(BigDecimal deliver_amt) {
            this.deliver_amt = deliver_amt;
    }
    @Column(name="prod_amt")
    public BigDecimal getProd_amt() {
            return prod_amt;
    }
    public void setProd_amt(BigDecimal prod_amt) {
            this.prod_amt = prod_amt;
    }

    @Column(name="lstOption")
    public String getLstOption() {
        return lstOption;
    }

    public void setLstOption(String lstOption) {
        this.lstOption = lstOption;
    }
    
    @Column(name="lstQty_Option")
    public String getLstQty_Option() {
        return lstQty_Option;
    }

    public void setLstQty_Option(String lstQty_Option) {
        this.lstQty_Option = lstQty_Option;
    }

    @Column(name="lstAdd")
    public String getLstAdd() {
        return lstAdd;
    }  

    public void setLstAdd(String lstAdd) {
        this.lstAdd = lstAdd;
    }

    @Column(name="lstQty_Add")
    public String getLstQty_Add() {
        return lstQty_Add;
    }

    public void setLstQty_Add(String lstQty_Add) {
        this.lstQty_Add = lstQty_Add;
    }
    
    
    
    
    
}
