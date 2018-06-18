package nfc.model.ViewModel;

import nfc.model.AttachFile;

public class SupplierAttachFileView {
	private AttachFile attachFile;
	private String imageType;
	public AttachFile getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(AttachFile attachFile) {
		this.attachFile = attachFile;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
}
