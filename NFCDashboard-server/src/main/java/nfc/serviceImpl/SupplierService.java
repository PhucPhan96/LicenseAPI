package nfc.serviceImpl;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import nfc.model.Address;
import nfc.model.AttachFile;
import nfc.model.Board;
import nfc.model.Category;
import nfc.model.Code;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.Product;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.model.SupplierFavorite;
import nfc.model.SupplierAddress;
import nfc.model.SupplierCategories;
import nfc.model.SupplierImage;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.User;
import nfc.model.ViewModel.BillHistory;
import nfc.model.ViewModel.ProductAttachFileView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.SupplierAppView;
import nfc.model.ViewModel.SupplierView;
import nfc.service.IBoardService;
import nfc.service.ICategoryService;
import nfc.service.ICodeService;
import nfc.service.IFileService;
import nfc.service.IOrderService;
import nfc.service.IRoleService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import nfc.model.ProductOptional;
import nfc.model.SupplierBank;
import nfc.model.ViewModel.BillHistoryView;
import nfc.model.ViewModel.BillSupplierInformation;
import nfc.model.ViewModel.ProductOptionalBH;

import nfc.model.ViewModel.SupplierAttachFileView;
import nfc.model.ViewModel.UserSupplierView;
import org.hibernate.transform.Transformers;
import nfc.service.IMailService;

public class SupplierService implements ISupplierService {

    /*
	 * @Autowired private ICommonService commonDAO;
     */
    @Autowired
    private IUserService userDAO;
    @Autowired
    private ICodeService codeDAO;
    @Autowired
    private IFileService fileDAO;
    @Autowired
    private IOrderService orderDAO;
    @Autowired
    private ICategoryService categoryDAO;
    @Autowired
    private IBoardService boardDAO;
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Supplier> getListSupplier() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Supplier.class);
        List<Supplier> list = (List<Supplier>) criteria.list();
        trans.commit();
        return list;
    }

    public Supplier getSupplier(String supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Supplier supplier = null;
        try{
            Criteria criteria = session.createCriteria(Supplier.class);
            criteria.add(Restrictions.eq("suppl_id", Integer.parseInt(supplId)));
            supplier = (Supplier) criteria.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return supplier;
    }

    public SupplierView getSupplierView(int supplId) {
        SupplierView supplierView = new SupplierView();
        supplierView.setSupplier(getSupplier(supplId + ""));
        supplierView.setSupplierWork(getSupplierWork(supplId));
        // get image of supplier
        List<SupplierImage> supplImgs = getListSupplierImage(supplId);
        List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
        for (SupplierImage supImg : supplImgs) {
            SupplierAttachFileView supplierAttachView = new SupplierAttachFileView();
            supplierAttachView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
            supplierAttachView.setImageType(supImg.getImg_type());
            supplAttachFiles.add(supplierAttachView);
        }
        supplierView.setImages(supplAttachFiles);
        // get categories of supplier
        List<SupplierCategories> supplCates = getListSupplierCategory(supplId);
        List<Category> supplCategories = new ArrayList<Category>();
        for (SupplierCategories supCate : supplCates) {
            supplCategories.add(categoryDAO.getCategory(supCate.getCate_id() + ""));
        }
        supplierView.setCategories(supplCategories);
        // get address of supplier
        List<SupplierAddress> supplAddresses = getListSupplierAddress(supplId);
        List<SupplierAddressView> supplAddressViewLst = new ArrayList<SupplierAddressView>();
        for (SupplierAddress supplAddr : supplAddresses) {
            SupplierAddressView suppAddrView = new SupplierAddressView();
            suppAddrView.setAddressOfSuppl(getAddress(supplAddr.getAddr_id()));
            suppAddrView.setIs_deliver(supplAddr.getIs_deliver());
            suppAddrView.setIs_main(supplAddr.getIs_main());
            supplAddressViewLst.add(suppAddrView);
        }
        supplierView.setLstSupplAddressView(supplAddressViewLst);
        supplierView.setSupplierBanks(getListSupplierBank(supplId));
        // get supplier manager
        // supplierView.setSupplierManage(getSupplier(supplId + ""));
        return supplierView;
    }
    //Lucas

    public SupplierView getSupplierView1(int supplId) {
        SupplierView supplierView = new SupplierView();
        SupplierWork supplierWork = new SupplierWork();
        supplierWork = getSupplierWork(supplId);
        User user = new User();
        user = getDirectorSuplier(supplierWork.getManage_suppl_id());
        supplierView.setDirector(user);

        supplierView.setSupplier(getSupplier(supplId + ""));
        supplierView.setSupplierWork(getSupplierWork(supplId));
        // get image of supplier
        List<SupplierImage> supplImgs = getListSupplierImage(supplId);
        List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
        for (SupplierImage supImg : supplImgs) {
            SupplierAttachFileView supplierAttachView = new SupplierAttachFileView();
            supplierAttachView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
            supplierAttachView.setImageType(supImg.getImg_type());
            supplAttachFiles.add(supplierAttachView);
        }
        supplierView.setImages(supplAttachFiles);
        // get categories of supplier
        List<SupplierCategories> supplCates = getListSupplierCategory(supplId);
        List<Category> supplCategories = new ArrayList<Category>();
        for (SupplierCategories supCate : supplCates) {
            supplCategories.add(categoryDAO.getCategory(supCate.getCate_id() + ""));
        }
        supplierView.setCategories(supplCategories);
        // get address of supplier
        List<SupplierAddress> supplAddresses = getListSupplierAddress(supplId);
        List<SupplierAddressView> supplAddressViewLst = new ArrayList<SupplierAddressView>();
        for (SupplierAddress supplAddr : supplAddresses) {
            SupplierAddressView suppAddrView = new SupplierAddressView();
            suppAddrView.setAddressOfSuppl(getAddress(supplAddr.getAddr_id()));
            suppAddrView.setIs_deliver(supplAddr.getIs_deliver());
            suppAddrView.setIs_main(supplAddr.getIs_main());
            supplAddressViewLst.add(suppAddrView);
        }
        supplierView.setLstSupplAddressView(supplAddressViewLst);
        // get supplier manager
        // supplierView.setSupplierManage(getSupplier(supplId + ""));
        return supplierView;
    }
    //Lucas

    public boolean ChangeOrderPhoneNumberSupplier(String supplierId, String orderPhoneNum) {
        System.out.println("vao dc update");
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            String strQuery = "update fg_suppliers set order_phone_no='" + orderPhoneNum + "' where suppl_id = '" + supplierId + "'";
            System.out.println(strQuery);
            Query query = session.createSQLQuery(strQuery);
            query.executeUpdate();
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public SupplierWork getSupplierWork(int supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        SupplierWork supplierWork = new SupplierWork();
        try {
            Criteria criteria = session.createCriteria(SupplierWork.class);
            criteria.add(Restrictions.eq("suppl_id", supplId));
            supplierWork = (SupplierWork) criteria.uniqueResult();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();

        }
        return supplierWork;
    }

    /**
     * Lucas - get list of supplierView around 3km by longitude and latitude
     */
    public List<SupplierView> getListSupplierByAddress(String longT, String latT) {
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
//        String sqlQuery = "SELECT * FROM fg_suppliers WHERE address LIKE '%" + address + "%' ";
        String sqlQuery = "SELECT * FROM 82wafoodgo.fg_supplier_work ORDER BY order_count DESC, favorite_count, rank5  limit 5";
        try {
            Query query = session.createSQLQuery(sqlQuery).addEntity(SupplierWork.class);;
            List<SupplierWork> list = (List<SupplierWork>) query.list();
            trans.commit();
            for (SupplierWork supplierWork : list) {
                System.out.println("Vao For ne");
                SupplierWork supplierWorkWithDistance = getSupplierByLongLat(longT == "undefined" ? "0" : longT, latT == "undefined" ? "0" : latT, supplierWork.getSuppl_id() + "");
                String distanceTemp = supplierWorkWithDistance.getDistance_in_km() + "";
                BigDecimal mindistance = new BigDecimal(3);
                if (distanceTemp.equalsIgnoreCase("null")) {
                } else {
                    BigDecimal distance = new BigDecimal(distanceTemp + "");
                    System.out.println(distance);
                    int res = distance.compareTo(mindistance);
                    if (res > 0) {
                        System.out.println("Far");
                    } else {
                        SupplierView supplierView = new SupplierView();
                        supplierView = getSupplierView(supplierWork.getSuppl_id());
                        supplierView.setSupplierWork(supplierWorkWithDistance);
                        lstSupplierView.add(supplierView);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Loi roi");
            System.out.println(ex.getMessage());
            trans.rollback();
        }

        return lstSupplierView;
    }

    /**
     * Lucas - get supplierWork by longitude and latitude. Give distance from my
     * location to store.
     */
    public SupplierWork getSupplierByLongLat(String LongT, String LatT, String supplierID) {
        SupplierWork supplierWork = new SupplierWork();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sqlQuery = "SELECT *, 111.111 * DEGREES(ACOS(COS(RADIANS(" + LatT + ")) * COS(RADIANS(latitude)) * COS(RADIANS(" + LongT + "- longitude)) + SIN(RADIANS(" + LatT + ")) * SIN(RADIANS(latitude)))) AS distance_in_km FROM fg_supplier_work WHERE suppl_id = " + supplierID;
        try {
            Query query = session.createSQLQuery(sqlQuery);
            List<Object[]> rows = query.list();
            SupplierWork str = new SupplierWork();
            for (Object[] row : rows) {
                str.setBoard_id(Integer.parseInt(row[20] + ""));
                str.setCall_order(Boolean.parseBoolean(row[12] + ""));
                str.setDirect_pay(Boolean.parseBoolean(row[13] + ""));
                str.setDistance_in_km(row[28] + "");
                str.setFavorite_count(Integer.parseInt(row[11] + ""));
                str.setIs_active(Boolean.parseBoolean(row[1] + ""));
                str.setIs_online(Boolean.parseBoolean(row[8] + ""));
                str.setLatitude(row[21] + "");
                str.setLongitude(row[22] + "");
                str.setManage_suppl_id(Integer.parseInt(row[17] + ""));
                str.setOrder_count(Integer.parseInt(row[10] + ""));
                str.setOwner_suppl_id(Integer.parseInt(row[16] + ""));
                str.setSat_end_hm(row[5] + "");
                str.setSat_start_hm(row[4] + "");
                str.setSun_end_hm(row[7] + "");
                str.setSun_start_hm(row[6] + "");
                str.setSuppl_id(Integer.parseInt(row[0] + ""));
                str.setSuppl_rank(row[19] + "");
                str.setSuppl_role(row[18] + "");
                str.setVisit_pay(Boolean.parseBoolean(row[14] + ""));
                str.setWd_end_hm(row[3] + "");
                str.setWd_start_hm(row[2] + "");
                str.setDelivery_id(row[27] + "");
                supplierWork = str;
            }
            trans.commit();
        } catch (Exception ex) {
            System.out.println("Loi roi ne");
            System.out.println(ex);
            trans.rollback();
        }
        return supplierWork;
    }

    /**
     * Lucas - get list of supplier by input text from searching bar.
     */
    public List<SupplierView> getListSupplierViewByTextInput(String text) {
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sqlQuery = "SELECT * FROM 82wafoodgo.fg_suppliers where supplier_name like '%" + text + "%' or short_name like '%"
                + text + "%' or official_name like '%" + text + "%' or phone_no like '%"
                + text + "%' or mobile_no like '%" + text + "%' or order_phone_no like '%" + text + "%'";
        try {
            Query query = session.createSQLQuery(sqlQuery).addEntity(Supplier.class);;
            List<Supplier> list = (List<Supplier>) query.list();
            trans.commit();
            for (Supplier supplier : list) {
                SupplierView supplierView = new SupplierView();
                supplierView = getSupplierView1(supplier.getSuppl_id());
                lstSupplierView.add(supplierView);
            }
        } catch (Exception ex) {
            System.out.println("Loi Ne");
            System.out.println(ex);
        }
        return lstSupplierView;
    }

    /**
     * Lucas - Get list supplier from supplierID of manager
     *
     */
    public List<Supplier> fGetListSupplierFromSuppIDManager(int supplId) {
        List<Supplier> lstSupplier = new ArrayList<Supplier>();
        List<SupplierWork> lstSupplierWork = getListSupplierWorkOfManager(supplId);
        for (SupplierWork supplierWork : lstSupplierWork) {
            Supplier supplier = new Supplier();
            supplier = getSupplier(supplierWork.getSuppl_id() + "");
            lstSupplier.add(supplier);
        }
        return lstSupplier;
    }

    public List<SupplierBank> getListSupplierBank(int supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<SupplierBank> list = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(SupplierBank.class);
            criteria.add(Restrictions.eq("suppl_id", supplId));
            list = (List<SupplierBank>) criteria.list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return list;
    }

    public List<SupplierImage> getListSupplierImage(int supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierImage.class);
        criteria.add(Restrictions.eq("suppl_id", supplId));
        List<SupplierImage> list = (List<SupplierImage>) criteria.list();
        trans.commit();
        return list;
    }

    public List<SupplierCategories> getListSupplierCategory(int supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierCategories.class);
        criteria.add(Restrictions.eq("suppl_id", supplId));
        List<SupplierCategories> list = (List<SupplierCategories>) criteria.list();
        trans.commit();
        return list;
    }

    public List<SupplierAddress> getListSupplierAddress(int supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierAddress.class);
        criteria.add(Restrictions.eq("suppl_id", supplId));
        List<SupplierAddress> list = (List<SupplierAddress>) criteria.list();
        trans.commit();
        return list;
    }

    public Address getAddress(int addrId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Address.class);
        criteria.add(Restrictions.eq("addr_id", addrId));
        Address role = (Address) criteria.uniqueResult();
        trans.commit();
        return role;
    }

    public int insertBoard(String supplierName, String ownerId) {
        Board board = new Board();
        board.setBoard_name(supplierName);
        board.setCreated_date(new Date());
        board.setApp_id(Utils.appId);
        board.setOwner_id(ownerId);
        boardDAO.insertBoard(board);
        return board.getBoard_id();
    }

    public boolean insertSupplierView(SupplierView supplierView, String username) {
        int boardIdDesc = 0;
        User user = userDAO.findUserByUserName(username);
        boardIdDesc = insertBoard(supplierView.getSupplier().getSupplier_name(), user.getUser_id());
        if (boardIdDesc > 0) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try {
                // save supplier
                int supplIdDesc = 0;
                supplierView.getSupplier().setApp_id(Utils.appId);
                Serializable ser = session.save(supplierView.getSupplier());
                if (ser != null) {
                    supplIdDesc = (Integer) ser;
                }
                // save supplierWork
                supplierView.getSupplierWork().setSuppl_id(supplIdDesc);
                supplierView.getSupplierWork().setBoard_id(boardIdDesc);
                session.save(supplierView.getSupplierWork());

                // save supplier address
                for (SupplierAddressView addrView : supplierView.getLstSupplAddressView()) {
                    int addressIdDesc = 0;
                    addrView.getAddressOfSuppl().setApp_id(Utils.appId);
                    Serializable serAdd = session.save(addrView.getAddressOfSuppl());
                    if (serAdd != null) {
                        addressIdDesc = (Integer) serAdd;
                    }
                    session.save(addrView.getAddressOfSuppl());
                    SupplierAddress supplAddr = new SupplierAddress();
                    supplAddr.setAddr_id(addressIdDesc);
                    supplAddr.setApp_id(Utils.appId);
                    supplAddr.setSuppl_id(supplIdDesc);
                    supplAddr.setIs_deliver(addrView.isIs_deliver());
                    supplAddr.setIs_main(addrView.isIs_main());
                    session.save(supplAddr);
                }
                // save supplier category
                insertSupplierCategory(supplierView.getCategories(), supplIdDesc, session);
                /*
                         * for(Category category: supplierView.getCategories()) {
                         * SupplierCategories supplCate = new SupplierCategories();
                         * supplCate.setCate_id(category.getCate_id());
                         * supplCate.setCate_name(category.getCate_name());
                         * supplCate.setSuppl_id(supplIdDesc); session.save(supplCate); }
                 */
                // save supplier image
                insertSupplierImage(supplierView.getImages(), supplIdDesc, session);
                /*
                         * for(AttachFile attFile: supplierView.getImages()) { SupplierImage
                         * supplImg = new SupplierImage();
                         * supplImg.setImg_id(attFile.getFile_id());
                         * supplImg.setImg_name(attFile.getFile_name());
                         * supplImg.setSuppl_id(supplIdDesc); session.save(supplImg); }
                 */

                // insert supplier user
                /*
                         * SupplierUser supplUser = new SupplierUser();
                         * supplUser.setApp_id(Utils.appId);
                         * supplUser.setSuppl_id(supplIdDesc);
                         * supplUser.setUser_id(user.getUser_id()); session.save(supplUser);
                         * 
                 */
                insertSupplierBank(supplierView.getSupplierBanks(), supplIdDesc, session);
                trans.commit();
                return true;
            } catch (Exception ex) {
                System.out.println("Error " + ex.getMessage());
                trans.rollback();
                return false;
            } finally {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
        return false;

    }

    private void insertSupplierCategory(List<Category> lstCategory, int supplIdDesc, Session session) {
        for (Category category : lstCategory) {
            SupplierCategories supplCate = new SupplierCategories();
            supplCate.setCate_id(category.getCate_id());
            supplCate.setCate_name(category.getCate_name());
            supplCate.setSuppl_id(supplIdDesc);
            session.save(supplCate);
        }
    }

    private void insertSupplierBank(List<SupplierBank> lstSupplierBank, int supplIdDesc, Session session) {
        for (SupplierBank supplierBank : lstSupplierBank) {
            supplierBank.setSuppl_id(supplIdDesc);
            session.save(supplierBank);
        }
    }

    private void insertSupplierImage(List<SupplierAttachFileView> lstAttachFile, int supplIdDesc, Session session) {
        for (SupplierAttachFileView attFile : lstAttachFile) {
            SupplierImage supplImg = new SupplierImage();
            supplImg.setImg_id(attFile.getAttachFile().getFile_id());
            supplImg.setImg_name(attFile.getAttachFile().getFile_name());
            supplImg.setImg_type(attFile.getImageType());
            supplImg.setSuppl_id(supplIdDesc);
            session.save(supplImg);
        }
    }

    private void deleteReferenceOfSupplier(Session session, int supplId, String table) {
        String deleteQuery = "delete from " + table + " where suppl_id = " + supplId;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public boolean updateSupplierView(SupplierView supplierView) {
        //deleteSupplierImage(supplierView.getSupplier().getSuppl_id());
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            int supplId = supplierView.getSupplier().getSuppl_id();
            session.update(supplierView.getSupplier());
            session.update(supplierView.getSupplierWork());
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_categories");
            insertSupplierCategory(supplierView.getCategories(), supplId, session);
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_imgs");
            insertSupplierImage(supplierView.getImages(), supplId, session);
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_address");
            for (SupplierAddressView addrView : supplierView.getLstSupplAddressView()) {
                int addressIdDesc = 0;
                if (addrView.getAddressOfSuppl().getAddr_id() == 0) {
                    addrView.getAddressOfSuppl().setApp_id(Utils.appId);
                    Serializable serAdd = session.save(addrView.getAddressOfSuppl());
                    if (serAdd != null) {
                        addressIdDesc = (Integer) serAdd;
                    }
                } else {
                    session.update(addrView.getAddressOfSuppl());
                }

                SupplierAddress supplAddr = new SupplierAddress();
                if (addressIdDesc == 0) {
                    supplAddr.setAddr_id(addrView.getAddressOfSuppl().getAddr_id());
                } else {
                    supplAddr.setAddr_id(addressIdDesc);
                }
                supplAddr.setApp_id(Utils.appId);
                supplAddr.setSuppl_id(supplId);
                supplAddr.setIs_deliver(addrView.isIs_deliver());
                supplAddr.setIs_main(addrView.isIs_main());
                session.save(supplAddr);
            }
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_bank");
            insertSupplierBank(supplierView.getSupplierBanks(), supplId, session);
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    private void deleteSupplierImage(int supplierId) {
        List<SupplierImage> supplImgs = getListSupplierImage(supplierId);
        for (SupplierImage suppImage : supplImgs) {
            fileDAO.deleteAttachFile(suppImage.getImg_id());
        }
    }

    private void deleteReferenceOfOder(Session session, int orderId, String table) {
        String deleteQuery = "delete from " + table + " where order_id = " + orderId;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public boolean deleteSupplierView(int supplId, String username) {
        User user = userDAO.findUserByUserName(username);
        List<Order> lstOrder = orderDAO.getListOrder(supplId);
        boardDAO.deleteBoard(getSupplierWork(supplId).getBoard_id());
        deleteSupplierImage(supplId);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            /*
			 * deleteReferenceOfSupplierFavorite(session, supplId,
			 * user.getUser_id(), "fg_favorite_suppliers"); for(Order order:
			 * lstOrder){ deleteReferenceOfOder(session, order.getOrder_id(),
			 * "fg_order_details"); }
             */
            // deleteReferenceOfSupplier(session, supplId, "fg_orders");
            // deleteReferenceOfSupplier(session, supplId, "fg_products");
            deleteReferenceOfSupplier(session, supplId, "fg_businessDays");
            deleteReferenceOfSupplier(session, supplId, "fg_favorite_suppliers");
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_categories");
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_imgs");
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_address");
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_users");
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_bank");
            deleteReferenceOfSupplier(session, supplId, "fg_supplier_work");
            deleteReferenceOfSupplier(session, supplId, "fg_suppliers");

            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    private User getDirectorSuplier(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Query query = session.createSQLQuery(
                "SELECT p.* FROM fg_users p INNER JOIN fg_supplier_users pc ON p.user_id = pc.user_id WHERE is_director = 1 and pc.suppl_id = "
                + supplierId)
                .addEntity(User.class);
        User result = (User) query.uniqueResult();
        trans.commit();
        return result;
    }

    public List<SupplierUser> getListSupplierUser(String username) {
        User user = userDAO.findUserByUserName(username);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<SupplierUser> list = new ArrayList<>();
        try{
            Criteria criteria = session.createCriteria(SupplierUser.class);
            criteria.add(Restrictions.eq("user_id", user.getUser_id()));
            list = (List<SupplierUser>) criteria.list();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return list;
    }

    public List<SupplierView> getListSupplierView(String username) {
        // TODO Auto-generated method stub
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        List<SupplierUser> supplierUsers = getListSupplierUser(username);
        if (username.equals("sysadmin")) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            Query query = session.createSQLQuery("CALL GetListSupplierWorkTree(:managerId)")
                    .addEntity(SupplierWork.class).setParameter("managerId", 0);
            List<SupplierWork> result = query.list();
            trans.commit();
            for (SupplierWork supplWork : result) {
                SupplierView supplView = getSupplierView(supplWork.getSuppl_id());
                supplView.setDirector(getDirectorSuplier(supplWork.getSuppl_id()));
                supplView.setCode(codeDAO.getCode("0001", supplWork.getSuppl_rank()));
                lstSupplierView.add(supplView);
            }
        } else {
            if (supplierUsers.size() > 0) {
                for (SupplierUser suppUse : supplierUsers) {
                    Session session = this.sessionFactory.getCurrentSession();
                    Transaction trans = session.beginTransaction();
                    Query query = session.createSQLQuery("CALL GetListSupplierWorkTree(:managerId)")
                            .addEntity(SupplierWork.class).setParameter("managerId", suppUse.getSuppl_id());
                    List<SupplierWork> result = query.list();
                    trans.commit();
                    for (SupplierWork supplWork : result) {
                        SupplierView supplView = getSupplierView(supplWork.getSuppl_id());
                        supplView.setDirector(getDirectorSuplier(supplWork.getSuppl_id()));
                        supplView.setCode(codeDAO.getCode("0001", supplWork.getSuppl_rank()));
                        lstSupplierView.add(supplView);
                    }
                }

            }
        }

        return lstSupplierView;
    }

    public Supplier getSupplierFromUser(String username) {
        List<SupplierUser> lstSupplierUser = getListSupplierUser(username);
        int supplierId = 0;
        if (lstSupplierUser.size() > 0) {
            supplierId = lstSupplierUser.get(0).getSuppl_id();
        }
        Supplier supplier = getSupplier(supplierId + "");
        return supplier;
    }

    public List<SupplierUser> getListSupplierUserId(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierUser.class);
        criteria.add(Restrictions.eq("user_id", userId));
        List<SupplierUser> list = (List<SupplierUser>) criteria.list();
        trans.commit();
        return list;
    }

    private List<SupplierCategories> getListSupplierCategoryFromCategory(int categoryId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierCategories.class);
        criteria.add(Restrictions.eq("cate_id", categoryId));
        List<SupplierCategories> list = (List<SupplierCategories>) criteria.list();
        trans.commit();
        return list;
    }

    private List<Supplier> getListSupplierFromCategory(String categoryId, String storeType, int pageindex, int pagesize) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "";
        System.out.println(storeType);
        if (storeType.equalsIgnoreCase("DELIVERY")) {
            System.out.println("Vao delivery");
            sql = "select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id inner join fg_supplier_categories sc on s.suppl_id = sc.suppl_id where sw.delivery_id != 'DELIVERIED' and FIND_IN_SET(sc.cate_id,'" + categoryId + "') limit " + pageindex + ", " + pagesize;
        } else if (storeType.equalsIgnoreCase("NONDELIVERY")) {
            System.out.println("Vao Nondelivery");
            sql = "select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id inner join fg_supplier_categories sc on s.suppl_id = sc.suppl_id where sw.delivery_id = 'DELIVERIED' and FIND_IN_SET(sc.cate_id,'" + categoryId + "') limit " + pageindex + ", " + pagesize;
        } else if (storeType.equalsIgnoreCase("ALLDELIVERY")) {
            System.out.println("Vao Alldelivery");
            sql = "select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id inner join fg_supplier_categories sc on s.suppl_id = sc.suppl_id where sw.delivery_id != 'DELIVERIED' and FIND_IN_SET(sc.cate_id,'" + categoryId + "') limit " + pageindex + ", " + pagesize;
        } else if (storeType.equalsIgnoreCase("ALLNONDELIVERY")) {
            System.out.println("Vao AllNondelivery");
            sql = "select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id inner join fg_supplier_categories sc on s.suppl_id = sc.suppl_id where sw.delivery_id = 'DELIVERIED' and FIND_IN_SET(sc.cate_id,'" + categoryId + "') limit " + pageindex + ", " + pagesize;
            
        }
        try {
            suppliers = session.createSQLQuery(sql).addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;
    }

    public List<SupplierAppView> getListSupplierViewOfCategory(String categoryId, String storeType, int pageindex, int pagesize) {
        List<SupplierAppView> lstSupplierAppView = new ArrayList<SupplierAppView>();
        //List<SupplierCategories> lstSupplierCategory = getListSupplierCategoryFromCategory(categoryId);
        List<Supplier> suppliers = getListSupplierFromCategory(categoryId, storeType, pageindex, pagesize);
        for (Supplier supplier : suppliers) {
            SupplierWork supplierWork = getSupplierWork(supplier.getSuppl_id());
//            if (Integer.parseInt(supplierWork.getSuppl_role()) == 21) {
//                SupplierAppView supplierAppView = new SupplierAppView();
//                supplierAppView.setSupplier(supplier);
//                supplierAppView.setSupplierWork(supplierWork);
//                List<SupplierImage> supplImgs = getListSupplierImage(supplier.getSuppl_id());
//                List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
//                for (SupplierImage supImg : supplImgs) {
//                    SupplierAttachFileView splImageView = new SupplierAttachFileView();
//                    splImageView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
//                    splImageView.setImageType(supImg.getImg_type());
//                    supplAttachFiles.add(splImageView);
//                }
//                supplierAppView.setImages(supplAttachFiles);
//                supplierAppView.setReviewCount(boardDAO.getListThread(supplierWork.getBoard_id()).size());
//                lstSupplierAppView.add(supplierAppView);
//            }
            SupplierAppView supplierAppView = new SupplierAppView();
            supplierAppView.setSupplier(supplier);
            supplierAppView.setSupplierWork(supplierWork);
            List<SupplierImage> supplImgs = getListSupplierImage(supplier.getSuppl_id());
            List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
            for (SupplierImage supImg : supplImgs) {
                SupplierAttachFileView splImageView = new SupplierAttachFileView();
                splImageView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
                splImageView.setImageType(supImg.getImg_type());
                supplAttachFiles.add(splImageView);
            }
            supplierAppView.setImages(supplAttachFiles);
            supplierAppView.setReviewCount(boardDAO.getListThread(supplierWork.getBoard_id()).size());
            lstSupplierAppView.add(supplierAppView);
        }
        return lstSupplierAppView;
    }

    public String getSupplierFavorite(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierFavorite.class);
        criteria.add(Restrictions.eq("suppl_id", supplierId));
        criteria.setProjection(Projections.rowCount());
        List rowCount = criteria.list();
        String favoCount = rowCount.get(0).toString();
        trans.commit();
        System.out.println(favoCount);
        return favoCount;
    }

    public List<SupplierView> getListSupplierView1(String username) {
        // TODO Auto-generated method stub
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        List<SupplierUser> supplierUsers = getListSupplierUser(username);

        System.out.println("list la: " + supplierUsers.size());
        for (SupplierUser supplUser : supplierUsers) {
            lstSupplierView.add(getSupplierView(supplUser.getSuppl_id()));
        }
        return lstSupplierView;
    }

    public SupplierFavorite isSupplierFavorite(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierFavorite.class);
        criteria.add(Restrictions.eq("user_id", userId));
        SupplierFavorite supplierFavorite = (SupplierFavorite) criteria.uniqueResult();
        trans.commit();
        return supplierFavorite;
    }

    public boolean insertSupplierFavorite(int supplId, String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            SupplierFavorite supplFavo = new SupplierFavorite();
            supplFavo.setApp_id("e6271952-d4b9-4ed3-b83b-63a56d47a713");
            supplFavo.setSuppl_id(supplId);
            supplFavo.setUser_id(userId);
            session.save(supplFavo);
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    private void deleteReferenceOfSupplierFavorite(Session session, int supplId, String userId, String table) {
        String deleteQuery = "delete from " + table + " where suppl_id = " + "'" + supplId + "'" + " and user_id = "
                + "'" + userId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public boolean deleteSupplierFavorite(int supplId, String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            deleteReferenceOfSupplierFavorite(session, supplId, userId, "fg_favorite_suppliers");
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public SupplierView getSupplierView2(int supplId) {
        System.out.println("run toi day 2");
        SupplierView supplierView = new SupplierView();
        supplierView.setSupplier(getSupplier(supplId + ""));
        supplierView.setSupplierWork(getSupplierWork(supplId));
        // get image of supplier
        List<SupplierImage> supplImgs = getListSupplierImage(supplId);
        List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
        for (SupplierImage supImg : supplImgs) {
            SupplierAttachFileView supAttachView = new SupplierAttachFileView();
            supAttachView.setImageType(supImg.getImg_type());
            supAttachView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
            supplAttachFiles.add(supAttachView);

        }
        supplierView.setLstAttachFileView(supplAttachFiles);
        // get categories of supplier
        List<SupplierCategories> supplCates = getListSupplierCategory(supplId);
        List<Category> supplCategories = new ArrayList<Category>();
        for (SupplierCategories supCate : supplCates) {
            supplCategories.add(categoryDAO.getCategory(supCate.getCate_id() + ""));
        }
        supplierView.setCategories(supplCategories);
        // get address of supplier
        List<SupplierAddress> supplAddresses = getListSupplierAddress(supplId);
        List<SupplierAddressView> supplAddressViewLst = new ArrayList<SupplierAddressView>();
        for (SupplierAddress supplAddr : supplAddresses) {
            SupplierAddressView suppAddrView = new SupplierAddressView();
            suppAddrView.setAddressOfSuppl(getAddress(supplAddr.getAddr_id()));
            suppAddrView.setIs_deliver(supplAddr.getIs_deliver());
            suppAddrView.setIs_main(supplAddr.getIs_main());
            supplAddressViewLst.add(suppAddrView);
        }
        supplierView.setLstSupplAddressView(supplAddressViewLst);
        // get supplier manager
        // supplierView.setSupplierManage(getSupplier(supplId + ""));
        return supplierView;
    }

    public SupplierUser checkUserIsStore(String username) {
        User user = userDAO.findUserByUserName(username);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Query query = session.createSQLQuery(
                "select su.* from fg_supplier_users su inner join fg_user_roles ur on su.user_id=ur.user_id inner join fg_roles r on ur.role_id=r.role_id where r.role_name='Store' and su.user_id='"
                + user.getUser_id() + "' limit 1 ")
                .addEntity(SupplierUser.class);
        SupplierUser supplierUser = (SupplierUser) query.uniqueResult();
        trans.commit();
        return supplierUser;
    }
    // Get list favorite store for user

    public List<Supplier> getListSupplierFavoriteByUser(String userID) {
        List<Supplier> results = new ArrayList<Supplier>();
        // Get supplierID
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sql = "SELECT f.suppl_id FROM fg_favorite_suppliers f WHERE f.user_id = '" + userID + "'";
        SQLQuery query = session.createSQLQuery(sql);
        List<Object[]> entities = query.list();
        System.out.println("show entities " + entities);
        // Get supplier
        for (Object entity : entities) {
            session = this.sessionFactory.openSession();
            System.out.println(" " + entity);
            String sql3 = "SELECT * FROM fg_suppliers s WHERE s.suppl_id='" + entity + "'";
            System.out.println(sql3);
            SQLQuery query3 = session.createSQLQuery(sql3);
            query3.addEntity(Supplier.class);
            results.add((Supplier) query3.uniqueResult());
            System.out.println("show result" + results);
        }
        trans.commit();
        System.out.println("show result count:" + results.size());
        return results;
    }

    // Delete favorite store
    public void deleteFavoriteStore(Session session, int suppl_id) {
        String deleteQuery = "delete from fg_favorite_suppliers where suppl_id = " + suppl_id;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public String deleteStoreFavorite(int supplId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            deleteFavoriteStore(session, supplId);
            trans.commit();
            return "true";
        } catch (Exception ex) {
            trans.rollback();
            return "false";
        }
    }

    public List<BillHistoryView> getListBillHistory(String userID) {
        //List BillHistory
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sql = "SELECT o.user_id ,o.order_id, o.order_date, o.deliver_date,o.order_status, o.prod_amt, o.app_id,od.prod_id,od.prod_qty,prod_name,s.suppl_id, s.supplier_name,p.unit_price,od.lstOption,od.lstQty_Option,o.order_amt FROM fg_orders o  INNER JOIN fg_order_details od ON o.order_id = od.order_id INNER JOIN	fg_products p ON od.prod_id = p.prod_id INNER JOIN fg_suppliers s ON	o.suppl_id = s.suppl_id WHERE o.user_id = '" + userID + "'";
        List<BillHistory> listBillHistory = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(BillHistory.class)).list();
        trans.commit();

        List<ProductOptionalBH> listProductOption = new ArrayList<ProductOptionalBH>();
        List<BillHistoryView> listBillHistoryView = new ArrayList<BillHistoryView>();
        for (BillHistory billHistory : listBillHistory) {
            //Get list product option         
            if (billHistory.getLstOption() != "null") {
                listProductOption = getListProductOptions(billHistory.getLstOption());
            }
            //Set value for list BillHistoryView
            BillHistoryView billHistoryView = new BillHistoryView();
            billHistoryView.setListProductOptions(listProductOption);
            billHistoryView.setBillHistory(billHistory);
            listBillHistoryView.add(billHistoryView);
        }

        return listBillHistoryView;
    }

    public List<BillHistoryView> getListSearchBillHistory(String userID, String dateFrom, String dateTo) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sql = "SELECT o.order_id, o.order_date, o.deliver_date,o.order_status, o.prod_amt, o.app_id,od.prod_id,od.prod_qty,prod_name,s.suppl_id, s.supplier_name,p.unit_price,od.lstOption,od.lstQty_Option,o.order_amt  FROM fg_orders o  INNER JOIN fg_order_details od ON o.order_id = od.order_id INNER JOIN	fg_products p ON od.prod_id = p.prod_id INNER JOIN fg_suppliers s ON	o.suppl_id = s.suppl_id WHERE o.user_id = '" + userID + "' AND o.order_date >= '" + dateFrom + "' AND o.order_date<='" + dateTo + "' ORDER BY o.order_date";
        List<BillHistory> listBillHistory = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(BillHistory.class)).list();
        trans.commit();

        List<ProductOptionalBH> listProductOption = new ArrayList<ProductOptionalBH>();
        List<BillHistoryView> listBillHistoryView = new ArrayList<BillHistoryView>();
        for (BillHistory billHistory : listBillHistory) {
            //Get list product option         
            if (billHistory.getLstOption() != "null") {
                listProductOption = getListProductOptions(billHistory.getLstOption());
            }
            //Set value for list BillHistoryView
            BillHistoryView billHistoryView = new BillHistoryView();
            billHistoryView.setListProductOptions(listProductOption);
            billHistoryView.setBillHistory(billHistory);
            listBillHistoryView.add(billHistoryView);
        }

        return listBillHistoryView;
    }

    public List<Supplier> getListSupplierManage(int roleId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id where sw.suppl_role = (select parent_id from fg_roles where role_id = " + roleId + ");").addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;

    }

    public List<Supplier> getListSupplierFromRoles(String roleJoin) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id where find_in_set(sw.suppl_role, '" + roleJoin + "')").addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;
    }

    public List<SupplierWork> getListSupplierWorkOfManager(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<SupplierWork> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select * from fg_supplier_work where manage_suppl_id =" + supplierId).addEntity(SupplierWork.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;
    }

    public List<SupplierWork> getListSupplierWorkOfRole(int roleId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<SupplierWork> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select * from fg_supplier_work where suppl_role = " + roleId).addEntity(SupplierWork.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;
    }

    public List<SupplierView> getListSupplierViewOfManage(int supplierId) {
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        List<SupplierWork> supplierWorks = getListSupplierWorkOfManager(supplierId);
        for (SupplierWork supplierWork : supplierWorks) {
            SupplierView supplView = getSupplierView(supplierWork.getSuppl_id());
            supplView.setDirector(getDirectorSuplier(supplierWork.getSuppl_id()));
            supplView.setCode(codeDAO.getCode("0001", supplierWork.getSuppl_rank()));
            lstSupplierView.add(supplView);
        }
        return lstSupplierView;
    }

    public List<SupplierView> getListSupplierViewOfRole(int roleId) {
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        List<SupplierWork> supplierWorks = getListSupplierWorkOfRole(roleId);
        for (SupplierWork supplierWork : supplierWorks) {
            SupplierView supplView = getSupplierView(supplierWork.getSuppl_id());
            supplView.setDirector(getDirectorSuplier(supplierWork.getSuppl_id()));
            supplView.setCode(codeDAO.getCode("0001", supplierWork.getSuppl_rank()));
            lstSupplierView.add(supplView);
        }
        return lstSupplierView;
    }

    public List<Supplier> getListSupplierOfManage(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id where manage_suppl_id = " + supplierId).addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;
    }

    public List<Supplier> getListSupplierOfRole(int roleId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id where suppl_role = " + roleId).addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return suppliers;
    }

    public List<Supplier> getListStore() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers s inner join fg_supplier_work sw on s.suppl_id = sw.suppl_id inner join fg_roles r on r.role_id = sw.suppl_role where r.role_id = 21").addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }

        return suppliers;
    }

    /**
     * Lucas - Get list supplier from userID
     *
     */
    public List<Supplier> fGetListSupplierFromUserName(String username) {

        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers as s left join fg_supplier_users as su on s.suppl_id = su.suppl_id left join fg_users as u on su.user_id = u.user_id where u.user_name = '" + username + "'").addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }

        return suppliers;
//        List<Supplier> lstSupplier = new ArrayList<Supplier>();
//        List<SupplierUser> lstSupplierUser = new ArrayList<SupplierUser>();
//        lstSupplierUser = getListSupplierUser(username);
//        for (SupplierUser supplierUser: lstSupplierUser) {
//            Supplier supplier = new Supplier();
//            supplier = getSupplier(supplierUser.getSuppl_id()+"");
//            lstSupplier.add(supplier);
//        }
//        return lstSupplier;
    }

    public BillSupplierInformation getBillSupplierInformation(String userId) {

        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        BillSupplierInformation billSupplierInformation = new BillSupplierInformation();
        try {
            Query query = session.createSQLQuery("select s.supplier_name, s.address, CONCAT_WS(' ', u.first_name, u.middle_name, u.last_name) as ownername from fg_suppliers s join fg_supplier_users su on  s.suppl_id = su.suppl_id join fg_users u on su.user_id = u.user_id where u.user_id = '" + userId + "' limit 1")
                    .setResultTransformer(Transformers.aliasToBean(BillSupplierInformation.class));
            billSupplierInformation = (BillSupplierInformation) query.uniqueResult();
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return billSupplierInformation;
    }

    public List<Supplier> getListSupplierChildFromUser(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select s.* from fg_suppliers s join fg_supplier_work sw on s.suppl_id = sw.suppl_id where sw.manage_suppl_id in (select swh.suppl_id from fg_suppliers swh join fg_supplier_users su on su.suppl_id = swh.suppl_id where su.user_id='" + userId + "')").addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }

        return suppliers;
    }

    public List<Supplier> getListSupplierFromSupplierIds(String supplierIds) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Supplier> suppliers = new ArrayList<>();
        try {
            suppliers = session.createSQLQuery("select * from fg_suppliers where find_in_set(suppl_id,'" + supplierIds + "')").addEntity(Supplier.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }

        return suppliers;
    }

    public List<ProductOptionalBH> getListProductOptions(String stringList) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sql = "SELECT * FROM fg_products WHERE  FIND_IN_SET(prod_id, '" + stringList + "')";
        List<Product> listProduct = session.createSQLQuery(sql).addEntity(Product.class).list();
        trans.commit();
        List<ProductOptionalBH> listProductOptional = new ArrayList<ProductOptionalBH>();
        for (Product product : listProduct) {
            ProductOptionalBH productOptional = new ProductOptionalBH();
            productOptional.setProduct(product);
            productOptional.setOptionalQuanlity("");
            listProductOptional.add(productOptional);
        }

        return listProductOptional;
    }

    public Supplier insertUserSupplierView(UserSupplierView userSupplierView, String username) {
        insertSupplierView(userSupplierView.getSupplierView(), username);
        User user = userSupplierView.getUser();
        List supplierIds = new ArrayList();
        supplierIds.add(userSupplierView.getSupplierView().getSupplier().getSuppl_id());
        user.setListSupplierId(supplierIds);
        System.err.println("supplier id" + user.getListSupplierId().size());
        userDAO.insertUser(userSupplierView.getUser());
        return userSupplierView.getSupplierView().getSupplier();
    }
       
       public boolean updateStoreInformation(SupplierView supplierView){
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try {
                int supplierId = supplierView.getSupplier().getSuppl_id();
                String strQuery = "update fg_suppliers set origin_food = '" + supplierView.getSupplier().getOrigin_food() + "' where suppl_id=" + supplierId;
                session.createSQLQuery(strQuery).executeUpdate();
                strQuery = "update fg_supplier_work set suppl_delivery_info = '" + supplierView.getSupplierWork().getSuppl_delivery_info() + "' where suppl_id=" + supplierId;
                session.createSQLQuery(strQuery).executeUpdate();
                trans.commit();
                return true;
            } catch (Exception ex) {
                System.out.println("Error " + ex.getMessage());
                trans.rollback();
                return false;
            } 
        
       }
       public int caculateAverageStar (int supplier_id){
           Session session = this.sessionFactory.getCurrentSession();
           Transaction trans = session.beginTransaction();
           int result = 0;
           try {
               String strQuery = "SELECT AVG (t.review_rank5) FROM 82wafoodgo.fg_threads t WHERE t.board_id IN ( SELECT s.board_id FROM 82wafoodgo.fg_supplier_work s WHERE s.suppl_id = "+supplier_id+")";
                Query query = session.createSQLQuery(strQuery);
                result = (int)query.uniqueResult();
                trans.commit();
                return result;
               
           }catch(Exception ex){
               System.out.println("Error caculateAverageStar " + ex.getMessage());
                trans.rollback();
                return result;
           }
       }
}
