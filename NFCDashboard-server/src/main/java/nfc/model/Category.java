package nfc.model;
 import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
 import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

 @Entity
 @Table(name="fg_categories")
public class Category{
    private int cate_id;
    private String app_id;
    private Integer cate_img_id;
    private String cate_name;
    private int parent_cate_id;
    private Date created_date;
    private int cate_seq;
    private String cate_type;
    private Integer suppl_id;
    private Boolean is_single;
    
    @Id
    @Column(name="cate_id")
    @GeneratedValue
    public int getCate_id() {
            return cate_id;
    }
    public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
    }
    @Column(name="app_id")
    public String getApp_id() {
            return app_id;
    }
    public void setApp_id(String app_id) {
            this.app_id = app_id;
    }
    @Column(name="cate_img_id", nullable = true )
    public Integer getCate_img_id() {
            return cate_img_id;
    }
    public void setCate_img_id(Integer cate_img_id) {
        this.cate_img_id = cate_img_id;
    }
    @Column(name="cate_name")
    public String getCate_name() {
            return cate_name;
    }
    public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
    }
    @Column(name="parent_cate_id")
    public int getParent_cate_id() {
            return parent_cate_id;
    }
    public void setParent_cate_id(int parent_cate_id) {
            this.parent_cate_id = parent_cate_id;
    }
    @Column(name="created_date")
    public Date getCreated_date() {
            return created_date;
    }
    public void setCreated_date(Date created_date) {
            this.created_date = created_date;
    }
    @Column(name="cate_seq")
    public int getCate_seq() {
            return cate_seq;
    }
    public void setCate_seq(int cate_seq) {
            this.cate_seq = cate_seq;
    }
    @Column(name="cate_type")
    public String getCate_type() {
            return cate_type;
    }
    public void setCate_type(String cate_type) {
            this.cate_type = cate_type;
    }

    @Column(name="suppl_id")
    public Integer getSuppl_id() {
        return suppl_id;
    }

    public void setSuppl_id(Integer suppl_id) {
        this.suppl_id = suppl_id;
    }

    @Column(name="is_single")
    public Boolean getIs_single() {
        return is_single;
    }

    public void setIs_single(Boolean is_single) {
        this.is_single = is_single;
    }

}
