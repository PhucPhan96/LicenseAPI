/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nfc.model.AttachFile;

/**
 *
 * @author Admin
 */
public class ThreadSupplierUser {
private String thread_id;
    private String app_id;
    private String writer_id;
    private String parent_thread_id;
    private String thd_subject;
    private String thd_contents;
    private int board_id;
    private Date write_date;
    private Integer review_rank5;
    private List<AttachFile> attachFile = new ArrayList<AttachFile>();
    private List<Thread> childThreads = new ArrayList<>();
    private String name;
    private int suppl_id;
    private String supplier_name;
    private String user_id;
    private String user_name;
    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public String getParent_thread_id() {
        return parent_thread_id;
    }

    public void setParent_thread_id(String parent_thread_id) {
        this.parent_thread_id = parent_thread_id;
    }

    public String getThd_subject() {
        return thd_subject;
    }

    public void setThd_subject(String thd_subject) {
        this.thd_subject = thd_subject;
    }

    public String getThd_contents() {
        return thd_contents;
    }

    public void setThd_contents(String thd_contents) {
        this.thd_contents = thd_contents;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public Date getWrite_date() {
        return write_date;
    }

    public void setWrite_date(Date write_date) {
        this.write_date = write_date;
    }

    public Integer getReview_rank5() {
        return review_rank5;
    }

    public void setReview_rank5(Integer review_rank5) {
        this.review_rank5 = review_rank5;
    }
    @javax.persistence.Transient
    public List<AttachFile> getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(List<AttachFile> attachFile) {
        this.attachFile = attachFile;
    }
    @javax.persistence.Transient
    public List<Thread> getChildThreads() {
        return childThreads;
    }

    public void setChildThreads(List<Thread> childThreads) {
        this.childThreads = childThreads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSuppl_id() {
        return suppl_id;
    }

    public void setSuppl_id(int suppl_id) {
        this.suppl_id = suppl_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    
    
}
