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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nfc.model.KeyTrial;
import nfc.service.IKeyTrial;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author PhucP
 */
public class KeyTrialService implements IKeyTrial{
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    
    boolean compareWithToday(Date date1, Date date2)
    {
        if(date1.compareTo(date2) > 0)
            return true;
        else
            return false;
    }
    
    public List<KeyTrial> fGetAllKeyTrial() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<KeyTrial> list;
        try {
            Criteria criteria = session.createCriteria(KeyTrial.class);
            list = (List<KeyTrial>) criteria.list();
            System.out.println(list.get(0).getEnd_date());
            trans.commit();
            return list;
        } catch (Exception e) {
            trans.rollback();
        }
        return null;
    }
    
    public KeyTrial checkMain(String main)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        KeyTrial keyTrial = new KeyTrial();
        Criteria criteria = session.createCriteria(KeyTrial.class);
        criteria.add(Restrictions.eq("main", main));
        keyTrial = (KeyTrial) criteria.uniqueResult();
        trans.commit();
        return keyTrial;
    }
    
    public boolean CheckTrial(String main, String cpu)
    {
        System.out.println("vô");
        
        KeyTrial keyTrial = checkMain(main);
        if(keyTrial == null)
        {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            try {
                System.out.println("voo");
                Criteria criteria = session.createCriteria(KeyTrial.class);
                KeyTrial kt = new KeyTrial();
                kt.setMain(main);
                kt.setCpu(cpu);
                kt.setStart_date(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()));
                System.out.println(kt.getStart_date());
                kt.setEnd_date(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(30).toString()));
                System.out.println(kt.getEnd_date());
                kt.setExpired_time(30);
                session.save(kt);
                trans.commit();
                return true;
            } catch (ParseException ex) {
                Logger.getLogger(KeyTrialService.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            catch (Exception e) {
                trans.rollback();
                return false;
            }
        }
        else try {
            System.out.println("vô luon");
            System.out.println(compareWithToday(keyTrial.getEnd_date(), new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString())));
            if(keyTrial.getCpu().equalsIgnoreCase(cpu) && compareWithToday(keyTrial.getEnd_date(), new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString())) == true)
            {
                return true;
            }
            else{
                return false;
            }
        } catch (ParseException ex) {
            Logger.getLogger(KeyTrialService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
