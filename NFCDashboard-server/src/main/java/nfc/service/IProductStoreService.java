/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.Category;
import nfc.model.Product;

/**
 *
 * @author Admin
 */
public interface IProductStoreService {
    //List<Category> getListCategoryBySupId(int suppl_id );
    List<Product> getListProductBySupplId(int suppl_id);
    List<Product> getListProductByCategoryID (int cate_id, int suppl_id );
    boolean updateProductStore(Product product);
}
