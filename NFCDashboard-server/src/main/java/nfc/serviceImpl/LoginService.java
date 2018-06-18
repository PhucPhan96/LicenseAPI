package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import nfc.model.AppUser;
import nfc.model.UserRole;
import nfc.service.ILoginService;

@Transactional
public class LoginService implements ILoginService {
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public AppUser findUserByUsername(String username) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		List<AppUser> users = new ArrayList<AppUser>();
		try {
            //String hql = "FROM Users U WHERE U.username = :username";
            //Query query = session.createQuery(hql)
            //        .setParameter("username", username);
            Criteria criteria = session.createCriteria(AppUser.class);
            criteria.add(Restrictions.eq("username", username));
            users = criteria.list();
        } catch (HibernateException e) {
            System.err.println("ERROR: "+ e.getMessage());
        }
		AppUser user = null;
		if(users.size() > 0)
		{
			user = (AppUser)users.get(0);
			
			//user.setUserRole(userRoles);
		}
		return user;
	}
	public Set<UserRole> getListUserRole(String username){
		Session session = this.sessionFactory.getCurrentSession();
		Set<UserRole> userRoles = new HashSet<UserRole>(0);
		try {
            /*String hql = "FROM UsersRoles R WHERE R.username = :username";

            org.hibernate.Query query = session.createQuery(hql)
                    .setParameter("username", username);*/
            Criteria criteria = session.createCriteria(UserRole.class);
            criteria.add(Restrictions.eq("username", username));
            userRoles = new HashSet<UserRole>(criteria.list());
        } catch (HibernateException e) {
            // You can log the error here. Or print to console
        }
		return userRoles;
	}
}
