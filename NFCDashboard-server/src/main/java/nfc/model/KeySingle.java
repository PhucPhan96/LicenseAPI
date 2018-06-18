/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author PhucP
 */
@Entity
@Table(name="keysingle")
public class KeySingle {
    private Integer id; 
    private String key_single; //longtext 
    private String ex_main;  //longtext 
    private String ex_cpu; //longtext 
    private Date start_date; //date 
    private Date end_date; 
    private Integer key_log;
    private String status_key;

    
    @Id
    @Column(name="id")
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="key_single")
    public String getKey_single() {
        return key_single;
    }

    public void setKey_single(String key_single) {
        this.key_single = key_single;
    }

    @Column(name="ex_main")
    public String getEx_main() {
        return ex_main;
    }

    public void setEx_main(String ex_main) {
        this.ex_main = ex_main;
    }

    @Column(name="ex_cpu")
    public String getEx_cpu() {
        return ex_cpu;
    }

    public void setEx_cpu(String ex_cpu) {
        this.ex_cpu = ex_cpu;
    }

    @Column(name="start_date")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Column(name="end_date")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    @Column(name="key_log")
    public Integer getKey_log() {
        return key_log;
    }

    public void setKey_log(Integer key_log) {
        this.key_log = key_log;
    }

    @Column(name="status_key")
    public String getStatus_key() {
        return status_key;
    }

    public void setStatus_key(String status_key) {
        this.status_key = status_key;
    }
    
    
}
