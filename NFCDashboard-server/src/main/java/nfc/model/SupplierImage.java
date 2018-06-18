package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import nfc.model.PKModel.SupplierImagePK;

@Entity
@Table(name="fg_supplier_imgs")
@IdClass(SupplierImagePK.class)
public class SupplierImage {
	@Id
	private int suppl_id; 
	@Id
	private int img_id; 
	private String img_name;
	private String img_type;
//	private String img_path;
	@Column(name="suppl_id")
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	@Column(name="img_id")
	public int getImg_id() {
		return img_id;
	}
	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}
	@Column(name="img_name")
	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}
	@Column(name="img_type")
	public String getImg_type() {
		return img_type;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
//	@Column(name="img_path")
//	public String img_path() {
//		return img_path;
//	}
//	public void setIimg_path(String img_path) {
//		this.img_path = img_path;
//	}
}
