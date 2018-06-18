package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_user_address")
public class UserAddress {
	private String app_id;
	private String user_id;
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
	@Column(name="user_id")
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Id
	@Column(name="addr_id")
	public int getAddr_id() {
		return addr_id;
	}
	public void setAddr_id(int addr_id) {
		this.addr_id = addr_id;
	}
	@Column(name="is_main")
	public boolean isIs_main() {
		return is_main;
	}
	public void setIs_main(boolean is_main) {
		this.is_main = is_main;
	}
	@Column(name="is_deliver")
	public boolean isIs_deliver() {
		return is_deliver;
	}
	public void setIs_deliver(boolean is_deliver) {
		this.is_deliver = is_deliver;
	}
	
	

}
