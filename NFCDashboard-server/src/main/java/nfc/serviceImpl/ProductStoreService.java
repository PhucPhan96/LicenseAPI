/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import nfc.model.Category;
import nfc.model.Product;
import nfc.service.IProductStoreService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Admin
 */
public class ProductStoreService implements IProductStoreService {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
//    public List<Category> getListCategoryBySupId(int suppl_id ) {
//        Session session = this.sessionFactory.getCurrentSession();
//        Transaction trans = session.beginTransaction();
//        Query query = session.createSQLQuery("SELECT pc.cate_id,c.app_id,c.cate_img_id, c.cate_name,c.parent_cate_id,c.created_date,c.cate_seq,c.cate_type, c.suppl_id FROM fg_product_categories pc inner join fg_categories c on pc.cate_id=c.cate_id WHERE pc.prod_id IN (SELECT p.prod_id FROM fg_products p WHERE p.suppl_id = "+suppl_id+")GROUP BY pc.cate_id").addEntity(Category.class);
//        List<Category> listCategory = query.list();
//        trans.commit();         
//        return listCategory;
//    }
//    
    
     public List<Product> getListProductBySupplId(int suppl_id) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Query query = session.createSQLQuery("SELECT * FROM fg_products  WHERE suppl_id =" + suppl_id).addEntity(Product.class);
        List<Product> listProduct = query.list();
        trans.commit();         
        return listProduct;
    }
     
     
    public List<Product> getListProductByCategoryID (int cate_id, int suppl_id ){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Query query = session.createSQLQuery("SELECT * FROM fg_products  WHERE cate_id ="+cate_id+" AND suppl_id="+suppl_id).addEntity(Product.class);
        List<Product> listProduct = query.list();
        trans.commit();         
        return listProduct;
        
    }
//   public boolean updateProductStore (int prod_id,String calculatedDate){
//       Session session = this.sessionFactory.getCurrentSession();
//		Transaction trans = session.beginTransaction();
//		try
//		{
//			Query query = session.createSQLQuery("UPDATE fg_products SET calculatedDate ="+calculatedDate+" WHERE prod_id ="+prod_id );
//                        query.executeUpdate();
//			trans.commit();
//			return true;
//		}
//		catch(Exception ex)
//		{
//			trans.rollback();
//			return false;
//		}
//   }
     
      public boolean updateProductStore(Product product) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
           
            session.update(product);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
   
     
    

    
    
}
