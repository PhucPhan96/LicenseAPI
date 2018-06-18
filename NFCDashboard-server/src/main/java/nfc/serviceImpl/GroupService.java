package nfc.serviceImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Code;
import nfc.model.Group;
import nfc.service.IGroupService;
import nfc.service.common.ICommonService;

public class GroupService implements IGroupService{
	@Autowired
	private ICommonService commonDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Group> getListGroup() {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Group.class);
		List<Group> list = (List<Group>) criteria.list();
		trans.commit();
		return list;
	}
	public boolean insertGroup(Group group){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int groupIdDesc = 0;
			Serializable ser = session.save(group);
	        if (ser != null) {
	        	groupIdDesc = (Integer) ser;
	        }
	        System.out.println("group Desc " + groupIdDesc);
			Map<String, String> map = new HashMap<String, String>();
			map.put("group_code", "0001");
			map.put("sub_code", groupIdDesc+"");
			Code code = (Code) commonDAO.createObject("nfc.model.Code", map);
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
	public Group getGroup(String groupId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Group.class);
		criteria.add(Restrictions.eq("group_id", Integer.parseInt(groupId)));
		Group group = (Group) criteria.uniqueResult();
		trans.commit();
		return group;
	}
	public boolean updateGroup(Group group) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(group);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	public boolean deleteGroup(String groupId) {
		Group group = getGroup(groupId);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.delete(group);
			commonDAO.deleteCode(session, "0001", groupId + "");
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
}
