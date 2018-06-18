package nfc.model;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import nfc.model.ViewModel.UserAddressView;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="fg_users")
public class User {
	private String user_id;
	private String user_name;
	private String luser_name;
	private String user_alias;
	private String last_name;
	private String first_name;
	private String is_anonymous;
	private String password;
	private String password_salt;
	private boolean is_lockedout;
	private boolean is_registered;
	private Date created_date;
	private Date last_act_date;
	private Date last_login_date;
	private Date last_password_changed_date;
	private Date last_locked_date;
	private int failed_password_count;
	private String middle_name;
	private String mobile_no;
	private String phone_no;
	private String english_name;
	private String idcard_no;
	private boolean sex_type;
	private Date registered_date;
	private boolean is_active;
	private String email;
	private String app_id;
	private Date password_expired_date;
        private Date birth_date;
	private int role_id;
        
        private String user_reg_num;
        private String user_reg_name;
        
	private List<Integer> listSupplierId;
	
	private List<UserAddressView> lstuserAddress = new ArrayList<UserAddressView>();
	private List<Role> lstRoles = new ArrayList<Role>();
        private String supplier_names;
	
	@Id
	@Column(name="user_id")
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Column(name="user_name")
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
        @Column(name="luser_name")
        public String getLuser_name() {
            return luser_name;
        }

        public void setLuser_name(String luser_name) {
            this.luser_name = luser_name;
        }
        
	@Column(name="user_alias")
	public String getUser_alias() {
		return user_alias;
	}
	public void setUser_alias(String user_alias) {
		this.user_alias = user_alias;
	}
	@Column(name="last_name")
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	@Column(name="first_name")
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	@Column(name="is_anonymous")
	public String getIs_anonymous() {
		return is_anonymous;
	}
	public void setIs_anonymous(String is_anonymous) {
		this.is_anonymous = is_anonymous;
	}
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="password_salt")
	public String getPassword_salt() {
		return password_salt;
	}
	public void setPassword_salt(String password_salt) {
		this.password_salt = password_salt;
	}
	@Column(name="is_lockedout")
	public boolean getIs_lockedout() {
		return is_lockedout;
	}
	public void setIs_lockedout(boolean is_lockedout) {
		this.is_lockedout = is_lockedout;
	}
	@Column(name="is_registered")
	public boolean getIs_registered() {
		return is_registered;
	}
	public void setIs_registered(boolean is_registered) {
		this.is_registered = is_registered;
	}
	@Column(name="created_date")
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	@Column(name="last_act_date")
	public Date getLast_act_date() {
		return last_act_date;
	}
	public void setLast_act_date(Date last_act_date) {
		this.last_act_date = last_act_date;
	}
	@Column(name="last_login_date")
	public Date getLast_login_date() {
		return last_login_date;
	}
	public void setLast_login_date(Date last_login_date) {
		this.last_login_date = last_login_date;
	}
	@Column(name="last_password_changed_date")
	public Date getLast_password_changed_date() {
		return last_password_changed_date;
	}
	public void setLast_password_changed_date(Date last_password_changed_date) {
		this.last_password_changed_date = last_password_changed_date;
	}
	@Column(name="last_locked_date")
	public Date getLast_locked_date() {
		return last_locked_date;
	}
	public void setLast_locked_date(Date last_locked_date) {
		this.last_locked_date = last_locked_date;
	}
	@Column(name="failed_password_count")
	public int getFailed_password_count() {
		return failed_password_count;
	}
	public void setFailed_password_count(int failed_password_count) {
		this.failed_password_count = failed_password_count;
	}
	@Column(name="middle_name")
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	@Column(name="mobile_no")
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	@Column(name="phone_no")
	public String getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	@Column(name="english_name")
	public String getEnglish_name() {
		return english_name;
	}
	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}
	@Column(name="idcard_no")
	public String getIdcard_no() {
		return idcard_no;
	}
	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}
	@Column(name="sex_type")
	public boolean getSex_type() {
		return sex_type;
	}
	public void setSex_type(boolean sex_type) {
		this.sex_type = sex_type;
	}
	@Column(name="registered_date")
	public Date getRegistered_date() {
		return registered_date;
	}
	public void setRegistered_date(Date registered_date) {
		this.registered_date = registered_date;
	}
	@Column(name="is_active")
	public boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
      
	@Column(name="password_expired_date")
	public Date getPassword_expired_date() {
		return password_expired_date;
	}
	public void setPassword_expired_date(Date password_expired_date) {
		this.password_expired_date = password_expired_date;
	}
      
	@javax.persistence.Transient
	@Column(name="role_id")
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
        
        @javax.persistence.Transient
        public List<Integer> getListSupplierId() {
            return listSupplierId;
        }

        public void setListSupplierId(List<Integer> listSupplierId) {
            this.listSupplierId = listSupplierId;
        }
        
	@javax.persistence.Transient
	public List<UserAddressView> getLstuserAddress() {
		return lstuserAddress;
	}
	public void setLstuserAddress(List<UserAddressView> lstuserAddress) {
		this.lstuserAddress = lstuserAddress;
	}
	@javax.persistence.Transient
	public List<Role> getLstRoles() {
		return lstRoles;
	}
	public void setLstRoles(List<Role> lstRoles) {
		this.lstRoles = lstRoles;
	}

      
        @Column(name="user_reg_num")
        public String getUser_reg_num() {
            return user_reg_num;
        }

        public void setUser_reg_num(String user_reg_num) {
            this.user_reg_num = user_reg_num;
        }

        @Column(name="user_reg_name")
        public String getUser_reg_name() {
            return user_reg_name;
        }

        public void setUser_reg_name(String user_reg_name) {
            this.user_reg_name = user_reg_name;
        }

        @javax.persistence.Transient
        public String getSupplier_names() {
            return supplier_names;
        }

        public void setSupplier_names(String supplier_names) {
            this.supplier_names = supplier_names;
        }

        @Column(name="birth_date")
        public Date getBirth_date() {
            return birth_date;
        }

        public void setBirth_date(Date birth_date) {
            this.birth_date = birth_date;
        }
        
        
        
        
        
}
