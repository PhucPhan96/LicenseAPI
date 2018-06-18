package nfc.service;

import java.util.List;
import nfc.model.Email;
import nfc.model.Mail;

import nfc.model.SupplierFavorite;

import org.hibernate.Session;

import nfc.model.User;
import nfc.model.UserAddress;

import nfc.model.UserRegister;
import nfc.model.UserRole;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.SupplierView;

public interface IUserService {
	List<User> getListUser();
	List<User> getListUserPermissionStore(String username);
	boolean updateUser(User user);
	boolean insertUser(User user);
	boolean deleteUser(String userId);
	User getUser(String userId);
	User findUserByUserName(String username);
	List<UserRole> getListUserRole(String userId);
	boolean ChangPasswordUser(String userId,String pass,String passSalt);
	List<UserAddress> getListUserAddress(String userId);
	boolean insertUserRegister(UserRegister userRegist);
	boolean updateUserRegister(UserRegister userRegist);
	UserRegister getUserRegister(String email);
	String saveUserRegister(UserRegister userRegist);
	//ForgotPassword - LanAnh
	boolean updateUserForgotPassword(User user);
	User getUserForgotPassword (String email);
	String forgotPassword (User user);
	boolean insertUserLogin(String username);
	public boolean insertUserFb(User user);
        List<User> getListUserOfRole(int roleId);
        String getUserIdOfSupplier(int supplierId);
        List<User> getListUserByPhoneNumber( String phoneNum);
        List<User> getListUserByLikePhone(String phone);
        List<UserAddress> getListUserByAddress(String address);
        List<User> getListUserByLikePhoneAndAddress(String phone, String address);
        List<User> getListUserGrid(GridView gridView);
        long countUserGrid(GridView gridView);
        public List<SupplierFavorite> fGetListSupplierFavoriteByUserId(String userId);       
        UserRegister getUserRegisterByEmail(Email email);
        boolean insertUserApp(User user);
        User getUserByEmail(Email email) ;
        boolean deleteUserResgist(Email email);
        
}
