package nfc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import nfc.model.PKModel.CategorySupplierPK;

@Entity
@Table(name="fg_supplier_categories")
@IdClass(CategorySupplierPK.class)
public class SupplierCategories{
	@Id
	private int cate_id; 
	@Id
	private int suppl_id; 
	private String cate_name;
	@Column(name="cate_id")
	public int getCate_id() {
		return cate_id;
	}
	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}
	@Column(name="suppl_id")
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	@Column(name="cate_name")
	public String getCate_name() {
		return cate_name;
	}
	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}
}
