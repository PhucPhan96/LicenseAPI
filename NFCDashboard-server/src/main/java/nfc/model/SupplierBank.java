/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name="fg_supplier_bank")
public class SupplierBank {
    
    private long id;
    private String bank_account_no;
    private String bank_holder_name;
    private String bank_swift_code;
    private String address;
    private int suppl_id;
    private Boolean is_default;

    @Id
    @Column(name="id")
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="bank_account_no")
    public String getBank_account_no() {
        return bank_account_no;
    }

    public void setBank_account_no(String bank_account_no) {
        this.bank_account_no = bank_account_no;
    }

    @Column(name="bank_holder_name")
    public String getBank_holder_name() {
        return bank_holder_name;
    }

    public void setBank_holder_name(String bank_holder_name) {
        this.bank_holder_name = bank_holder_name;
    }

    @Column(name="bank_swift_code")
    public String getBank_swift_code() {
        return bank_swift_code;
    }

    public void setBank_swift_code(String bank_swift_code) {
        this.bank_swift_code = bank_swift_code;
    }

    @Column(name="address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name="suppl_id")
    public int getSuppl_id() {
        return suppl_id;
    }

    public void setSuppl_id(int suppl_id) {
        this.suppl_id = suppl_id;
    }

    @Column(name="is_default")
    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }
    
    
}
