/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

import java.util.Date;
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
@Table(name = "fg_texts")
public class Text {

    private int text_id;
    private String text_content;
    private Date text_createdDate;
    private String text_createdUser;
    private String text_title;
    private String text_type;
    private Date text_updatedDate;
    
    private String code_name;
    private String user_name;

    @Id
    @Column(name = "text_id")
    public int getText_id() {
        return text_id;
    }

    public void setText_id(int text_id) {
        this.text_id = text_id;
    }

    @Column(name = "text_content")
    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    @Column(name = "text_createdDate")
    public Date getText_createdDate() {
        return text_createdDate;
    }

    public void setText_createdDate(Date text_createdDate) {
        this.text_createdDate = text_createdDate;
    }

    @Column(name = "text_createdUser")
    public String getText_createdUser() {
        return text_createdUser;
    }

    public void setText_createdUser(String text_createdUser) {
        this.text_createdUser = text_createdUser;
    }

    @Column(name = "text_title")
    public String getText_title() {
        return text_title;
    }

    public void setText_title(String text_title) {
        this.text_title = text_title;
    }

    @Column(name = "text_type")
    public String getText_type() {
        return text_type;
    }

    public void setText_type(String text_type) {
        this.text_type = text_type;
    }

    @Column(name = "text_updatedDate")
    public Date getText_updatedDate() {
        return text_updatedDate;
    }

    public void setText_updatedDate(Date text_updatedDate) {
        this.text_updatedDate = text_updatedDate;
    }

    @javax.persistence.Transient
    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    @javax.persistence.Transient
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    
    
    
    

}
