package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import nfc.model.PKModel.ProductOptionalPK;

@Entity
@Table(name="fg_prod_optional")
@IdClass(ProductOptionalPK.class)
public class ProductOptional {
	@Id
	private int prod_id;
	@Id
	private int option_prod_id;
	private boolean is_optional;
	@Column(name="prod_id")
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	@Column(name="option_prod_id")
	public int getOption_prod_id() {
		return option_prod_id;
	}
	public void setOption_prod_id(int option_prod_id) {
		this.option_prod_id = option_prod_id;
	}
	@Column(name="is_optional")
	public boolean isIs_optional() {
		return is_optional;
	}
	public void setIs_optional(boolean is_optional) {
		this.is_optional = is_optional;
	}
}
