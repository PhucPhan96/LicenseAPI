package nfc.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_suppliers")
public class Supplier {
    private int suppl_id;
    private String supplier_name;
    private String short_name;
    private String official_name;
    private String phone_no;
    private String mobile_no;
    private String address;
    private String city;
    private String region;
    private String zip_code;
    private String country;
    private String order_phone_no;
    private String fax_no;
    private String busi_intro;
    private String sales_info;
    private String app_id;
    private String origin_food;
    private String supplier_reg_number;
    private String supplier_reg_name;
    
    @Id
    @Column(name="suppl_id")
    @GeneratedValue
    public int getSuppl_id() {
            return suppl_id;
    }
    
    public void setSuppl_id(int suppl_id) {
            this.suppl_id = suppl_id;
    }
    
    @Column(name="supplier_name")
    public String getSupplier_name() {
            return supplier_name;
    }
    
    public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
    }
    
    @Column(name="short_name")
    public String getShort_name() {
            return short_name;
    }
    
    public void setShort_name(String short_name) {
            this.short_name = short_name;
    }
    
    @Column(name="official_name")
    public String getOfficial_name() {
            return official_name;
    }
    
    public void setOfficial_name(String official_name) {
            this.official_name = official_name;
    }
    
    @Column(name="phone_no")
    public String getPhone_no() {
            return phone_no;
    }
    
    public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
    }
    
    @Column(name="mobile_no")
    public String getMobile_no() {
            return mobile_no;
    }
    
    public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
    }
    
    @Column(name="address")
    public String getAddress() {
            return address;
    }
    
    public void setAddress(String address) {
            this.address = address;
    }
    
    @Column(name="city")
    public String getCity() {
            return city;
    }
    
    public void setCity(String city) {
            this.city = city;
    }
    
    @Column(name="region")
    public String getRegion() {
            return region;
    }
    
    public void setRegion(String region) {
            this.region = region;
    }
    
    @Column(name="zip_code")
    public String getZip_code() {
            return zip_code;
    }
    
    public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
    }
    
    @Column(name="country")
    public String getCountry() {
            return country;
    }
    
    public void setCountry(String country) {
            this.country = country;
    }
    
    @Column(name="order_phone_no")
    public String getOrder_phone_no() {
            return order_phone_no;
    }
    
    public void setOrder_phone_no(String order_phone_no) {
            this.order_phone_no = order_phone_no;
    }
    
    @Column(name="fax_no")
    public String getFax_no() {
            return fax_no;
    }
    
    public void setFax_no(String fax_no) {
            this.fax_no = fax_no;
    }
    
    @Column(name="busi_intro")
    public String getBusi_intro() {
            return busi_intro;
    }
    
    public void setBusi_intro(String busi_intro) {
            this.busi_intro = busi_intro;
    }
    
    @Column(name="sales_info")
    public String getSales_info() {
            return sales_info;
    }
    
    public void setSales_info(String sales_info) {
            this.sales_info = sales_info;
    }
    
    @Column(name="app_id")
    public String getApp_id() {
            return app_id;
    }
    
    public void setApp_id(String app_id) {
            this.app_id = app_id;
    }
        
    @Column(name="origin_food")
    public String getOrigin_food() {
        return origin_food;
    }

    public void setOrigin_food(String origin_food) {
        this.origin_food = origin_food;
    }

    @Column(name="supplier_reg_number")
    public String getSupplier_reg_number() {
        return supplier_reg_number;
    }

    public void setSupplier_reg_number(String supplier_reg_number) {
        this.supplier_reg_number = supplier_reg_number;
    }

    @Column(name="supplier_reg_name")
    public String getSupplier_reg_name() {
        return supplier_reg_name;
    }

    public void setSupplier_reg_name(String supplier_reg_name) {
        this.supplier_reg_name = supplier_reg_name;
    }
        
    
        

}
