package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import nfc.model.Code;
import nfc.model.Role;
import nfc.service.ICodeService;

public class CodeService implements ICodeService{
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Code> getListCode(String groupCode) {
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            List<Code> list = new ArrayList<>();
            try{
                Criteria criteria = session.createCriteria(Code.class);
		criteria.add(Restrictions.eq("group_code", groupCode));
		list = (List<Code>) criteria.list();
		trans.commit();
            }
            catch(Exception ex){
                trans.rollback();
            }
            return list;
	}
	public Code getCode(String groupCode, String subCode){
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            Code code = new Code();
            try{
                Criteria criteria = session.createCriteria(Code.class);
		criteria.add(Restrictions.eq("group_code", groupCode));
		criteria.add(Restrictions.eq("sub_code", subCode));
		code = (Code) criteria.uniqueResult();
		trans.commit();
            }
            catch(Exception ex){
                trans.rollback();
            }
            return code;
	}
	public boolean insertCode(Code code){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(code);
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
	public List<Code> getAllCode(){
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            List<Code> list = new ArrayList<>();
            try{
                Criteria criteria = session.createCriteria(Code.class);
		list = (List<Code>) criteria.list();
		trans.commit();
            }
            catch(Exception ex){
                trans.rollback();
            }
            return list;
	}
	public boolean deleteCode(String groupCode, String subCode){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			String deleteQuery = "delete from fg_codes where group_code = " + "'" + groupCode + "'" + " and sub_code = " + "'" + subCode +"'" ;
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
	public boolean updateCode(Code code){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(code);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		} 
	}
}
