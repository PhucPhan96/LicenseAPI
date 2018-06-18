package nfc.model.ViewModel;

import java.math.BigDecimal;
import java.util.Date;

public class BillHistory {
    private String user_id;
    private String order_id;
    private Date order_date;
    private Date deliver_date;
    private String order_status;
    private BigDecimal prod_amt;
    private String app_id;
    private int prod_id;
    private int prod_qty;
    private String prod_name;    
    private int suppl_id;  
    private String supplier_name;
    private BigDecimal unit_price;
    private String lstOption;
    private String lstQty_Option;
    private BigDecimal order_amt;
 

   

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public BigDecimal getOrder_amt() {
        return order_amt;
    }

    public void setOrder_amt(BigDecimal order_amt) {
        this.order_amt = order_amt;
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
	
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public Date getDeliver_date() {
		return deliver_date;
	}
	public void setDeliver_date(Date deliver_date) {
		this.deliver_date = deliver_date;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public BigDecimal getProd_amt() {
		return prod_amt;
	}
	public void setProd_amt(BigDecimal prod_amt) {
		this.prod_amt = prod_amt;
	}
	public int getProd_qty() {
		return prod_qty;
	}
	public void setProd_qty(int prod_qty) {
		this.prod_qty = prod_qty;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	
	
	public BigDecimal getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}
	
	
	
	
	
	
	


	
}
