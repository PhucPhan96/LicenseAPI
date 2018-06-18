/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import nfc.model.KeySingle;
import nfc.service.IKeySingle;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author PhucP
 */
public class KeySingleService implements IKeySingle {

    @Autowired
    private SessionFactory sessionFactory;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    
    boolean compareWithToday(Date date1, Date date2)
    {
        if(date1.compareTo(date2) > 0)
            return true;
        else
            return false;
    }

//    Get All Key
    
    public List<KeySingle> fGetAllKeySingle() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<KeySingle> list;
        try {
            Criteria criteria = session.createCriteria(KeySingle.class);
            list = (List<KeySingle>) criteria.list();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getStart_date());
            }
            trans.commit();
            return list;
        } catch (Exception e) {
            trans.rollback();
        }
        return null;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    Generate Key
    
    protected String getKeySingle() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    
//    Search Key By Key

    protected KeySingle findKeyByKey_Single(String key) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            Criteria criteria = session.createCriteria(KeySingle.class);
            criteria.add(Restrictions.eq("key_single", key));
//            List<KeySingle> list = new ArrayList<>();
            KeySingle keysingle = (KeySingle) criteria.uniqueResult();
            trans.commit();
        return keysingle;
        } catch (Exception e) {
            trans.rollback();
        }
        return new KeySingle();
    }
    
//    Generate 500 key single

    public boolean GenerateKeySingle() {
        for (int i = 0; i < 500; i++) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            String key = getKeySingle();
            
            try {
                Criteria criteria = session.createCriteria(KeySingle.class);
                criteria.add(Restrictions.eq("key_single", key));
                List<KeySingle> list = new ArrayList<>();
                list = (List<KeySingle>) criteria.list();
                System.out.print(list.size());
                if (list.size() == 0) {
                 
                KeySingle ks = new KeySingle();
                ks.setKey_single(key);
                ks.setStatus_key("noactive");
                session.save(ks);
                trans.commit();
            }
            } catch (Exception e) {
                trans.rollback();
            }
        }
        return true;
    }
    
//    Generate Key By Quantity
    
    public boolean GenerateKeySingleByNumber(int num) {
        for (int i = 0; i < num; i++) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            String key = getKeySingle();
            
            try {
                Criteria criteria = session.createCriteria(KeySingle.class);
                criteria.add(Restrictions.eq("key_single", key));
                List<KeySingle> list = new ArrayList<>();
                list = (List<KeySingle>) criteria.list();
    //            trans.commit();
                System.out.print(list.size());
                if (list.size() == 0) {
                KeySingle ks = new KeySingle();
                ks.setKey_single(key);
                ks.setStatus_key("noactive");
                session.save(ks);
                trans.commit();
            }
            } catch (Exception e) {
                trans.rollback();
            }
        }
        return true;
    }

//    Check Key
    
    public boolean FindByKey(String key_single, String main, String cpu) {
        
        KeySingle keysingle = findKeyByKey_Single(key_single);
        System.out.println(keysingle.getKey_single());
        if (keysingle.getKey_single().equalsIgnoreCase(key_single)) {
            if (keysingle.getEx_main() == null && keysingle.getEx_cpu() == null) {
                Session session = this.sessionFactory.getCurrentSession();
                Transaction trans = session.beginTransaction();
                try {
                    String strQuery = "update keysingle set ex_main = '" + main + "', ex_cpu = '" + cpu + "', start_date = '" + LocalDate.now() + "', end_date = '" + LocalDate.now().plusDays(365) + "', key_log = " + 365 + ", status_key = 'inactive'  where id = " + keysingle.getId();
                    System.out.println(strQuery);
                    session.createSQLQuery(strQuery).executeUpdate();
                    trans.commit();
                    return true;
                } catch (Exception e) {
                    trans.rollback();
                    System.out.println("Error " + e.getMessage());
                    return false;
                }
            }
            else try {
                if(keysingle.getEx_main().equalsIgnoreCase(main) && keysingle.getEx_cpu().equalsIgnoreCase(cpu) && compareWithToday(keysingle.getEnd_date(), new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString())) == true)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            } catch (ParseException ex) {
                Logger.getLogger(KeySingleService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
//    Get All Key Actived
    
    public List<KeySingle> GetFilterKeyActive()
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            Criteria criteria = session.createCriteria(KeySingle.class);
            criteria.add(Restrictions.eq("status_key", "inactive"));
            List<KeySingle> list = (List<KeySingle>) criteria.list();
            trans.commit();
            return list;
        } catch (Exception e) {
            trans.rollback();
        }
        return null;
    }
    
//    Search Key
    
    public KeySingle SearchKey(String key)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            Criteria criteria = session.createCriteria(KeySingle.class);
            criteria.add(Restrictions.eq("key_single", key));
            KeySingle key_single = (KeySingle) criteria.uniqueResult();
            trans.commit();
            return key_single;
        } catch (Exception e) {
            trans.rollback();
        }
        return new KeySingle();
    }
    
//    Reset Key
    
    public boolean ResetKey(String key){
        KeySingle keysingle = findKeyByKey_Single(key);
        if (keysingle.getKey_single().equalsIgnoreCase(key))
        {
            Session session = this.sessionFactory.getCurrentSession();
                Transaction trans = session.beginTransaction();
                try {
                    String strQuery = "update keysingle set ex_main = null, ex_cpu = null, start_date = null, end_date = null, key_log = null, status_key = 'noactive'  where id = "+ keysingle.getId();
                    System.out.println(strQuery);
                    session.createSQLQuery(strQuery).executeUpdate();
                    trans.commit();
                    return true;
                } catch (Exception e) {
                    trans.rollback();
                    System.out.println("Error " + e.getMessage());
                    return false;
                }
        }
        else{
            return false;
        }
        
    }
    
    public boolean Adjourn(String key, int num)
    {
        KeySingle keysingle = findKeyByKey_Single(key);
        
        Date endDate = keysingle.getEnd_date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, num);
        System.out.println(formatter.format(cal.getTime()));
        
        int exTime = keysingle.getKey_log()+ num;
        
        
        if (keysingle.getKey_single().equalsIgnoreCase(key))
        {
            Session session = this.sessionFactory.getCurrentSession();
                Transaction trans = session.beginTransaction();
                try {
                    String strQuery = "update keysingle set end_date = '" + formatter.format(cal.getTime()) + "', key_log = " + exTime + ", status_key = 'inactive'  where id = " + keysingle.getId();
                    System.out.println(strQuery);
                    session.createSQLQuery(strQuery).executeUpdate();
                    trans.commit();
                    return true;
                } catch (Exception e) {
                    trans.rollback();
                    System.out.println("Error " + e.getMessage());
                    return false;
                }
        }
        else{
            return false;
        }
    }
}
