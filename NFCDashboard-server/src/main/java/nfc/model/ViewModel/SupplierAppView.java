package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nfc.model.AttachFile;
import nfc.model.Supplier;
import nfc.model.SupplierWork;

public class SupplierAppView {

    private Supplier supplier = new Supplier();
    private SupplierWork supplierWork = new SupplierWork();
    private List<SupplierAttachFileView> images = new ArrayList<SupplierAttachFileView>();
    private int reviewCount;

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

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

}
