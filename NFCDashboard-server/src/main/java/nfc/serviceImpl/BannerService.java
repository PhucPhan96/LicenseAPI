/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import nfc.model.AttachFile;
import nfc.model.Banner;
import nfc.model.Banner;
import nfc.model.ViewModel.BannerView;
import nfc.service.IBannerService;
import nfc.service.IFileService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class BannerService implements IBannerService {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private IFileService fileDAO;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Banner fGetBannerByID(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Banner.class);
        criteria.add(Restrictions.eq("banner_id", id));
        Banner banner = (Banner) criteria.uniqueResult();
        trans.commit();
        return banner;
    }

    public List<Banner> fGetBannerByType(String type) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Banner.class);
        criteria.add(Restrictions.eq("banner_type", type));
        List<Banner> lstBanner = (List<Banner>) criteria.list();
        trans.commit();
        return lstBanner;
    }

    public List<Banner> fGetListBanner() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Banner.class);
        List<Banner> lstBanner = (List<Banner>) criteria.list();
        trans.commit();
        return lstBanner;
    }

    public List<Banner> fGetListBannerGroupBy() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Banner.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.groupProperty("banner_type"));
        criteria.setProjection(projectionList);
        List<Banner> lstBanner = (List<Banner>) criteria.list();
        trans.commit();
        return lstBanner;
    }

    public List<BannerView> fGetBannerViewByType(String type) {
        List<BannerView> lstBannerView = new ArrayList<BannerView>();
        List<Banner> lstBanner = fGetBannerByType(type);
        try {
            for (Banner banner : lstBanner) {
                BannerView bannerView = new BannerView();
                bannerView.setBanner(banner);
                AttachFile attachFile = new AttachFile();
                attachFile = fileDAO.getAttachFile(banner.getFile_id());
                bannerView.setFile(attachFile);
                lstBannerView.add(bannerView);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lstBannerView;
    }

    public BannerView fGetBannerViewById(int id) {
        BannerView bannerView = new BannerView();
        Banner banner = fGetBannerByID(id);
        bannerView.setBanner(banner);
        bannerView.setFile(fileDAO.getAttachFile(banner.getFile_id()));
        return bannerView;
    }

    public boolean insertBanner(BannerView bannerView) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.save(bannerView.getBanner());
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    private void insertBanner1(Session session, BannerView bannerView) {
        //Transaction trans = session.beginTransaction();
        session.save(bannerView.getBanner());
    }

    public boolean updateBanner(BannerView bannerView) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            BannerView bannerViewTemp = new BannerView();
            bannerViewTemp = bannerView;
            int file_ID = bannerView.getBanner().getFile_id();
            deleteReferenceOfBanner1(session, bannerView.getBanner().getBanner_id());
//            deleteReferenceOfBanner(session, file_ID);
            insertBanner1(session, bannerViewTemp);
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    private void deleteReferenceOfBanner(Session session, int id) {
        String deleteQuery = "delete from fg_files where file_id = " + id;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }
    private void deleteReferenceOfBanner1(Session session, int id) {
        String deleteQuery = "delete from fg_banner where banner_id = " + id;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public boolean deleteBanner(int banner_id, int file_id) {
        System.out.println("Vao Delete:");
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            System.out.println(banner_id);
            System.out.println(file_id);
            String deleteQuery = "delete from fg_banner where banner_id = " + banner_id;
            Query query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
            deleteReferenceOfBanner(session, file_id);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
}
