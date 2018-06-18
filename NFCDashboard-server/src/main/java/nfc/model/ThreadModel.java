package nfc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_threads")
public class ThreadModel {
	private String thread_id;
	private String app_id;
	private String writer_id;
	private String parent_thread_id;
	private String thd_subject;
	private String thd_contents;
	private int board_id;
	private Date write_date;
	private int review_rank5;
	@Id
	@Column(name="thread_id")
	public String getThread_id() {
		return thread_id;
	}
	public void setThread_id(String thread_id) {
		this.thread_id = thread_id;
	}
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Column(name="writer_id")
	public String getWriter_id() {
		return writer_id;
	}
	public void setWriter_id(String writer_id) {
		this.writer_id = writer_id;
	}
	@Column(name="parent_thread_id",nullable=true)
	public String getParent_thread_id() {
		return parent_thread_id;
	}
	public void setParent_thread_id(String parent_thread_id) {
		this.parent_thread_id = parent_thread_id;
	}
	@Column(name="thd_subject")
	public String getThd_subject() {
		return thd_subject;
	}
	public void setThd_subject(String thd_subject) {
		this.thd_subject = thd_subject;
	}
	@Column(name="thd_contents")
	public String getThd_contents() {
		return thd_contents;
	}
	public void setThd_contents(String thd_contents) {
		this.thd_contents = thd_contents;
	}
	@Column(name="board_id")
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}
	@Column(name="write_date")
	public Date getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Date write_date) {
		this.write_date = write_date;
	}
	@Column(name="review_rank5")
	public int getReview_rank5() {
		return review_rank5;
	}
	public void setReview_rank5(int review_rank5) {
		this.review_rank5 = review_rank5;
	}
}
