package nfc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_user_regist")
public class UserRegister {
	private int req_id; 
	private Date req_time;
	private String req_name;
	private String req_mobile;
	private String req_email;
	private String req_code;
	private boolean req_approved;
	private String req_address;
	
	@Id
	@Column(name="req_id")
	public int getReq_id() {
		return req_id;
	}
	public void setReq_id(int req_id) {
		this.req_id = req_id;
	}
	@Column(name="req_time")
	public Date getReq_time() {
		return req_time;
	}
	public void setReq_time(Date req_time) {
		this.req_time = req_time;
	}
	@Column(name="req_name")
	public String getReq_name() {
		return req_name;
	}
	public void setReq_name(String req_name) {
		this.req_name = req_name;
	}
	@Column(name="req_mobile")
	public String getReq_mobile() {
		return req_mobile;
	}
	public void setReq_mobile(String req_mobile) {
		this.req_mobile = req_mobile;
	}
	@Column(name="req_email")
	public String getReq_email() {
		return req_email;
	}
	public void setReq_email(String req_email) {
		this.req_email = req_email;
	}
	@Column(name="req_code")
	public String getReq_code() {
		return req_code;
	}
	public void setReq_code(String req_code) {
		this.req_code = req_code;
	}
	@Column(name="req_approved")
	public boolean isReq_approved() {
		return req_approved;
	}
	public void setReq_approved(boolean req_approved) {
		this.req_approved = req_approved;
	}
	@Column(name="req_address")
	public String getReq_address() {
		return req_address;
	}
	public void setReq_address(String req_address) {
		this.req_address = req_address;
	}
}
