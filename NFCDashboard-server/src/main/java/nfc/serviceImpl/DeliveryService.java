/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import nfc.model.Delivery;
import nfc.model.DeliveryMeta;
import nfc.model.ViewModel.DeliveryView;
import nfc.service.IDeliveryService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Admin
 */
public class DeliveryService implements IDeliveryService{
    
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }

    public List<Delivery> getDeliveries() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Delivery> deliveries = new ArrayList<>();
        try{
            Criteria criteria = session.createCriteria(Delivery.class);
            deliveries = (List<Delivery>) criteria.list();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return deliveries;
    }   
    
    private Delivery getDelivery(int deliveryId, Session session){
        Criteria criteria = session.createCriteria(Delivery.class);
        criteria.add(Restrictions.eq("delivery_id", deliveryId));
        return (Delivery) criteria.uniqueResult();
    }
    
    private List<DeliveryMeta> getListDeliveryMeta(int delieryId, Session session){
        Criteria criteria = session.createCriteria(DeliveryMeta.class);
        criteria.add(Restrictions.eq("delivery_id", delieryId));
        return (List<DeliveryMeta>) criteria.list();
    }
    
    public DeliveryView getDeliveryView(int delieryId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        DeliveryView deliveryView = new DeliveryView();
        try{
            deliveryView.setDelivery(getDelivery(delieryId, session));
            deliveryView.setDeliveryMetas(getListDeliveryMeta(delieryId, session));
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return deliveryView;
    }
    
    public boolean insertDeliveryView(DeliveryView deliveryView){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            session.save(deliveryView.getDelivery());
            insertDeliveryMeta(deliveryView.getDeliveryMetas(), session, deliveryView.getDelivery().getDelivery_id());
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    private void insertDeliveryMeta(List<DeliveryMeta> deliveryMetas, Session session, int deliveryId){
        for(DeliveryMeta deliveryMeta : deliveryMetas){
            deliveryMeta.setDelivery_id(deliveryId);
            session.save(deliveryMeta);
        }
    }
    
    public boolean updateDeliveryView(DeliveryView deliveryView){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            session.update(deliveryView.getDelivery());
            deleteReferenceOfDelivery(session, deliveryView.getDelivery().getDelivery_id(), "fg_delivery_meta");
            insertDeliveryMeta(deliveryView.getDeliveryMetas(), session, deliveryView.getDelivery().getDelivery_id());
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    private void deleteReferenceOfDelivery(Session session, int paymentId, String table)
    {
        String deleteQuery = "delete from "+table+" where delivery_id = " + paymentId;
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }
    
    public boolean deleteDeliveryView(int paymentId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            deleteReferenceOfDelivery(session, paymentId, "fg_delivery_meta");
            deleteReferenceOfDelivery(session, paymentId, "fg_delivery");
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    public Delivery getDeliveryFromStore(int storeId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Delivery delivery = new Delivery();
        try{
            Query query = session.createSQLQuery("SELECT d.* FROM fg_supplier_work sw inner join fg_delivery d on sw.delivery_id = d.delivery_id where sw.suppl_id = " + storeId + " limit 1")
                            .addEntity(Delivery.class);
            delivery = (Delivery) query.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return delivery;
    }
}
