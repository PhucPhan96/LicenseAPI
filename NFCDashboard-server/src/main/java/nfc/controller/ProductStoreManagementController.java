/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.model.Category;
import nfc.model.Product;
import nfc.service.IProductStoreService;
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
public class ProductStoreManagementController {
    @Autowired
    private IProductStoreService productStoreDAO;
    
    @RequestMapping(value = "productStores/getListProductBySupplId/{suppl_id}", method = RequestMethod.GET)
    public List<Product> getListProductBySupplId(@PathVariable("suppl_id") int suppl_id) {   
        List<Product> products = productStoreDAO.getListProductBySupplId(suppl_id);
        return products;
    }
    
//    @RequestMapping(value = "productStores/getListCategoryBySupId/{suppl_id}", method = RequestMethod.GET)
//    public List<Category> getListCategoryBySupId(@PathVariable("suppl_id") int suppl_id) {   
//        List<Category> categories = productStoreDAO.getListCategoryBySupId(suppl_id);
//        return categories;
//    }
    
    @RequestMapping(value = "productStores/getListProductByCategoryID/{cate_id}/{suppl_id}", method = RequestMethod.GET)
    public List<Product> getListProductByCategoryID(@PathVariable("cate_id") int cate_id,@PathVariable("suppl_id") int suppl_id) {   
        List<Product> products = productStoreDAO.getListProductByCategoryID(cate_id,suppl_id);
        System.out.print(productStoreDAO.getListProductByCategoryID(cate_id,suppl_id));
        return products;
    }
    
    @RequestMapping(value="productStores/updateProductStore", method=RequestMethod.PUT)
    public @ResponseBody String updateProductStore(@RequestBody Product product){
        System.out.print("Vao Update updateProductStore" +product);
        String data = productStoreDAO.updateProductStore(product) + "";          
        return "{\"result\":\"" + data + "\"}";
    }
}
