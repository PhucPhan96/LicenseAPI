package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_roles")
public class Role {
	private int role_id;
	private String app_id;
	private String role_name;
	private boolean is_fixed;
	private String role_desc;
        private int parent_id;
	@Id
	@Column(name="role_id")
	@GeneratedValue
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Column(name="role_name")
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	@Column(name="is_fixed")
	public boolean getIs_fixed() {
		return is_fixed;
	}
	public void setIs_fixed(boolean is_fixed) {
		this.is_fixed = is_fixed;
	}
	@Column(name="role_desc")
	public String getRole_desc() {
		return role_desc;
	}
	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}
        
        @Column(name="parent_id")
        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }
        
        
	
}
