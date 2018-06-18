package nfc.controller;

import nfc.model.AttachFile;
import nfc.model.Category;
import nfc.model.Role;
import nfc.model.ViewModel.CategoryView;
import nfc.model.ViewModel.SupplierProductView;
import nfc.service.ICategoryService;
import nfc.service.IFileService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryManagementController {
    @Autowired
    private ICategoryService categoryDAO;
    @Autowired
    private IFileService fileDAO;
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @RequestMapping(value="category",method=RequestMethod.GET)
    public List<Category> getListCategory(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<Category> category = categoryDAO.getListCategory(username);
        return category;
    }
    
    @RequestMapping(value="category/supplier/{id}",method=RequestMethod.GET)
    public List<Category> getListCategoryOfSupplier(@PathVariable("id") int supplierId){
        List<Category> categories = categoryDAO.getListCategoryOfSupplier(supplierId);
        return categories;
    }
    
    
    @RequestMapping(value="category/{type}", method=RequestMethod.GET)
    public @ResponseBody List<Category> getListCategoryFilterType(@PathVariable("type") String type){
        List<Category> category = categoryDAO.getListCategoryFilterType(type);
        System.out.println(category.size());
        return category;
    }
    @RequestMapping(value="app/category/view/{type}", method=RequestMethod.GET)
    public @ResponseBody List<CategoryView> getListCategoryViewFilterType(@PathVariable("type") String type){
        List<CategoryView> categoryView = categoryDAO.getListCategoryView(type);
        return categoryView;
    }
    @RequestMapping(value="category/add", method=RequestMethod.POST)
    public @ResponseBody String insertCategory(@RequestBody Category cate){
        cate.setApp_id(Utils.appId);
        Date date = new Date();
        cate.setCreated_date(date);
        System.out.println(cate.getCate_name());
        String data = categoryDAO.insertCategory(cate)+"";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="category/edit", method=RequestMethod.PUT)
    public @ResponseBody String editCategory(@RequestBody Category cate){
        String data = categoryDAO.updateCategory(cate)+"";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="category/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteCategory(@PathVariable("id") String cateID){
        String data = categoryDAO.deleteCategory(cateID)+"";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="category/getCate/{id}", method=RequestMethod.GET)
    public Category getCategory(@PathVariable("id") String cateID){
        Category category = categoryDAO.getCategory(cateID);
        return category;
    } 
    @RequestMapping(value="category/getFile/{id}", method=RequestMethod.GET)
    public String getCategory(@PathVariable("id") int fileId){
        System.out.println("FileId " + fileId);
        AttachFile file = fileDAO.getAttachFile(fileId);
        System.out.println(file.getFile_path());
        return Utils.convertObjectToJsonString(file);
    } 
    @RequestMapping(value="app/product/store/{id}", method=RequestMethod.GET)
    public List<SupplierProductView> getProductOfStore(@PathVariable("id") int supplierId){
        List<SupplierProductView> lstSupplierProductView = categoryDAO.getListProductOfCategory(supplierId);
        return lstSupplierProductView;
    } 
    
    
    @RequestMapping(value="category/add/list", method=RequestMethod.POST)
    public @ResponseBody List<Category>  addListCategory(@RequestBody List<Category> categories){
        categoryDAO.addListCategory(categories);
        return categories;
    }

}
