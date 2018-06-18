package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import nfc.model.PKModel.SupplierAddressPK;

@Entity
@Table(name="fg_supplier_address")
@IdClass(SupplierAddressPK.class)
public class SupplierAddress {
	@Id
	private String app_id;
	@Id
	private int suppl_id;
	@Id
	private int addr_id; 
	private boolean is_main; 
	private boolean is_deliver;
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Column(name="suppl_id")
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	@Column(name="addr_id")
	public int getAddr_id() {
		return addr_id;
	}
	public void setAddr_id(int addr_id) {
		this.addr_id = addr_id;
	}
	@Column(name="is_main")
	public boolean getIs_main() {
		return is_main;
	}
	public void setIs_main(boolean is_main) {
		this.is_main = is_main;
	}
	@Column(name="is_deliver")
	public boolean getIs_deliver() {
		return is_deliver;
	}
	public void setIs_deliver(boolean is_deliver) {
		this.is_deliver = is_deliver;
	}
}
