package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_prod_addition")
public class ProductAdd {
	private int add_id; 
	private int prod_id; 
	private String add_name; 
	private int add_price; 
	private Byte price_type;
        private String add_desc;
	@Id
	@Column(name="add_id")
	@GeneratedValue
	public int getAdd_id() {
		return add_id;
	}
	public void setAdd_id(int add_id) {
		this.add_id = add_id;
	}
	@Column(name="prod_id")
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	@Column(name="add_name")
	public String getAdd_name() {
		return add_name;
	}
	public void setAdd_name(String add_name) {
		this.add_name = add_name;
	}
	@Column(name="add_price")
	public int getAdd_price() {
		return add_price;
	}
	public void setAdd_price(int add_price) {
		this.add_price = add_price;
	}
	@Column(name="price_type",nullable=true)
	public Byte getPrice_type() {
		return price_type;
	}
	public void setPrice_type(Byte price_type) {
		this.price_type = price_type;
	}

        @Column(name="add_desc")
    public String getAdd_desc() {
        return add_desc;
    }

    public void setAdd_desc(String add_desc) {
        this.add_desc = add_desc;
    }
	
        
}
