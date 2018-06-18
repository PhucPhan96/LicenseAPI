package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_applications")
public class Application {
	private int app_id;
	private String app_name;
	private String app_path;
	private String app_info;
	@Id
	@Column(name="app_id")
	public int getApp_id() {
		return app_id;
	}
	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	@Column(name="app_name")
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	@Column(name="app_path")
	public String getApp_path() {
		return app_path;
	}
	public void setApp_path(String app_path) {
		this.app_path = app_path;
	}
	@Column(name="app_info")
	public String getApp_info() {
		return app_info;
	}
	public void setApp_info(String app_info) {
		this.app_info = app_info;
	}
	
}
