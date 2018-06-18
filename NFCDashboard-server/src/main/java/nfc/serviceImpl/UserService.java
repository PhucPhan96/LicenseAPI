package nfc.serviceImpl;

import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import static nfc.controller.SMSController.ACCOUNT_SID;
import static nfc.controller.SMSController.AUTH_TOKEN;

import nfc.model.Address;
import nfc.model.AppUser;
import nfc.model.Category;
import nfc.model.Code;
import nfc.model.Email;
import nfc.model.Mail;

import nfc.model.Role;
import nfc.model.SupplierAddress;
import nfc.model.SupplierFavorite;
import nfc.model.SupplierUser;
import nfc.model.User;
import nfc.model.UserAddress;

import nfc.model.UserLogin;
import nfc.model.UserRegister;
import nfc.model.UserRole;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.SupplierView;
import nfc.model.ViewModel.UserAddressView;
import nfc.service.IMailService;
import nfc.service.IRoleService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;

@Transactional
public class UserService implements IUserService {

    @Autowired
    private ISupplierService supplDAO;
    @Autowired
    private IRoleService roleServiceDao;
    @Autowired
    private IMailService mailDAO;
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<User> getListUser() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<User> list = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(User.class);
            list = (List<User>) criteria.list();
            trans.commit();

        } catch (Exception ex) {
            trans.rollback();
        }
        return list;

    }

    public List<User> getListUserPermissionStore(String username) {
        User user = findUserByUserName(username);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<User> result;
        try {
            Query query = session.createSQLQuery(
                    "CALL GetListUser(:userid)")
                    .addEntity(User.class)
                    .setParameter("userid", user.getUser_id());
            result = query.list();
        } catch (Exception ex) {
            result = new ArrayList<User>();
        }
        trans.commit();
        return result;
    }

    public boolean updateUser(User user) {
        System.out.println("vao dc update");
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.update(user);
            updateUserSupplier(session, user);
            deleteAddressOfUser(session, user.getUser_id());
            for (UserAddressView addrView : user.getLstuserAddress()) {
                int addressIdDesc = 0;
                addrView.getAddressOfUser().setApp_id(Utils.appId);
                Serializable serAdd = session.save(addrView.getAddressOfUser());
                if (serAdd != null) {
                    addressIdDesc = (Integer) serAdd;
                    System.out.println("add ID: " + addressIdDesc);
                }
                session.save(addrView.getAddressOfUser());
                UserAddress userAddr = new UserAddress();
                userAddr.setAddr_id(addressIdDesc);
                userAddr.setApp_id(Utils.appId);
                userAddr.setUser_id(user.getUser_id());
                userAddr.setIs_deliver(addrView.isIs_deliver());
                userAddr.setIs_main(addrView.isIs_main());
                session.save(userAddr);
            }
            deleteRoleOfUser(session, user.getUser_id());
            insertUserRole(session, user);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean insertUser(User user) {

        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            user.setApp_id(Utils.appId);
            //user.setPassword(Utils.BCryptPasswordEncoder(user.getPassword()));
            session.save(user);
            insertUserRole(session, user);
            insertUserSupplier(session, user);
            //save supplier address
            System.out.println("con save address");
            System.out.println(user.getLstuserAddress().size());

            for (UserAddressView addrView : user.getLstuserAddress()) {
                int addressIdDesc = 0;
                addrView.getAddressOfUser().setApp_id(Utils.appId);
                Serializable serAdd = session.save(addrView.getAddressOfUser());
                if (serAdd != null) {
                    addressIdDesc = (Integer) serAdd;
                    System.out.println("add ID: " + addressIdDesc);
                }
                session.save(addrView.getAddressOfUser());
                UserAddress userAddr = new UserAddress();
                userAddr.setAddr_id(addressIdDesc);
                userAddr.setApp_id(Utils.appId);
                userAddr.setUser_id(user.getUser_id());
                userAddr.setIs_deliver(addrView.isIs_deliver());
                userAddr.setIs_main(addrView.isIs_main());
                session.save(userAddr);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    private void insertUserRole(Session session, User user) {
        for (Role r : user.getLstRoles()) {
            UserRole userRole = new UserRole();
            userRole.setUser_id(user.getUser_id());
            userRole.setApp_id(Utils.appId);
            userRole.setRole_id(r.getRole_id());
            session.save(userRole);
        }

    }

    public boolean insertUserFb(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            User userfb = new User();
            userfb.setUser_id(user.getUser_id());
            userfb.setApp_id(user.getApp_id());
            userfb.setUser_name(user.getUser_name());
            userfb.setUser_alias(user.getUser_alias());
            userfb.setFirst_name(user.getFirst_name());
            userfb.setMiddle_name(user.getMiddle_name());
            userfb.setLast_name(user.getLast_name());
            userfb.setEnglish_name(user.getEnglish_name());
            userfb.setIs_anonymous(user.getIs_anonymous());
            userfb.setPassword(user.getPassword());
            userfb.setPassword_salt(user.getPassword_salt());
            userfb.setIs_lockedout(user.getIs_lockedout());
            userfb.setIs_registered(user.getIs_registered());
            userfb.setCreated_date(user.getCreated_date());
            userfb.setLast_act_date(user.getLast_act_date());
            userfb.setLast_login_date(user.getLast_login_date());
            userfb.setLast_password_changed_date(user.getLast_password_changed_date());
            userfb.setLast_locked_date(user.getLast_locked_date());
            userfb.setFailed_password_count(user.getFailed_password_count());
            userfb.setPassword_expired_date(user.getPassword_expired_date());
            userfb.setMobile_no(user.getMobile_no());
            userfb.setPhone_no(user.getPhone_no());
            userfb.setIdcard_no(user.getIdcard_no());
            userfb.setSex_type(user.getSex_type());
            userfb.setRegistered_date(user.getRegistered_date());
            userfb.setIs_active(user.getIs_active());
            userfb.setEmail(user.getEmail());
            session.save(userfb);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    private void insertUserSupplier(Session session, User user) {
        for (Integer supplierId : user.getListSupplierId()) {
            SupplierUser userSuppl = new SupplierUser();
            userSuppl.setUser_id(user.getUser_id());
            userSuppl.setApp_id(user.getApp_id());
            userSuppl.setSuppl_id(supplierId);
            session.save(userSuppl);
        }

    }

    private void updateUserSupplier(Session session, User user) {
        deleteSupplierOfUser(session, user.getUser_id());
        insertUserSupplier(session, user);
        //String updateQuery = "update fg_supplier_users set suppl_id ="+ user.getSuppl_id()+" where user_id = '" + user.getUser_id()+"'";
//            Query query = session.createSQLQuery(updateQuery);
//	    query.executeUpdate();
    }

    public boolean deleteUser(String userID) {
        System.out.print("Vao nay roi " + userID);
        User user = getUser(userID);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            deleteRoleOfUser(session, userID);
            session.delete(user);
            deleteAddressOfUser(session, userID);
            deleteSupplierOfUser(session, userID);
            trans.commit();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            trans.rollback();
            return false;
        }

    }

    public User getUser(String userId) {
        //List<SupplierUser> supplUser = supplDAO.getListSupplierUserId(userId);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("user_id", userId));
        User user = (User) criteria.uniqueResult();
        user.setListSupplierId(getListSupplierIdOfUser(session, userId));
//		set SupplierUser
//		if(supplUser.size()>0)
//		{
//			user.setSuppl_id(supplUser.get(0).getSuppl_id());
//		}
//		 set Address user
        List<UserAddress> userAddresses = getListUserAddress(session, userId);
        List<UserAddressView> userAddressViewLst = new ArrayList<UserAddressView>();
        for (UserAddress userAddr : userAddresses) {
            UserAddressView userAddrView = new UserAddressView();
            userAddrView.setAddressOfUser(getAddress(session, userAddr.getAddr_id()));
            userAddrView.setIs_deliver(userAddr.isIs_deliver());
            userAddrView.setIs_main(userAddr.isIs_main());
            userAddressViewLst.add(userAddrView);
        }
        user.setLstuserAddress(userAddressViewLst);
//		set role user
        List<UserRole> listUserRoles = getlstUserRolePrivate(session, userId);
        List<Role> listRole = new ArrayList<Role>();
        for (UserRole item : listUserRoles) {
            Role role = new Role();
            role = roleServiceDao.getRoleWithSeeion(session, String.valueOf(item.getRole_id()));
            System.out.println("get 1 dc phan tu");
            System.out.println("id la:" + role.getRole_id());
            listRole.add(role);
        }
        user.setLstRoles(listRole);
        trans.commit();
        return user;
    }

    private List<Integer> getListSupplierIdOfUser(Session session, String userId) {
        String sql = "select suppl_id from fg_supplier_users where user_id = '" + userId + "'";
        SQLQuery query = session.createSQLQuery(sql);
        List<Integer> listSupplerId = query.list();
        return listSupplerId;
    }

    public User findUserByUserName(String username) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("user_name", username));
        User user = (User) criteria.uniqueResult();
        trans.commit();
        return user;
    }

    public List<UserRole> getListUserRole(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserRole.class);
        criteria.add(Restrictions.eq("user_id", userId));
        List<UserRole> userRoles = (List<UserRole>) criteria.list();
        trans.commit();
        return userRoles;
    }

    public List<UserRole> getlstUserRolePrivate(Session session, String userId) {
        session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UserRole.class);
        criteria.add(Restrictions.eq("user_id", userId));
        List<UserRole> userRoles = (List<UserRole>) criteria.list();
        return userRoles;
    }

    private void deleteAddressOfUser(Session session, String userId) {
        String deleteQuery = "delete from fg_user_address where user_id = '" + userId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    private void deleteRoleOfUser(Session session, String userId) {
        String deleteQuery = "delete from fg_user_roles where user_id = '" + userId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    private void deleteSupplierOfUser(Session session, String userId) {
        String deleteQuery = "delete from fg_supplier_users where user_id = '" + userId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public List<UserAddress> getListUserAddress(Session session, String userId) {
        //Session session = this.sessionFactory.getCurrentSession();
        //Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(UserAddress.class);
        criteria.add(Restrictions.eq("user_id", userId));
        List<UserAddress> list = (List<UserAddress>) criteria.list();
        //trans.commit();
        return list;
    }

    public Address getAddress(Session session, int addrId) {
        //Session session = this.sessionFactory.getCurrentSession();
        //Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Address.class);
        criteria.add(Restrictions.eq("addr_id", addrId));
        Address role = (Address) criteria.uniqueResult();
        //trans.commit();
        return role;
    }

    public boolean ChangPasswordUser(String userId, String pass, String passSalt) {
        System.out.println("vao dc update");
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            String strQuery = "update fg_users set password='" + pass + "',password_salt='" + passSalt + "' where user_id = '" + userId + "'";
            Query query = session.createSQLQuery(strQuery);
            query.executeUpdate();
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public List<UserAddress> getListUserAddress(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(UserAddress.class);
        criteria.add(Restrictions.eq("user_id", userId));
        List<UserAddress> lstUserAddress = (List<UserAddress>) criteria.list();
        trans.commit();
        return lstUserAddress;
    }

    public boolean insertUserRegister(UserRegister userRegist) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            String code = Utils.generationCode();
            userRegist.setReq_code(code);
            session.save(userRegist);
            System.out.println("code la " + code);
            sendSMS(code);
            //send code by sms to  mobile
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
            
        }
    }

    public boolean updateUserRegister(UserRegister userRegist) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.update(userRegist);
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    public UserRegister getUserRegister(String email) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(UserRegister.class);
        criteria.add(Restrictions.eq("req_email", email));
        UserRegister userRegist = (UserRegister) criteria.uniqueResult();
        trans.commit();
        return userRegist;
    }

    public String saveUserRegister(UserRegister userRegist) {
        UserRegister userExist = getUserRegister(userRegist.getReq_email());
        if (userExist == null) {
            userRegist.setReq_code(Utils.generationCode());
            userRegist.setReq_approved(false);
            if (insertUserRegister(userRegist)) {
                mailDAO.sendSimpleMail("kjncunn@gmail.com", userRegist.getReq_email(), "Verify", "Code for register nfc account: " + userRegist.getReq_code());
                return "sendNewVerify";
            }
        } else if (!userExist.isReq_approved()) {
            if (StringUtils.isEmpty(userRegist.getReq_code())) {
                userExist.setReq_code(Utils.generationCode());
                if (updateUserRegister(userExist)) {
                    mailDAO.sendSimpleMail("kjncunn@gmail.com", userRegist.getReq_email(), "Verify", "Code for register nfc account: " + userExist.getReq_code());
                    return "refreshVerify";
                }
            } else if (userRegist.getReq_code().equals(userExist.getReq_code())) {

                Session session = this.sessionFactory.getCurrentSession();
                Transaction trans = session.beginTransaction();
                String passwordRandom = Utils.randomPassword(8);
                try {
                    userExist.setReq_approved(true);
                    AppUser user = new AppUser();
                    user.setApp_id(Utils.appId);
                    user.setUser_id(UUID.randomUUID().toString());
                    user.setUser_name(userExist.getReq_email());
                    user.setPassword(Utils.Sha1(passwordRandom));
                    java.util.Date currentDay = new java.util.Date();
                    user.setCreated_date(new Date(currentDay.getTime()));
                    user.setIs_active((byte) 1);
                    user.setIs_lockedout((byte) 0);
                    user.setMobile_no(userRegist.getReq_mobile());
                    user.setLast_name(userExist.getReq_name());
                    session.save(user);
                    //Insert Address
                    Address address = new Address();
                    address.setAddress(userRegist.getReq_address());
                    address.setApp_id(Utils.appId);
                    int addressIdDesc = 0;
                    Serializable serAdd = session.save(address);
                    if (serAdd != null) {
                        addressIdDesc = (Integer) serAdd;
                    }
                    System.out.println("addressId " + addressIdDesc);
                    //insert user address
                    UserAddress userAddr = new UserAddress();
                    userAddr.setAddr_id(addressIdDesc);
                    userAddr.setApp_id(Utils.appId);
                    userAddr.setUser_id(user.getUser_id());
                    userAddr.setIs_deliver(true);
                    userAddr.setIs_main(true);
                    session.save(userAddr);
                    trans.commit();
                    updateUserRegister(userExist);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    trans.rollback();
                }

                mailDAO.sendSimpleMail("kjncunn@gmail.com", userRegist.getReq_email(), "Verify Password", "password for email " + userRegist.getReq_email() + " : " + passwordRandom);
                return userRegist.getReq_email() + ":" + Utils.Sha1(passwordRandom);
            }
        } else {
            return "exist";
        }
        return "fail";
    }

    /**
     * Lucas
         *
     */
    public List<User> getListUserByPhoneNumber(String phoneNum) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("phone_no", phoneNum));
        List<User> lstUser = (List<User>) criteria.list();
        trans.commit();
        return lstUser;
    }

    public List<UserAddress> getListUserByAddress(String address) {
        List<UserAddress> lstUserAddress = new ArrayList<UserAddress>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String stringSQL = "SELECT q.* FROM 82wafoodgo.fg_address p inner join fg_user_address q on p.addr_id = q.addr_id "
                + "where p.city like '%" + address + "%' or p.region like '%" + address + "%' or p.address like '%" + address + "%';";
        try {
            Query query = session.createSQLQuery(stringSQL).addEntity(UserAddress.class);;
            lstUserAddress = (List<UserAddress>) query.list();

        } catch (Exception ex) {
            System.out.println("Loi Ne");
            System.out.println(ex);
        }
        trans.commit();
        return lstUserAddress;
    }
    public List<User> getListUserByLikePhone(String phone) {
        List<User> lstUser = new ArrayList<User>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String stringSQL = "SELECT * FROM 82wafoodgo.fg_users where mobile_no like '%" + phone +"%' or phone_no like '%" + phone +"%';";
        try {
            Query query = session.createSQLQuery(stringSQL).addEntity(User.class);;
            lstUser = (List<User>) query.list();

        } catch (Exception ex) {
            System.out.println("Loi Ne");
            System.out.println(ex);
        }
        trans.commit();
        return lstUser;
    }
    public List<User> getListUserByLikePhoneAndAddress(String phone, String address) {
        List<User> lstUser = new ArrayList<User>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String stringSQL = "SELECT r.* FROM 82wafoodgo.fg_address p inner join fg_user_address q inner join fg_users r on p.addr_id = q.addr_id and q.user_id = r.user_id "
                + "where (p.city like '%" + address + "%' or p.region like '%" + address + "%' or p.address like '%" + address + "%') "
                + "and (r.mobile_no like '%" + address + "%' or r.phone_no like '%" + address + "%');";
        try {
            Query query = session.createSQLQuery(stringSQL).addEntity(User.class);;
            lstUser = (List<User>) query.list();

        } catch (Exception ex) {
            System.out.println("Loi Ne");
            System.out.println(ex);
        }
        trans.commit();
        return lstUser;
    }
    
// Forgot Password - LanAnh

    public boolean updateUserForgotPassword(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.update(user);
            trans.commit();
            return true;

        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    public User getUserForgotPassword(String email) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        User user = (User) criteria.uniqueResult();
        trans.commit();
        System.out.println("Vao Ham getUserForgotPassword");
        return user;
    }

    public String forgotPassword(User user) {
        User userExist = getUserForgotPassword(user.getEmail());
        System.out.println("getUserForgotPassword " + userExist);
        if (userExist != null) {
//            String passwordRandom = Utils.randomPassword(8);
//            userExist.setPassword(Utils.Sha1(passwordRandom));
            System.out.println("Mail User " + user.getEmail());           
            if (updateUserForgotPassword(userExist)) {                                    
                    return "success";                         
                
            } else {
                return "fail";
            }

        } else {
            return "notExist";
        }
    }

    public boolean insertUserLogin(String username) {
        User user = findUserByUserName(username);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            UserLogin userLogin = new UserLogin();
            userLogin.setUser_id(user.getUser_id());
            userLogin.setApp_id(Utils.appId);
            userLogin.setLogin_date(new java.util.Date());
            session.save(userLogin);
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    public List<User> getListUserOfRole(int roleId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<User> users = new ArrayList<>();
        try {
            users = session.createSQLQuery("select u.* from fg_users u inner join fg_user_roles ur on u.user_id = ur.user_id where ur.role_id = " + roleId).addEntity(User.class).list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return users;

    }

    public String getUserIdOfSupplier(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String userId = "";
        try {
            userId = (String) session.createSQLQuery("select user_id from fg_supplier_users where suppl_id='" + supplierId + "' limit 1").uniqueResult();
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return userId;
    }
    
    public List<User> getListUserGrid(GridView gridView){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<User> users = new ArrayList<>();
        try {
            String filter = Utils.generateGridFilterString(gridView).toString();
            filter = filter.equals("") ? "" : (" and " +  filter); 
            users = session.createSQLQuery("select u.mobile_no, u.user_name, u.email, u.created_date, u.user_id, (select group_concat(s.supplier_name) from fg_suppliers s join fg_supplier_users su on s.suppl_id = su.suppl_id  where su.user_id = u.user_id) as supplier_names from fg_users u join fg_supplier_users su on u.user_id = su.user_id join fg_suppliers s on su.suppl_id = s.suppl_id where find_in_set(su.suppl_id, '" + gridView.getData() + "')" + filter + " limit " + gridView.getPageSize() + " offset " + ((gridView.getPageIndex() - 1) * gridView.getPageSize()))
                            //.addEntity(User.class).list();
                            .setResultTransformer(Transformers.aliasToBean(User.class)).list();
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return users;
    }
    
    
    public long countUserGrid(GridView gridView){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        long count = 0;
        try {
            String filter = Utils.generateGridFilterString(gridView).toString();
            filter = filter.equals("") ? "" : (" and " +  filter);
            count = (long) session.createSQLQuery("select count(*) as count from fg_users u join fg_supplier_users su on u.user_id = su.user_id join fg_suppliers s on su.suppl_id = s.suppl_id where find_in_set(su.suppl_id, '" + gridView.getData() + "')" + filter)
                    .addScalar("count", LongType.INSTANCE)
                    .uniqueResult();
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return count;
    }
    
    public List<SupplierFavorite> fGetListSupplierFavoriteByUserId(String userId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(SupplierFavorite.class);
        criteria.add(Restrictions.eq("user_id", userId));
        List<SupplierFavorite> lstSupplier = (List<SupplierFavorite>) criteria.list();
        trans.commit();
        return lstSupplier;
    }
    
    List<SupplierView> fGetListSupplierViewFavoriteByListFavorite(List<SupplierFavorite> lstSupplier){
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        for (SupplierFavorite supplier: lstSupplier) {
            SupplierView supplierView = new SupplierView();
            supplierView = supplDAO.getSupplierView(supplier.getSuppl_id());
            lstSupplierView.add(supplierView);
        }
        return lstSupplierView;
    }  
    
    public UserRegister getUserRegisterByEmail(Email email) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<UserRegister> listUserRegister = new ArrayList<UserRegister>() ;
        UserRegister userRegister = new UserRegister();
        try{
            listUserRegister = session.createSQLQuery("SELECT sw.* FROM 82wafoodgo.fg_user_regist sw  WHERE sw.req_email ='" + email.getEmail() +"'").addEntity(UserRegister.class).list();
            userRegister = listUserRegister.get(0);
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }     
        return userRegister;
    }
    
    //Insert User App
    public static final String ACCOUNT_SID = "ACb4fc4a37e7e7edd2396d1c8bfe766034";
    public static final String AUTH_TOKEN = "01a94a54d2c1a124b0a73d0dc7715754";
    public void sendSMS(String code){
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message
            .creator(new PhoneNumber("+821025670105"), new PhoneNumber("+12512542245"),
                    "SMS notification from NFC, your verify code is "+ code)
            .create();
            System.out.println("vao ham send SMS");
             System.out.println(message.getSid()); 
             
	}
    public boolean insertUserApp(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            
            user.setApp_id(Utils.appId);
            java.util.Date date = new java.util.Date();
            user.setCreated_date(date);
            user.setIs_active(true);            
            session.save(user);
            trans.commit();
        
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
    public User getUserByEmail(Email email) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<User> listUser = new ArrayList<User>() ;
        User user = new User();
        try{
            listUser= session.createSQLQuery("SELECT sw.* FROM 82wafoodgo.fg_users sw  WHERE sw.email ='" + email.getEmail() +"'").addEntity(User.class).list();
            user = listUser.get(0);
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }     
        return user;
    }
    
    public boolean deleteUserResgist(Email email) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();        
        List<UserRegister> listUserRegister = new ArrayList<UserRegister>();
        UserRegister userRegister = new UserRegister();
        try{
            listUserRegister = session.createSQLQuery("SELECT sw.* FROM 82wafoodgo.fg_user_regist sw  WHERE sw.req_email ='" + email.getEmail() +"'").addEntity(UserRegister.class).list();
            userRegister = listUserRegister.get(0);
            session.delete(userRegister);            
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
             return false;
        }       
    }
}
