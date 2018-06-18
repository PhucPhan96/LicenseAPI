package nfc.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nfc.model.Code;
import nfc.model.Role;
import nfc.model.UserRole;
import nfc.service.IRoleService;
import nfc.service.IUserService;
import nfc.service.common.ICommonService;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleService implements IRoleService {
	@Autowired
	private ICommonService commonDAO;
	@Autowired
	private IUserService userDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Role> getListRole(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Role.class);
		List<Role> list = (List<Role>) criteria.list();
		trans.commit();
		return list;
	}
	public boolean insertRole(Role role){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int roleIdDesc = 0;
			Serializable ser = session.save(role);
	        if (ser != null) {
	        	roleIdDesc = (Integer) ser;
	        }
			Map<String, String> map = new HashMap<String, String>();
			map.put("group_code", "0003");
			map.put("sub_code", roleIdDesc+"");
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
	public Role getRole(String roleId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Restrictions.eq("role_id", Integer.parseInt(roleId)));
		Role role = (Role) criteria.uniqueResult();
		trans.commit();
		return role;
	}
	public Role getRoleWithSeeion(Session session,String roleId){
		//Session session = this.sessionFactory.getCurrentSession();
		//Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Restrictions.eq("role_id", Integer.parseInt(roleId)));
		Role role = (Role) criteria.uniqueResult();
		//trans.commit();
		return role;
	}
	public boolean updateRole(Role role){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(role);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	public boolean deleteRole(String roleId){
		Role role = getRole(roleId);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.delete(role);
			commonDAO.deleteCode(session, "0003", roleId+"");
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		} 
	}
	public List<Role> getListRoleByUserId(String userId) {
		List<Role> roles = new ArrayList<Role>();
		List<UserRole> lstUserRole = userDAO.getListUserRole(userId);
		for(UserRole userRole : lstUserRole)
		{
			Role role = getRole(userRole.getRole_id()+"");
			roles.add(role);
		}
		// TODO Auto-generated method stub
		return roles;
	}
        public List<Role> getListRoleOfUserPermission(String username){
            Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"CALL SP_GetListRoleOfUserPermission(:username)")
                                .addEntity(Role.class)
                                .setParameter("username", username);
		List<Role>  roles = (List<Role>) query.list();
		trans.commit();
		return roles;
	}
        
//	public List<Role> getListRoleOfUserPermission(String username){
//		User user = userDAO.findUserByUserName(username);
//		List<Role> userRoles = getListRoleByUserId(user.getUser_id());
//		System.out.println("get user: "+ user.getUser_id());
//		System.out.println("userrole: "+ userRoles.size());
//		if(containsRoleName(userRoles,"SuperAdmin")!=null)
//		{
//			return getRoleOfUserPermission(new String[]{"SuperAdmin"});
//		}
//		else if(containsRoleName(userRoles,"WholeSale")!=null)
//		{
//			return getRoleOfUserPermission(new String[]{"SysAdmin", "WholeSale"});
//		}
//		else if(containsRoleName(userRoles,"Headquarter")!=null)
//		{
//			return getRoleOfUserPermission(new String[]{"SysAdmin", "WholeSale", "Headquarter"});
//		}
//		else if(containsRoleName(userRoles,"Branch")!=null)
//		{
//			return getRoleOfUserPermission(new String[]{"SysAdmin", "WholeSale", "Headquarter", "Branch"});
//		}
////		else if(containsRoleName(userRoles,"Dealer")!=null)
////		{
////			return getRoleOfUserPermission(new String[]{"SysAdmin", "WholeSale", "Headquarter", "Branch", "Dealer"});
////		}
//		else
//		{
//			return new ArrayList<Role>();
//		}
//	}
//	private Role containsRoleName(Collection<Role> c, String roleName) {
//	    for(Role o : c) {
//	        if(o != null && o.getRole_name().equals(roleName)) {
//	            return o;
//	        }
//	    }
//	    return null;
//	}
//	private List<Role> getRoleOfUserPermission(String[] roleLimit)
//	{
//		List<Role> roleSupplier = new ArrayList<Role>();
//		List<Role> roles = getListRole();
//		for(Role role: roles)
//		{
//			if(!Arrays.asList(roleLimit).contains(role.getRole_name().trim())){
//				roleSupplier.add(role);
//			}
//		}
//		return roleSupplier;
//	}
}
