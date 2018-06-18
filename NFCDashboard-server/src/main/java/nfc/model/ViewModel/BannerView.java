/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import nfc.model.AttachFile;
import nfc.model.Banner;

/**
 *
 * @author Admin
 */
public class BannerView {
    private Banner banner;
    private AttachFile file;

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public AttachFile getFile() {
        return file;
    }

    public void setFile(AttachFile file) {
        this.file = file;
    }
}
