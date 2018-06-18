/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nfc.messages.request.DiscountRequest;
import nfc.model.Discount;
import nfc.service.IDiscountService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import nfc.serviceImpl.common.Utils;

/**
 *
 * @author Admin
 */
public class DiscountService implements IDiscountService{
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }
    
    public Discount insertDiscount(Discount discount){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            session.save(discount);
            trans.commit();
            return discount;
        }
        catch(Exception ex)
        {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return discount;
        }
    }
    
    public boolean updateDiscount(Discount discount){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            session.update(discount);
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

    public boolean deleteDiscount(String discountId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            String deleteQuery = "delete from fg_discount where discount_id = '" + discountId + "'";
            Query query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
            trans.commit();
            return true;
        }
        catch(Exception ex)
        {
            trans.rollback();
            return false;
        } 
    }
    
    public List<Discount> getListDiscount(DiscountRequest discountRequest){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Discount> discounts = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("SELECT * FROM fg_discount WHERE ((discount_date >= '" + discountRequest.getDateFrom() + "' AND discount_date <= '" + discountRequest.getDateTo()+ "' AND discount_type='TODAY') or discount_type like '%REPEAT') AND suppl_id=" + discountRequest.getSupplierId())
                          .addEntity(Discount.class);
            discounts = (List<Discount>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return discounts;
    }
    
    public Discount getDiscountOfStore(int supplierId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Discount discount = new Discount();
        try{
            Query query = session.createSQLQuery("CALL SP_GetDiscountOfStore(:supplierId)")
                                 .addEntity(Discount.class)
                                 .setParameter("supplierId", supplierId);
            discount = (Discount) query.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("Error " +  ex.getMessage());
            trans.rollback();
        }
        return discount;
    }
    
    /* Lucas: Get Discount by date */
    public Discount fgetDiscountByDate(String supplierId){
        Discount discount = new Discount();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String dateFrom = Utils.getCurrentDateYYYYMMDD() + " 00:00:00";
        String dateTo = Utils.getCurrentDateYYYYMMDD() + " 23:59:59";
        String sqlQuery = "SELECT * FROM 82wafoodgo.fg_discount where suppl_id = ' " + supplierId + "' and discount_date between '" + dateFrom + "' and '" + dateTo + "'";
        try{
            Query query = session.createSQLQuery(sqlQuery).addEntity(Discount.class);
            discount = (Discount) query.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("Error " +  ex.getMessage());
            trans.rollback();
        }
        return discount;
    }
}
