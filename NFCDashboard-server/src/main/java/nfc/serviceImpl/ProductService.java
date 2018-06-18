package nfc.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.AttachFile;
import nfc.model.Code;
import nfc.model.Product;
import nfc.model.ProductAdd;
import nfc.model.ProductCategory;
import nfc.model.ProductImage;
import nfc.model.ProductOptional;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.model.SupplierUser;
import nfc.model.ViewModel.ProductAttachFileView;
import nfc.model.ViewModel.ProductView;
import nfc.service.IFileService;
import nfc.service.IProductService;
import nfc.service.common.ICommonService;
import nfc.serviceImpl.common.Utils;
@Transactional
public class ProductService implements IProductService{
	@Autowired
	private ICommonService commonDAO;
	@Autowired
	private IFileService fileDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	//get list product by Supplier ID
	public List<Product> getListProduct(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
				/*.createAlias("product.attachFiles", "attachFile", Criteria.LEFT_JOIN);*/
		criteria.add(Restrictions.eq("suppl_id",supplId));
		List<Product>  products = (List<Product>) criteria.list();
		trans.commit();
		return products;
	}
	public Product getProduct(int productId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("prod_id",productId));
		Product product = (Product) criteria.uniqueResult();
		trans.commit();
		return product;
	}
	private void insertProductImage(Session session, ProductView productView){
		for(ProductAttachFileView attachFileView : productView.getLstAttachFileView())
        {
			System.out.println("attachFileID " + attachFileView.getAttachFile().getFile_id());
        	ProductImage proImg = new ProductImage();
        	proImg.setImg_id(attachFileView.getAttachFile().getFile_id());
        	proImg.setImg_name(attachFileView.getAttachFile().getFile_name());
        	proImg.setProd_id(productView.getProduct().getProd_id());
        	proImg.setImg_type(attachFileView.getImageType());
        	session.save(proImg);
        }
	}
	private void insertProductCategory(Session session, Product product){
		System.out.println("categoryId " + product.getCate_id());
		ProductCategory proCate = new ProductCategory();
		proCate.setProd_id(product.getProd_id());
		proCate.setCate_id(product.getCate_id());
		session.save(proCate);
	}
	public boolean insertProductView(ProductView productView) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try
            {
                //int productIdDesc = 0;
                productView.getProduct().setApp_id(Utils.appId);
//                Serializable ser = session.save(productView.getProduct());
//                if (ser != null) {
//                    productIdDesc = (Integer) ser;
//                }
                session.save(productView.getProduct());
           // System.out.println("Product Id " + productIdDesc);
            //deleteImagesOfProduct(session, product.getProd_id());
            //productView.getProduct().setProd_id(productIdDesc);
            //insertProductImage(session, product);
            //deleteCategoryProduct(session, product.getProd_id());
                insertProductCategory(session, productView.getProduct());
                insertProductImage(session, productView);
                insertProductAdd(session, productView);
                insertProductOption(session, productView);
                trans.commit();
                return true;
            }
            catch(Exception ex)
            {
                System.out.println("Error " + ex.getMessage());
                trans.rollback();
                return false;
            }
	}
	private void insertProductAdd(Session session, ProductView productView){
            for(ProductAdd productAdd : productView.getLstProductAdd())
            {
                productAdd.setProd_id(productView.getProduct().getProd_id());
                session.save(productAdd);
            }
	}
	private void insertProductOption(Session session, ProductView productView){
            for(Product product : productView.getLstProductOption())
            {
                ProductOptional prodOption = new ProductOptional();
                prodOption.setProd_id(productView.getProduct().getProd_id());
                prodOption.setOption_prod_id(product.getProd_id());
                prodOption.setIs_optional(true);
                session.save(prodOption);
            }
	}
	public List<Product> getProduct(String prodId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("suppl_id", Integer.parseInt(prodId)));
		//Product product = (Product) criteria.uniqueResult();
		List<Product> product = (List<Product>) criteria.list();
		trans.commit();
		return product;
	}
	public List<ProductOptional> getProductOptional(int prodId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(ProductOptional.class);
		criteria.add(Restrictions.eq("prod_id", prodId));
		//Product product = (Product) criteria.uniqueResult();
		List<ProductOptional> products = (List<ProductOptional>) criteria.list();
		trans.commit();
		return products;
	}
	public Product getProducts(String productId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("prod_id", Integer.parseInt(productId)));
		Product product = (Product) criteria.uniqueResult();
		trans.commit();
		return product;
	}
	public boolean updateProductView(ProductView productView) {
                //deleteProductImage(productView.getProduct().getProd_id());
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
                    session.update(productView.getProduct());
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_product_categories");
                    insertProductCategory(session, productView.getProduct());
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_prod_imgs");
                    insertProductImage(session, productView);
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_prod_addition");
                    insertProductAdd(session, productView);
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_prod_optional");
                    insertProductOption(session, productView);
                    trans.commit();
                    return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
        
        public void deleteProductImage(int productId){
            List<ProductImage> productImages = getListProductImage(productId);
            for(ProductImage productImage: productImages){
                fileDAO.deleteAttachFile(productImage.getImg_id());
            }
        }
        
        
	public boolean deleteProductView(List<ProductView> productViews) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try
            {
                for(ProductView productView: productViews)
                {
                    /*for(AttachFile file: product.getAttachFiles())
                    {
                            System.out.println("FileID " + file.getFile_id());
                            session.delete(file);
                    }*/
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_prod_imgs");
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_product_categories");
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_prod_addition");
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_prod_optional");
                    deleteReferenceOfProduct(session, productView.getProduct().getProd_id(), "fg_products");
                    //session.delete(product);

                }
                trans.commit();
                return true;
            }
            catch(Exception ex)
            {
                    trans.rollback();
                    return false;
            } 
	}
	public List<ProductImage> getListProductImage(int productId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(ProductImage.class);
		criteria.add(Restrictions.eq("prod_id",productId));
		List<ProductImage>  productImages = (List<ProductImage>) criteria.list();
		trans.commit();
		return productImages;
	}
	public List<ProductAdd> getListProductAdd(int productId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(ProductAdd.class);
		criteria.add(Restrictions.eq("prod_id",productId));
		List<ProductAdd>  productAdds = (List<ProductAdd>) criteria.list();
		trans.commit();
		return productAdds;
	}
	public List<Product> getListProductOption(int productId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"select p.* from fg_products p inner join fg_prod_optional po on p.prod_id=po.option_prod_id where po.prod_id="+productId)
				.addEntity(Product.class);
		List<Product>  productOps = (List<Product>) query.list();
		trans.commit();
		return productOps;
	}
	private void deleteReferenceOfProduct(Session session, int productId, String table)
	{
		String deleteQuery = "delete from "+table+" where prod_id = " + productId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	public ProductView getProductView(int productId)
	{
            ProductView prodView = new ProductView();
            prodView.setProduct(getProduct(productId));
            List<ProductImage> lstProdImage = getListProductImage(productId);
            List<ProductAttachFileView> lstProdAttachView = new ArrayList<ProductAttachFileView>();
            for(ProductImage prodImage: lstProdImage){
                    ProductAttachFileView proAttachView = new ProductAttachFileView();
                    proAttachView.setImageType(prodImage.getImg_type());
                    proAttachView.setAttachFile(fileDAO.getAttachFile(prodImage.getImg_id()));
                    lstProdAttachView.add(proAttachView);
            }
            prodView.setLstAttachFileView(lstProdAttachView);
            prodView.setLstProductAdd(getListProductAdd(productId));
            prodView.setLstProductOption(getListProductOption(productId));
            return prodView;
	}
	public List<ProductView> getListProductView(int supplId){
		List<ProductView> lstProductView = new ArrayList<ProductView>();
		List<Product> lstProduct = getListProduct(supplId);
		for(Product prod: lstProduct){
			lstProductView.add(getProductView(prod.getProd_id()));
		}
		return lstProductView;
	}
	private Product getProductSupplier(int productId, int supplId) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            Product product = new Product();
            try{
                Criteria criteria = session.createCriteria(Product.class);
                criteria.add(Restrictions.eq("prod_id",productId));
                criteria.add(Restrictions.eq("suppl_id",supplId));
                product = (Product) criteria.uniqueResult();
                trans.commit();
            }
            catch(Exception ex){
                trans.rollback();
            }
            return product;
	}
	public List<Product> getListProductOfCategory(int cateId, int supplId){
		List<Product> lstProduct = new ArrayList<Product>();
		List<ProductCategory> lstProductCategory = getListProductCategory(cateId);
		for(ProductCategory proCate: lstProductCategory){
			Product product = getProductSupplier(proCate.getProd_id() , supplId);
			if(product != null){
				lstProduct.add(getProductSupplier(proCate.getProd_id() , supplId));
			}
		}
		return lstProduct;
	}
	public List<ProductCategory> getListProductCategory(int cateId){
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            Criteria criteria = session.createCriteria(ProductCategory.class);
            criteria.add(Restrictions.eq("cate_id", cateId));
            //Product product = (Product) criteria.uniqueResult();
            List<ProductCategory> lstProductCategory = (List<ProductCategory>) criteria.list();
            trans.commit();
            return lstProductCategory;
	}
        
        public boolean deleteProductView(int productId){
            
            deleteProductImage(productId);
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try
            {
                deleteReferenceOfProduct(session, productId, "fg_prod_imgs");
                deleteReferenceOfProduct(session, productId, "fg_product_categories");
                deleteReferenceOfProduct(session, productId, "fg_prod_addition");
                deleteReferenceOfProduct(session, productId, "fg_prod_optional");
                deleteReferenceOfProduct(session, productId, "fg_products");
                trans.commit();
                return true;
            }
            catch(Exception ex)
            {
                    trans.rollback();
                    return false;
            } 
        }
	
        
        public boolean insertListProductView(List<ProductView> productViews){
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try
            {
                for(ProductView productView: productViews){
                    productView.getProduct().setApp_id(Utils.appId);
                    session.save(productView.getProduct());
                    insertProductCategory(session, productView.getProduct());
                    insertProductImage(session, productView);
                    insertProductAdd(session, productView);
                    //insertProductOption(session, productView);
                }
                trans.commit();
                return true;
            }
            catch(Exception ex)
            {
                System.out.println("Error " + ex.getMessage());
                trans.rollback();
                return false;
            }
        }
        
        
        public List<Product> getListProductOfCategory(int cateId) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            List<Product>  products = new ArrayList<>();
            try{
                Criteria criteria = session.createCriteria(Product.class);
                            /*.createAlias("product.attachFiles", "attachFile", Criteria.LEFT_JOIN);*/
                criteria.add(Restrictions.eq("cate_id",cateId));
                products = (List<Product>) criteria.list();
                trans.commit();
            }
            catch(Exception ex){
                trans.rollback();
            }
            return products;
	}
        
        public List<ProductView> getListProductViewOfCategory(int cateId){
            List<ProductView> lstProductView = new ArrayList<ProductView>();
            List<Product> lstProduct = getListProductOfCategory(cateId);
            for(Product prod: lstProduct){
                    lstProductView.add(getProductView(prod.getProd_id()));
            }
            return lstProductView;
        }
}
