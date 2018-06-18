/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nfc.model.Payment;
import nfc.model.PaymentMeta;
import nfc.model.ViewModel.PaymentView;
import nfc.service.IPaymentService;
import nfc.serviceImpl.common.NFCHttpClient;
import nfc.serviceImpl.common.SpeedPayInformation;
import org.apache.commons.httpclient.NameValuePair;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public class PaymentService implements IPaymentService{
    
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }

    public List<Payment> getPayments() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Payment> payments = new ArrayList<>();
        try{
            Criteria criteria = session.createCriteria(Payment.class);
            payments = (List<Payment>) criteria.list();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return payments;
    }   
    
    private Payment getPayment(int paymentId, Session session){
        Criteria criteria = session.createCriteria(Payment.class);
        criteria.add(Restrictions.eq("payment_id", paymentId));
        return (Payment) criteria.uniqueResult();
    }
    
    private List<PaymentMeta> getListPaymentMeta(int paymentId, Session session){
        Criteria criteria = session.createCriteria(PaymentMeta.class);
        criteria.add(Restrictions.eq("payment_id", paymentId));
        return (List<PaymentMeta>) criteria.list();
    }
    
    public PaymentView getPaymentView(int paymentId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        PaymentView paymentView = new PaymentView();
        try{
            paymentView.setPayment(getPayment(paymentId, session));
            paymentView.setPaymentMetas(getListPaymentMeta(paymentId, session));
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return paymentView;
    }
    
    public boolean insertPaymentView(PaymentView paymentView){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            session.save(paymentView.getPayment());
            insertPaymentMeta(paymentView.getPaymentMetas(), session, paymentView.getPayment().getPayment_id());
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    private void insertPaymentMeta(List<PaymentMeta> paymentMetas, Session session, int paymentId){
        for(PaymentMeta paymentMeta : paymentMetas){
            paymentMeta.setPayment_id(paymentId);
            session.save(paymentMeta);
        }
    }
    
    public boolean updatePaymentView(PaymentView paymentView){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            session.update(paymentView.getPayment());
            deleteReferenceOfPayment(session, paymentView.getPayment().getPayment_id(), "fg_payment_meta");
            insertPaymentMeta(paymentView.getPaymentMetas(), session, paymentView.getPayment().getPayment_id());
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    private void deleteReferenceOfPayment(Session session, int paymentId, String table)
    {
        String deleteQuery = "delete from "+table+" where payment_id = " + paymentId;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }
    
    public boolean deletePaymentView(int paymentId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            deleteReferenceOfPayment(session, paymentId, "fg_payment_meta");
            deleteReferenceOfPayment(session, paymentId, "fg_payments");
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    public boolean updatePaymentDefault(int paymentId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            String updateQuery = "update fg_payments set is_default = false where payment_id != " + paymentId;
            Query query = session.createSQLQuery(updateQuery);
            query.executeUpdate();
            updateQuery = "update fg_payments set is_default = true where payment_id = " + paymentId;
            Query queryUpdatePayment = session.createSQLQuery(updateQuery);
            queryUpdatePayment.executeUpdate();
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    
}
