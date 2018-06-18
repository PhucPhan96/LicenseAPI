package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import nfc.model.PKModel.ThreadImgPK;
@Entity
@Table(name="fg_thread_imgs")
@IdClass(ThreadImgPK.class)
public class ThreadImg 
{
	@Id
	private String thread_id;
	@Id
	private int img_id;
        
	@Column(name="thread_id")
	public String getThread_id() {
		return thread_id;
	}
	public void setThread_id(String thread_id) {
		this.thread_id = thread_id;
	}	
	@Column(name="img_id")
	public int getImg_id() {
		return img_id;
	}
	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}
}
