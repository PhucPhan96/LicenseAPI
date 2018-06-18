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
@Table(name="key_trial")
public class KeyTrial {
    private Integer id;
    private String main; 
    private String cpu;
    private Date start_date;
    private Date end_date;
    private Integer expired_time;

    @Id
    @Column(name="id")
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="main")
    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    @Column(name="cpu")
    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    @Column(name="start_date")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    public Date getStart_date() {
        System.out.println(start_date);
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

    @Column(name="expired_time")
    public Integer getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Integer expired_time) {
        this.expired_time = expired_time;
    }
    
}
