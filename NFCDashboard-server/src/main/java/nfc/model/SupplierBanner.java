/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import nfc.model.PKModel.SupplierBannerPK;

/**
 *
 * @author Admin
 */
@Entity
@Table(name="fg_supplier_imgs")
@IdClass(SupplierBannerPK.class)
public class SupplierBanner {
    
    @Id
    private int suppl_id; 
    @Id
    private int img_id; 
    private Boolean is_load;
    
    @Column(name="suppl_id")
    public int getSuppl_id() {
        return suppl_id;
    }

    public void setSuppl_id(int suppl_id) {
        this.suppl_id = suppl_id;
    }
    
    @Column(name="file_id")
    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
    
    @Column(name="is_load")
    public Boolean getIs_load() {
        return is_load;
    }

    public void setIs_load(Boolean is_load) {
        this.is_load = is_load;
    }
    
}
