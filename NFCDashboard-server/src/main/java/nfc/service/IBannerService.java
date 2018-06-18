/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;
import java.util.List;
import nfc.model.Banner;
import nfc.model.ViewModel.BannerView;
/**
 *
 * @author Admin
 */
public interface IBannerService {
    List<Banner> fGetListBanner();
    Banner fGetBannerByID(int id);
    List<BannerView> fGetBannerViewByType(String type);
    List<Banner> fGetListBannerGroupBy();
    public BannerView fGetBannerViewById(int id);
    boolean insertBanner(BannerView bannerView);
    public boolean updateBanner(BannerView bannerView);
    public boolean deleteBanner(int banner_id, int file_id);
}
