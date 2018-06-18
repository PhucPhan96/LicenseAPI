/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.util.List;
import nfc.model.BusinessDay;
import nfc.service.IBusinessDayService;
import nfc.service.ICodeService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class BusinessDayService implements IBusinessDayService {
     private SessionFactory sessionFactory;
    @Autowired
    private ICodeService codeDAO;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
      
     @Override
    public List<BusinessDay> getListBusinessDayBySupId(int suppl_id ) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Query query = session.createSQLQuery("select b.* from fg_businessDays b  where b.suppl_id = " + suppl_id).addEntity(BusinessDay.class);
        List<BusinessDay> listBusinessDay = query.list();
        trans.commit();            
        for (BusinessDay businessDay : listBusinessDay) {            
            businessDay.setBusinessDays_reason(codeDAO.getCode("0007", businessDay.getBusinessDays_reason()).getCode_name());
        }
        return listBusinessDay;
    }
   
    public boolean insertBusinessDay(BusinessDay businessDay) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {            
            session.save(businessDay);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
    
    
    public BusinessDay getBusinessDaybyId(int businessDays_id) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(BusinessDay.class);
        criteria.add(Restrictions.eq("businessDays_id", businessDays_id));
        BusinessDay businessDay = (BusinessDay) criteria.uniqueResult();
        trans.commit();
        return businessDay;
    }
    
    public boolean updateBusinessDay(BusinessDay businessDay) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {            
            session.update(businessDay);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
    public boolean deleteBusinessDay(int businessDays_id) {
        BusinessDay businessDay = getBusinessDaybyId(businessDays_id);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.delete(businessDay);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
}
