package nfc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;

import nfc.model.Category;
import nfc.model.User;
import nfc.service.IUserService;
import nfc.serviceImpl.Security.JwtAuthenticationResponse;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;

import java.util.UUID;
import nfc.model.Email;

import nfc.model.ViewModel.GridView;
import org.json.simple.JSONArray;
import nfc.model.SupplierFavorite;

import nfc.model.UserRegister;
import nfc.model.ViewModel.SupplierView;
import nfc.model.ViewModel.UserAddressView;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {

    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IUserService userDAO;
    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public List<User> getListUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<User> users = userDAO.getListUserPermissionStore(username);
        return users;
    }

    @RequestMapping(value = "user/all", method = RequestMethod.GET)
    public List<User> getAllUser() {
        List<User> users = userDAO.getListUser();
        return users;
    }

    @RequestMapping(value = "/users/role/{id}", method = RequestMethod.GET)
    public List<User> getListUserOfRole(@PathVariable("id") int roleId) {
        List<User> users = userDAO.getListUserOfRole(roleId);
        return users;
    }

    @RequestMapping(value = "app/user", method = RequestMethod.GET)
    public List<User> getListUserApp(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<User> users = userDAO.getListUser();
        //User user = userDAO.findUserByUserName(username);
        return users;
        //return Utils.convertObjectToJsonString(users);
    }

    @RequestMapping(value = "app/userOne", method = RequestMethod.GET)
    public JSONObject getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User users = userDAO.findUserByUserName(username);
        String userid = users.getUser_id();
        String userName = users.getUser_name();
        String address = "";
        List<UserAddressView> lstAddressView = users.getLstuserAddress();
        for (UserAddressView addressView:  lstAddressView) {
            address = addressView.getAddressOfUser().getAddress();
        }
        String phone = users.getPhone_no();
        String lastname = users.getLast_name();
        String firstname = users.getFirst_name();
        String midname = users.getMiddle_name();
        String fullname = firstname + midname + lastname;
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userid", userid);
        jsonObj.put("userName", fullname);
        jsonObj.put("address", address);
        jsonObj.put("phone", phone);
        //return Utils.convertObjectToJsonString(users);
        return jsonObj;
        //return Utils.convertObjectToJsonString(jsonObj);
        //return "{\"result\":\"" + userid +  "\"}";
    }

    @RequestMapping(value = "user/detail/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") String userId) {
        User users = userDAO.getUser(userId);
        return users;
    }

    @RequestMapping(value = "user/find/{id}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable("id") String username) {
        User user = userDAO.findUserByUserName(username);
        if (user == null) {
            return new User();
        }
        return user;
    }

    @RequestMapping(value = "app/user/{id}", method = RequestMethod.GET)
    public User getUserApp(@PathVariable("id") String userId) {
        User users = userDAO.getUser(userId);
        return users;
    }

    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    public @ResponseBody
    String insertUser(@RequestBody User user) {
//        user.setApp_id(Utils.appId);
//        UUID uuid = UUID.randomUUID();
//        String randomUUID = uuid.toString();
//        user.setUser_id(randomUUID);
//        Date date = new Date();
//        user.setCreated_date(date);
//        System.out.println("vao insert");
//        System.out.println(user.getLstRoles().size());

        String data = userDAO.insertUser(user) + "";
        //String data = "";
        return "{\"result\":\"" + data + "\"}";
    }


    @RequestMapping(value="/users/role",method=RequestMethod.POST)
    public GridView getListUserOfRole(@RequestBody GridView gridData){
        gridData.setCount(userDAO.countUserGrid(gridData));
        List<User>  users = userDAO.getListUserGrid(gridData);
        JSONArray response = new JSONArray();
        for(User user: users){
            JSONObject object = new JSONObject();
            object.put("supplier_name", user.getSupplier_names());
            object.put("mobile_no", user.getMobile_no());
            object.put("user_name", user.getUser_name());
            object.put("email", user.getEmail());
            object.put("created_date", Utils.convertDateToString(user.getCreated_date()));
            object.put("user_id", user.getUser_id());
            response.add(object);
        }
        gridData.setResponse(new ArrayList<>(response));
        return gridData;
    }

    @RequestMapping(value="app/user/addUserFB", method=RequestMethod.POST)
    public @ResponseBody  String insertUserFacebook(@RequestBody User user){	
        user.setApp_id(Utils.appId);
        UUID uuid = UUID.randomUUID();
        String randomUUID = uuid.toString();
        user.setUser_id(randomUUID);
        Date date = new Date();
        user.setCreated_date(date);

        boolean data = userDAO.insertUserFb(user);
        System.out.println(data);
        if(data){
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUser_name());
            final String token = jwtTokenUtil.generateToken(userDetails);
            System.out.println(token);
            return "{\"result\":\"" + token + "\"}";
        } else {
            //data = "false";
            String data1 = userDAO.insertUserFb(user) + "";
            return "{\"result\":\"" + data1 + "\"}";
        }
    }

    @RequestMapping(value = "app/user/checkUserFB/{id}", method = RequestMethod.GET)
    public String checkUserFacebook(@PathVariable("id") String username) {
        User users = userDAO.findUserByUserName(username);// .getUser(userId);	
        try {
            if (users != null) {
                if (users.getUser_name().equals(username)) {
                    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    final String token = jwtTokenUtil.generateToken(userDetails);
                    return "{\"result\":\"" + token + "\"}";
                } else {
                    String data = "false";
                    return "{\"result\":\"" + data + "\"}";
                }
            } else {
                String data = "false";
                return "{\"result\":\"" + data + "\"}";
            }

        } catch (Exception ex) {
            String data = "false";
            return "{\"result\":\"" + data + "\"}";
        }
    }

    @RequestMapping(value = "user/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteUser(@PathVariable("id") String userId) {
        String data = userDAO.deleteUser(userId) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "user/update", method = RequestMethod.PUT)
    public @ResponseBody
    String updateUser(@RequestBody User user) {
        System.out.println("vao duoc update user");
        String data = userDAO.updateUser(user) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "user/changePassword", method = RequestMethod.POST)
    public @ResponseBody
    String ChangePasswordUser(@RequestBody String[] temp) {
        System.out.println("vao change password: Pass:  " + temp[0]);
        System.out.println("vao change password: Id: " + temp[1]);
        System.out.println("vao change password: Id: " + temp[2]);
        String data = userDAO.ChangPasswordUser(temp[1], temp[0], temp[2]) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    /**
     * Lucas 
         *
     */
    @RequestMapping(value = "user/findbyphone/{id}", method = RequestMethod.GET)
    public List<User> getListUserByPhone(@PathVariable("id") String phoneNum) {
        List<User> lstUser = userDAO.getListUserByPhoneNumber(phoneNum);
        if (lstUser == null) {
            List<User> list = new ArrayList<>();
            return list;
        }
        return lstUser;
    }
    /**
     * Lucas 
     */
    @RequestMapping(value = "app/user/finduseridbysupplier/{id}", method = RequestMethod.GET)
    public String getUserIDBySupplier(@PathVariable("id") int id) {
        String userID = userDAO.getUserIdOfSupplier(id) + "";
        return "{\"result\":\"" + userID + "\"}";
    }

//    @RequestMapping(value = "app/user/getlistfavorite", method = RequestMethod.GET)
//    public List<SupplierView> getListSupplierFavorite(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//        User users = userDAO.findUserByUserName(username);
//        String userid = users.getUser_id();
//        List<SupplierFavorite> lstSupplierFavo = userDAO.fGetListSupplierFavoriteByUserId(userid);
//        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
//        for (SupplierFavorite supplier: lstSupplierFavo) {
//            SupplierView supplierView = new SupplierView();
//            supplierView = supplDAO.getSupplierView(supplier.getSuppl_id());
//            lstSupplierView.add(supplierView);
//        }
//        return lstSupplierView;
//    }
    
    
    //LanAnh
    
    @RequestMapping(value = "app/user/insertUserRegister", method = RequestMethod.POST)
    public @ResponseBody
    String insertUserRegister(@RequestBody UserRegister userRegist) {
      
        Date date = new Date();
        userRegist.setReq_time(date);
        System.out.println("vao insertUserRegister"); 
        String data = userDAO.insertUserRegister(userRegist) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value = "app/user/getUserRegisterByEmail", method = RequestMethod.POST)
    public UserRegister getUserRegisterByEmail(@RequestBody Email email) {
        System.out.println("Email la"+email.getEmail()); 
        UserRegister userRegist = userDAO.getUserRegisterByEmail(email);
        System.out.println("vao getUserRegisterByEmail"+userRegist); 
        return userRegist;
    }
    @RequestMapping(value = "app/user/insertUserApp", method = RequestMethod.POST)
    public @ResponseBody
    String insertUserApp(@RequestBody User user) {       
        System.out.println("vao insertUserApp");     
        String data = userDAO.insertUserApp(user) + "";
        //String data = "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value = "app/user/updateUserRegister", method = RequestMethod.PUT)
    public @ResponseBody
    String updateUserRegister(@RequestBody UserRegister userRegister) {
        System.out.println("vao duoc update user");
        //userRegister.setReq_approved(true);
        String data = userDAO.updateUserRegister(userRegister) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value = "app/user/deleteUserResgist", method = RequestMethod.POST)
    public @ResponseBody
    String deleteUser(@RequestBody Email email) {
        System.out.println("OMG");
        String data = userDAO.deleteUserResgist(email) + "";
        return "{\"result\":\"" + data + "\"}";
    }
}
