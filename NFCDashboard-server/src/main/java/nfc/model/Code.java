package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import nfc.model.PKModel.CodePK;

@Entity
@Table(name="fg_codes")
@IdClass(CodePK.class)
public class Code {
	@Id
	private String group_code;
	@Id
	private String sub_code;
	private String code_name;
	private String code_desc;
	private String code_opt1;
	private String code_opt2;
	private String code_opt3;
	private Byte code_use;
	@Column(name="group_code")
	public String getGroup_code() {
		return group_code;
	}
	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}
	@Column(name="sub_code")
	public String getSub_code() {
		return sub_code;
	}
	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}
	@Column(name="code_name")
	public String getCode_name() {
		return code_name;
	}
	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}
	@Column(name="code_desc")
	public String getCode_desc() {
		return code_desc;
	}
	public void setCode_desc(String code_desc) {
		this.code_desc = code_desc;
	}
	@Column(name="code_opt1")
	public String getCode_opt1() {
		return code_opt1;
	}
	public void setCode_opt1(String code_opt1) {
		this.code_opt1 = code_opt1;
	}
	@Column(name="code_opt2")
	public String getCode_opt2() {
		return code_opt2;
	}
	public void setCode_opt2(String code_opt2) {
		this.code_opt2 = code_opt2;
	}
	@Column(name="code_opt3")
	public String getCode_opt3() {
		return code_opt3;
	}
	public void setCode_opt3(String code_opt3) {
		this.code_opt3 = code_opt3;
	}
	@Column(name="code_use", nullable=true)
	public Byte getCode_use() {
		return code_use;
	}
	public void setCode_use(Byte code_use) {
		this.code_use = code_use;
	}
}
