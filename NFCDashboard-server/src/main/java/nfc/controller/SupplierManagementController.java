package nfc.controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import nfc.model.Code;
import nfc.model.Mail;
import nfc.model.PKModel.SupplierUserPK;
import nfc.model.Product;
import nfc.model.Supplier;
import nfc.model.SupplierFavorite;
import nfc.model.SupplierUser;
import nfc.model.ViewModel.BillHistory;
import nfc.model.ViewModel.SupplierAppView;
import nfc.model.ViewModel.SupplierView;
import nfc.service.ICodeService;
import nfc.service.ISupplierService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;
import nfc.model.Search;
import nfc.model.User;
import nfc.model.ViewModel.BillHistoryView;
import nfc.model.ViewModel.ProductOptionalBH;
import nfc.model.ViewModel.UserSupplierView;
import nfc.service.IMailService;
import nfc.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplierManagementController {

    @Autowired
    private ISupplierService supplierDAO;
    @Autowired
    private ICodeService codeDAO;
    @Value("Authorization")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IMailService mailDAO;

    @Autowired
    private IUserService userDAO;

    @RequestMapping(value = "supplier", method = RequestMethod.GET)
    public List<Supplier> getSupplier() {
        List<Supplier> supplier = supplierDAO.getListSupplier();
        return supplier;
    }

    @RequestMapping(value = "stores", method = RequestMethod.GET)
    public List<Supplier> getListStore() {
        List<Supplier> stores = supplierDAO.getListStore();
        return stores;
    }

    @RequestMapping(value = "supplier/manage/{roleId}", method = RequestMethod.GET)
    public List<Supplier> getSupplierManage(@PathVariable("roleId") int roleId) {
        List<Supplier> supplier = supplierDAO.getListSupplierManage(roleId);
        return supplier;
    }

    @RequestMapping(value = "supplier/view", method = RequestMethod.GET)
    public List<SupplierView> getListSupplierView(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<SupplierView> lstSupplierView = supplierDAO.getListSupplierView(username);
        //System.out.println(Utils.convertObjectToJsonString(lstSupplierView));
        return lstSupplierView;
    }

    @RequestMapping(value = "manage/suppliers/{supplierId}", method = RequestMethod.GET)
    public List<Supplier> getListSupplierViewOfManage(@PathVariable("supplierId") int supplierId) {
        List<Supplier> lstSupplier = supplierDAO.getListSupplierOfManage(supplierId);
        return lstSupplier;
    }

    @RequestMapping(value = "role/suppliers/{roleId}", method = RequestMethod.GET)
    public List<Supplier> getListSupplierViewOfRole(@PathVariable("roleId") int roleId) {
        List<Supplier> lstSupplier = supplierDAO.getListSupplierOfRole(roleId);
        return lstSupplier;
    }

    @RequestMapping(value = "supplier/view/manage/{supplierId}", method = RequestMethod.GET)
    public List<SupplierView> getListSupplierOfManage(@PathVariable("supplierId") int supplierId) {
        List<SupplierView> lstSupplierView = supplierDAO.getListSupplierViewOfManage(supplierId);
        return lstSupplierView;
    }

    @RequestMapping(value = "supplier/view/role/{roleId}", method = RequestMethod.GET)
    public List<SupplierView> getListSupplierOfRole(@PathVariable("roleId") int roleId) {
        List<SupplierView> lstSupplierView = supplierDAO.getListSupplierViewOfRole(roleId);
        return lstSupplierView;
    }

    @RequestMapping(value = "app/supplier/{id}/{type}/{pageindex}/{pagesize}", method = RequestMethod.GET)
    public List<SupplierAppView> getListSupplierViewOfCategory(
            @PathVariable("id") String categoryId,
            @PathVariable("type") String storeType,
            @PathVariable("pageindex") int pageindex,
            @PathVariable("pagesize") int pagesize) {
        System.out.println("Type: " + storeType);
        List<SupplierAppView> lstSupplierView = supplierDAO.getListSupplierViewOfCategory(categoryId, storeType, pageindex, pagesize);
        return lstSupplierView;
    }

    @RequestMapping(value = "supplier/user", method = RequestMethod.GET)
    public Supplier getListSupplierUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Supplier supplier = supplierDAO.getSupplierFromUser(username);
        return supplier;
    }

    @RequestMapping(value = "supplier/{id}", method = RequestMethod.GET)
    public Supplier getSupplier(@PathVariable("id") String supplId) {
        Supplier supplier = supplierDAO.getSupplier(supplId);
        return supplier;
    }

    @RequestMapping(value = "app/supplierFavorite/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String getSupplierFavorite(@PathVariable("id") int supplId) {
        String supplierFavorite = supplierDAO.getSupplierFavorite(supplId);
        return "{\"result\":\"" + supplierFavorite + "\"}";
    }

    // get supplier view
    @RequestMapping(value = "supplier/detail/{id}", method = RequestMethod.GET)
    public SupplierView getSupplierView(@PathVariable("id") String supplId, HttpServletRequest request) {
        SupplierView supplierView = supplierDAO.getSupplierView(Integer.parseInt(supplId));
        System.out.println("supplierViewStr" + supplierView);
        return supplierView;
    }
    // get supplier view by Lucas

    @RequestMapping(value = "supplier/detail1/{id}", method = RequestMethod.GET)
    public SupplierView getSupplierView1(@PathVariable("id") String supplId, HttpServletRequest request) {
        SupplierView supplierView = supplierDAO.getSupplierView1(Integer.parseInt(supplId));
        System.out.println("supplierViewStr" + supplierView);
        return supplierView;
    }

    @RequestMapping(value = "supplier/add", method = RequestMethod.POST)
    public @ResponseBody
    String insertSupplier(@RequestBody SupplierView supplierView, HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        String data = supplierDAO.insertSupplierView(supplierView, username) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "supplier/edit", method = RequestMethod.PUT)
    public @ResponseBody
    String updateSupplier(@RequestBody SupplierView supplierView) {
        String data = supplierDAO.updateSupplierView(supplierView) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "supplier/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteRole(@PathVariable("id") String supplierId, HttpServletRequest request) {
        System.out.println("Vao delete " + supplierId);
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        String data = supplierDAO.deleteSupplierView(Integer.parseInt(supplierId), username) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "app/supplierFavorite/add/{supplierID}/{userID}", method = RequestMethod.POST)
    public @ResponseBody
    String insertSupplierFavorite(@PathVariable("supplierID") int supplierId,
            @PathVariable("userID") String userId) {
        String data = supplierDAO.insertSupplierFavorite(supplierId, userId) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value = "app/isSupplierFavorite/{id}", method = RequestMethod.GET)
    public SupplierFavorite isSupplierFavorite(@PathVariable("id") String userId) {
        SupplierFavorite supplierFavorite = supplierDAO.isSupplierFavorite(userId);
        return supplierFavorite;
    }

    @RequestMapping(value = "app/deleteSupplierFavorite/{supplierID}/{userID}", method = RequestMethod.POST)
    public String deleteSupplierFavorite(@PathVariable("supplierID") int supplId,
            @PathVariable("userID") String userId) {
        System.out.println("Vao delete " + supplId);
        System.out.println("Vao delete " + userId);
        String data = supplierDAO.deleteSupplierFavorite(supplId, userId) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    // getSupplierView2
    @RequestMapping(value = "app/supplier/detail2/{id}", method = RequestMethod.GET)
    public SupplierView getSupplierView2(@PathVariable("id") String supplId, HttpServletRequest request) {
        /*
		 * String token = request.getHeader(tokenHeader); String username =
		 * jwtTokenUtil.getUsernameFromToken(token);
         */
        SupplierView supplierView = supplierDAO.getSupplierView2(Integer.parseInt(supplId));
        System.out.println("supplierViewStr" + supplierView);
        return supplierView;
    }

    @RequestMapping(value = "supplier/store", method = RequestMethod.GET)
    public SupplierUser checkUserIsStore(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        SupplierUser supplierUser = supplierDAO.checkUserIsStore(username);
        if (supplierUser == null) {
            return new SupplierUser();
        }
        return supplierUser;
    }

    @RequestMapping(value = "supplier/code", method = RequestMethod.GET)
    public List<Code> getCodeRankSupplier() {
        List<Code> codes = codeDAO.getListCode("0001");
        return codes;
    }

    // Get List Favorite Store for User
    @RequestMapping(value = "app/supplierFavoriteByUser/{userID}", method = RequestMethod.GET)
    public @ResponseBody
    List<Supplier> getListSupplierFavoriteByUser(@PathVariable("userID") String userID) {
        System.out.println("run getListSupplierFavoriteByUser");
        List<Supplier> listSuplier = supplierDAO.getListSupplierFavoriteByUser(userID);
        return listSuplier;
    }

    // Delete Favorite Store
    @RequestMapping(value = "app/deleteSupplierFavorite/{supplId}", method = RequestMethod.DELETE)
    public String deleteFavoriteSupplier(@PathVariable("supplId") int supplId) {
        System.out.println("Vao delete " + supplId);
        String data = supplierDAO.deleteStoreFavorite(supplId);
        System.out.println("result " + data);
        return "{\"result\":\"" + data + "\"}";

    }

    //Get List Bill History
    @RequestMapping(value = "app/billHistory/{userID}", method = RequestMethod.GET)
    public @ResponseBody
    List<BillHistoryView> getListBillHistory(@PathVariable("userID") String userID) {
        System.out.println("run getListBillHistory");
        List<BillHistoryView> listBillHistoryView = supplierDAO.getListBillHistory(userID);
        return listBillHistoryView;
    }

    //Get List Search Bill History
    @RequestMapping(value = "app/searchBillHistory/{userID}/{dateFrom}/{dateTo}", method = RequestMethod.GET)
    public @ResponseBody
    List<BillHistoryView> getListSearchBillHistory(@PathVariable("userID") String userID, @PathVariable("dateFrom") String dateFrom, @PathVariable("dateTo") String dateTo) {
        List<BillHistoryView> listBillHistory = supplierDAO.getListSearchBillHistory(userID, dateFrom, dateTo);
        System.out.println("show list search BillHistory" + listBillHistory.size());
        return listBillHistory;
    }

    @RequestMapping(value = "/suppliers/roles/{rolejoin}", method = RequestMethod.GET)
    public List<Supplier> getListUserOfRole(@PathVariable("rolejoin") String roleJoin) {
        List<Supplier> suppliers = supplierDAO.getListSupplierFromRoles(roleJoin);
        return suppliers;
    }

    //Lucas
    @RequestMapping(value = "supplier/changeOrderPhoneNumber", method = RequestMethod.POST)
    public @ResponseBody
    String ChangePasswordUser(@RequestBody String[] temp) {
        String data = supplierDAO.ChangeOrderPhoneNumberSupplier(temp[0], temp[1]) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    //Lucas
    @RequestMapping(value = "app/suppliers/getlstbylocation/{long}/{lat}", method = RequestMethod.GET)
    public List<SupplierView> getListSupplierByLocation(@PathVariable("long") String longT, @PathVariable("lat") String lat) {
        List<SupplierView> suppliers = supplierDAO.getListSupplierByAddress(longT, lat);
        return suppliers;
    }

    @RequestMapping(value = "app/suppliers/searchsupplierbytextinput", method = RequestMethod.POST)
    public List<SupplierView> getListSupplierByTextInput(@RequestBody Search search) throws UnsupportedEncodingException {
        List<SupplierView> lstsuppliers = supplierDAO.getListSupplierViewByTextInput(search.getText());
        return lstsuppliers;
    }

    @RequestMapping(value = "app/suppliers/getlstsupplierbymanagerid/{id}", method = RequestMethod.GET)
    public List<Supplier> getListSupplierByManagerId(@PathVariable("id") int id) {
        List<Supplier> lstSupplier = supplierDAO.fGetListSupplierFromSuppIDManager(id);
        return lstSupplier;
    }

    @RequestMapping(value = "supplier/getlistupplierfromtoken", method = RequestMethod.GET)
    public List<Supplier> getListSupplierByUserName(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<Supplier> lstSupplier = supplierDAO.fGetListSupplierFromUserName(username);
        return lstSupplier;
    }
    //Get List Product Optional

    @RequestMapping(value = "app/getListProductOptions/{stringList}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProductOptionalBH> getListProductOptions(@PathVariable("stringList") String stringList) {
        List<ProductOptionalBH> listProductOptional = supplierDAO.getListProductOptions(stringList);
        System.out.println("show list search BillHistory" + listProductOptional.size());
        return listProductOptional;
    }

    @RequestMapping(value = "app/user/getlistfavorite", method = RequestMethod.GET)
    public List<SupplierView> getListSupplierFavorite(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User users = userDAO.findUserByUserName(username);
        String userid = users.getUser_id();
        List<SupplierFavorite> lstSupplierFavo = userDAO.fGetListSupplierFavoriteByUserId(userid);
        List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();
        for (SupplierFavorite supplier : lstSupplierFavo) {
            SupplierView supplierView = new SupplierView();
            supplierView = supplierDAO.getSupplierView(supplier.getSuppl_id());
            lstSupplierView.add(supplierView);
        }
        return lstSupplierView;
    }

    @RequestMapping(value = "/suppliers/child/{userId}", method = RequestMethod.GET)
    public List<Supplier> getListSupplierChildOfUser(@PathVariable("userId") String userId) {
        List<Supplier> suppliers = supplierDAO.getListSupplierChildFromUser(userId);
        return suppliers;
    }

    @RequestMapping(value = "/suppliers/supplierIds/{supplierIds}", method = RequestMethod.GET)
    public List<Supplier> getListSupplierFromSupplierIds(@PathVariable("supplierIds") String supplierIds) {
        List<Supplier> suppliers = supplierDAO.getListSupplierFromSupplierIds(supplierIds);
        return suppliers;
    }

    @RequestMapping(value = "user/supplier/add", method = RequestMethod.POST)
    public @ResponseBody
    Supplier insertUserSupplier(@RequestBody UserSupplierView userSupplierView, HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return supplierDAO.insertUserSupplierView(userSupplierView, username);
    }
    
    @RequestMapping(value = "store/update/information", method = RequestMethod.POST)
    public @ResponseBody String updateStoreInformation(@RequestBody SupplierView supplierView) {
        String data = supplierDAO.updateStoreInformation(supplierView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    //Caculate average stars
    @RequestMapping(value = "app/caculateAverageStar/{supplId}", method = RequestMethod.POST)
    public @ResponseBody int caculateAverageStar(@PathVariable("supplId") int supplId) {
        int data = supplierDAO.caculateAverageStar(supplId);
        return data;
    }
    
    
    
}
