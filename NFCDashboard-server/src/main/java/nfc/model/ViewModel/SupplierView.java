package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nfc.model.Address;
import nfc.model.AttachFile;
import nfc.model.Category;
import nfc.model.Code;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.model.SupplierAddress;
import nfc.model.SupplierBank;
import nfc.model.SupplierWork;
import nfc.model.User;

public class SupplierView {
    
    private Supplier supplier = new Supplier();
    private SupplierWork supplierWork = new SupplierWork();
    private List<SupplierAttachFileView> images = new ArrayList<SupplierAttachFileView>();
    private List<Category> categories = new ArrayList<Category>();
    private List<SupplierAddressView> lstSupplAddressView = new ArrayList<SupplierAddressView>();
    private User director = new User();
    private List<SupplierAttachFileView> lstAttachFileView;
    private Code code = new Code();
    private List<SupplierBank> supplierBanks;
    
    //	private String img_path;
    public User getDirector() {
            return director;
    }
    public void setDirector(User director) {
            this.director = director;
    }
    /*private List<Role> roles;*/
    //private Supplier supplierManage;
    public Supplier getSupplier() {
            return supplier;
    }
    public void setSupplier(Supplier supplier) {
            this.supplier = supplier;
    }
    public SupplierWork getSupplierWork() {
            return supplierWork;
    }
    public void setSupplierWork(SupplierWork supplierWork) {
            this.supplierWork = supplierWork;
    }
    public List<SupplierAttachFileView> getImages() {
            return images;
    }
    public void setImages(List<SupplierAttachFileView> images) {
            this.images = images;
    }
    public List<Category> getCategories() {
            return categories;
    }
    public void setCategories(List<Category> categories) {
            this.categories = categories;
    }
    public List<SupplierAddressView> getLstSupplAddressView() {
            return lstSupplAddressView;
    }
    public void setLstSupplAddressView(List<SupplierAddressView> lstSupplAddressView) {
            this.lstSupplAddressView = lstSupplAddressView;
    }

//	public String getImagePath() {
//		return img_path;
//	}
//	public void setImagePath(String img_path) {
//		this.img_path = img_path;
//	}
    /*public Supplier getSupplierManage() {
            return supplierManage;
    }
    public void setSupplierManage(Supplier supplierManage) {
            this.supplierManage = supplierManage;
    }*/
    public List<SupplierAttachFileView> getLstAttachFileView() {
            return lstAttachFileView;
    }
    public void setLstAttachFileView(List<SupplierAttachFileView> lstAttachFileView) {
            this.lstAttachFileView = lstAttachFileView;
    }
    public Code getCode() {
            return code;
    }
    public void setCode(Code code) {
            this.code = code;
    }

    public List<SupplierBank> getSupplierBanks() {
        return supplierBanks;
    }

    public void setSupplierBanks(List<SupplierBank> supplierBanks) {
        this.supplierBanks = supplierBanks;
    }
        
         
}
