/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nfc.model.Text;
import nfc.model.User;
import nfc.model.ViewModel.BillSupplierInformation;
import nfc.model.ViewModel.GridFiltering;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.TextView;
import nfc.service.ICodeService;
import nfc.service.ITextService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *   
 * @author Admin
 *
 */
public class TextService implements ITextService {

    @Autowired
    private IUserService userDAO;
    @Autowired
    private ICodeService codeDAO;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Text> getAllText() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Text> list = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Text.class);
            list = (List<Text>) criteria.list();
            trans.commit();
        } catch (HibernateException ex) {
            trans.rollback();
        }
        return list;
    }

    @Override
    public List<Text> getListText() {
        List<Text> lstText = getAllText();
        System.out.println("VAO lstText:" + lstText);
        for (Text text : lstText) {
            text.setText_createdUser(userDAO.getUser(text.getText_createdUser()).getUser_name());
            text.setText_type(codeDAO.getCode("0006", text.getText_type()).getCode_name());
            System.out.println("Value listtext:" + lstText);
        }
        return lstText;
    }

    @Override
    public List<Text> getListTextByType(String text_type) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Text.class);
        criteria.add(Restrictions.eq("text_type", text_type));
        List<Text> list = (List<Text>) criteria.list();
        trans.commit();
        for (Text text : list) {
            text.setText_createdUser(userDAO.getUser(text.getText_createdUser()).getUser_name());
            text.setText_type(codeDAO.getCode("0006", text.getText_type()).getCode_name());
        }
        return list;
    }

    @Override
    public Text getTextbyId(int text_id) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Text.class);
        criteria.add(Restrictions.eq("text_id", text_id));
        Text text = (Text) criteria.uniqueResult();
        trans.commit();
        return text;
    }

    @Override
    public boolean updateText(Text text) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            text.setText_updatedDate(new Date());
            session.update(text);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    @Override
    public boolean insertText(Text text) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            text.setText_createdDate(new Date());
            session.save(text);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean deleteText(int text_id) {
        Text text = getTextbyId(text_id);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.delete(text);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
    
    public GridView getListTextGrid(GridView gridData){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            StringBuilder filterBuild = Utils.generateGridFilterString(gridData, "text_type", "user_name");
            setFilterString(filterBuild, gridData);
            String filter = filterBuild.toString();
            List<Text> list = session.createSQLQuery("select *, (select code_name from fg_codes where sub_code = text_type and group_code = '0006') as code_name, (select user_name from fg_users where user_id = text_createdUser) as user_name from fg_texts " + (filter.equals("")  ? "" :  (" where " + filter)) + " limit " + gridData.getPageSize() + " offset " + ((gridData.getPageIndex() - 1) * gridData.getPageSize()))
                    .setResultTransformer(Transformers.aliasToBean(Text.class))
                    .list();
            gridData.setResponse(new ArrayList<>(convertTextToJson(list)));
            
            long count = (long) session.createSQLQuery("select count(*) as count from fg_texts " + (filter.equals("")  ? "" :  (" where " + filter)))
                    .addScalar("count", LongType.INSTANCE)
                    .uniqueResult();
            gridData.setCount(count);
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error" + ex.getMessage());
            trans.rollback();
        }
        return gridData;
    }
    
    private void setFilterString(StringBuilder filter, GridView gridData){
        for(GridFiltering filtering : gridData.getFiltering()){
            if(filtering.getName().equals("user_name") && !filtering.getValue().equals("")){
                appendFilter("text_createdUser IN (select user_id from fg_users where user_name like '%" + filtering.getValue()+ "%')", filter);
            }
            else if(filtering.getName().equals("text_type")&& !filtering.getValue().equals("")){
                appendFilter("text_type in (select sub_code from fg_codes  where code_name like '%" + filtering.getValue() + "%' and group_code='0006')", filter);
                
            }
        }
    }
    
    private void appendFilter(String appendStr, StringBuilder filter){
        if(filter.length() > 0){
            filter.append(" and ");
        }
        filter.append(appendStr);
    }
    
    private JSONArray convertTextToJson(List<Text> texts){
        JSONArray jsonArr = new JSONArray();
        for(Text text: texts){
            JSONObject object = new JSONObject();
            object.put("text_title", text.getText_title());
            object.put("text_createdDate",Utils.convertDateToString(text.getText_createdDate()));
            object.put("user_name", text.getUser_name());
            object.put("text_type", text.getCode_name());
            object.put("text_id", text.getText_id());
            jsonArr.add(object);
        }
        return jsonArr;
    }
     public List<Text> getListTextbyTextInput(String textInput, String text_type) {
       List<Text> lstText= new ArrayList<Text>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        String sqlQuery = "SELECT * FROM 82wafoodgo.fg_texts where text_title like '%" + textInput + "%' AND text_type='"+text_type+"'";
        try {
            Query query = session.createSQLQuery(sqlQuery).addEntity(Text.class);;
            lstText = (List<Text>) query.list();
            trans.commit();
            
        } catch (Exception ex) {            
            System.out.println(ex);
        }
        return lstText;
    }
     
     public List<Text> getListNews() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Text> lstText= new ArrayList<Text>();
        String sqlQuery = "SELECT * FROM 82wafoodgo.fg_texts where text_type ='0009'";
        try {
            Query query = session.createSQLQuery(sqlQuery).addEntity(Text.class);
            lstText = (List<Text>) query.list();
            trans.commit();
            
        } catch (Exception ex) {            
            System.out.println(ex);
        }
        return lstText;
     }


}
