package nfc.model.ViewModel;

import nfc.model.AttachFile;
import nfc.model.Category;

public class CategoryView {
	private Category category;
	private AttachFile attachFile;
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public AttachFile getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(AttachFile attachFile) {
		this.attachFile = attachFile;
	}
}
