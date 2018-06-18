/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nfc.model.Banner;
import nfc.model.ViewModel.BannerView;
import nfc.service.IBannerService;
import nfc.serviceImpl.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class BannerManagementController {

    @Autowired
    private IBannerService bannerDAO;

    @RequestMapping(value = "app/banner/getAll", method = RequestMethod.GET)
    public List<Banner> fGetListBanner() {
        List<Banner> lstBanner = bannerDAO.fGetListBannerGroupBy();
        return lstBanner;
    }

    @RequestMapping(value = "app/banner/getbannerbyid/{id}", method = RequestMethod.GET)
    public Banner fGetBannerByID(@PathVariable("id") int id) {
        Banner banner = new Banner();
        banner = bannerDAO.fGetBannerByID(id);
        return banner;
    }

    @RequestMapping(value = "app/banner/getbannerbytype/{id}", method = RequestMethod.GET)
    public List<BannerView> fGetListBannerByType(@PathVariable("id") String type) {
        List<BannerView> lstBanner = new ArrayList<BannerView>();
        lstBanner = bannerDAO.fGetBannerViewByType(type);
        return lstBanner;
    }

    @RequestMapping(value = "app/banner/getbannerviewbyid/{id}", method = RequestMethod.GET)
    public BannerView fGetBannerViewById(@PathVariable("id") int id) {
        BannerView banner = new BannerView();
        try {
            banner = bannerDAO.fGetBannerViewById(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return banner;
    }

    @RequestMapping(value = "app/banner/add", method = RequestMethod.POST)
    public @ResponseBody String insertBanner(@RequestBody BannerView banner) {
        String data = bannerDAO.insertBanner(banner) + "";//.insertCategory(cate)+"";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "app/banner/delete/{banner_id}/{file_id}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteBanner(@PathVariable("banner_id") int banner_id, @PathVariable("file_id") int file_id) {
        String data = bannerDAO.deleteBanner(banner_id, file_id) + "";//
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "app/banner/update", method = RequestMethod.PUT)
    public @ResponseBody String editRole(@RequestBody BannerView bannerView) {
        String data = bannerDAO.updateBanner(bannerView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
}
