package nfc.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="fg_products")
public class Product {
    private int prod_id;
    private String app_id;
    private String prod_name;
    private int cate_id;
    private String prod_desc;
    private int suppl_id;
    private String qty_per_unit;
    private BigDecimal unit_price;
    private String prod_origins;
    private int min_order_qty;
//    private Boolean is_used;
    private String calculatedDate;
    /*private Category category;
    @ManyToOne
    @JoinColumn(name="cate_id")
    @OneToMany
    @JoinTable(name="fg_product_categories",
    joinColumns={@JoinColumn(name = "prod_id")},inverseJoinColumns={@JoinColumn(name = "cate_id")})
    public Category getCategory() {
            return category;
    }

    public void setCategory(Category category) {
            this.category = category;
    }*/

    public String getCalculatedDate() {
        return calculatedDate;
    }

    public void setCalculatedDate(String calculatedDate) {
        this.calculatedDate = calculatedDate;
    }

    /*private Set<AttachFile> attachFiles = new HashSet<AttachFile>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="fg_prod_imgs",joinColumns={@JoinColumn(name = "prod_id")},inverseJoinColumns={@JoinColumn(name = "img_id")})
    public Set<AttachFile> getAttachFiles() {
            return attachFiles;
    }
    public void setAttachFiles(Set<AttachFile> attachFiles) {
            this.attachFiles = attachFiles;
    }*/
    @Id
    @Column(name="prod_id")
    @GeneratedValue
    public int getProd_id() {
            return prod_id;
    }
    
    public void setProd_id(int prod_id) {
            this.prod_id = prod_id;
    }
    
    @Column(name="app_id")
    public String getApp_id() {
            return app_id;
    }
    
    public void setApp_id(String app_id) {
            this.app_id = app_id;
    }
    
    @Column(name="prod_name")
    public String getProd_name() {
            return prod_name;
    }
    
    public void setProd_name(String prod_name) {
            this.prod_name = prod_name;
    }
    
    @Column(name="cate_id")
    public int getCate_id() {
            return cate_id;
    }
    
    public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
    }
    
    @Column(name="prod_desc")
    public String getProd_desc() {
            return prod_desc;
    }
    
    public void setProd_desc(String prod_desc) {
            this.prod_desc = prod_desc;
    }
    
    @Column(name="suppl_id")
    public int getSuppl_id() {
            return suppl_id;
    }
    
    public void setSuppl_id(int suppl_id) {
            this.suppl_id = suppl_id;
    }
    
    @Column(name="qty_per_unit")
    public String getQty_per_unit() {
            return qty_per_unit;
    }
    
    public void setQty_per_unit(String qty_per_unit) {

            this.qty_per_unit = qty_per_unit;
    }
    
    @Column(name="unit_price")
    public BigDecimal getUnit_price() {
            return unit_price;
    }
    
    public void setUnit_price(BigDecimal unit_price) {
            this.unit_price = unit_price;
    }
    
    @Column(name="prod_origins")
    public String getProd_origins() {
            return prod_origins;
    }
    
    public void setProd_origins(String prod_origins) {
            this.prod_origins = prod_origins;
    }
    
    @Column(name="min_order_qty")
    public int getMin_order_qty() {
            return min_order_qty;
    }
    
    public void setMin_order_qty(int min_order_qty) {
            if(Integer.valueOf(min_order_qty) != null)
            {
                    this.min_order_qty = min_order_qty;
            }
            else
            {
                    this.min_order_qty = 0;
            }
    }
    
//    @Column(name="is_used")
//    public Boolean getIs_used() {
//        return is_used;
//    }
//
//    public void setIs_used(Boolean is_used) {
//        this.is_used = is_used;
//    }
        
        
}
