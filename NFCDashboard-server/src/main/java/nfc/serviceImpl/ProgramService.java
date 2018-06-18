package nfc.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.SqlASTFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Category;
import nfc.model.Program;
import nfc.model.ProgramRole;
import nfc.model.User;
import nfc.model.ViewModel.ProgramRoleSubmit;
//import nfc.model.ViewModel.ProgramRoleView;
import nfc.service.IProgramService;
import nfc.service.ISupplierService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.criterion.Order;

@Transactional
public class ProgramService implements IProgramService {
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }

    public List<Program> getListProgram(String username){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Program> programs = new ArrayList<>();
        try{
            String sql = "SELECT * FROM fg_programs WHERE program_id IN (SELECT distinct program_id FROM fg_program_role WHERE role_id IN  (SELECT role_id FROM fg_user_roles WHERE user_id IN(SELECT user_id  FROM fg_users WHERE user_name  ='"+username+"'))) and use_yn='Yes' order by display_seq";
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Program.class);
            programs = query.list();			
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return programs;		
    }

    public List<Program> getListProgramFull(){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Program> programs = new ArrayList<>();
        try{
            Criteria criteria = session.createCriteria(Program.class);	
            criteria.add(Restrictions.eq("use_yn", "Yes"));
            criteria.addOrder(Order.asc("display_seq"));
            programs = (List<Program>)criteria.list();			
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return programs;

    }

    public Program insertProgram(Program prog)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            session.save(prog);
            trans.commit();			
        }
        catch(Exception ex)
        {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
        }
        return prog;
    }
    public boolean deleteProgram(String ProgId)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {   
            String deleteQueryChild = "update fg_programs set use_yn ='No' where parent_prog_id = '" + ProgId +"'";
            String deleteQuery = "update fg_programs set use_yn ='No' where program_id = '" + ProgId +"'";
            Query query = session.createSQLQuery(deleteQueryChild);
            query.executeUpdate();
            query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
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
//	public boolean deleteProgram(String ProgId)
//	{
//		Session session = this.sessionFactory.getCurrentSession();
//		Transaction trans = session.beginTransaction();
//		try
//		{	String deleteQueryChild = "delete from fg_programs where parent_prog_id = '" + ProgId+"'";
//			String deleteQuery = "delete from fg_programs where program_id = '" + ProgId+"'";
//			Query query = session.createSQLQuery(deleteQueryChild);
//		    query.executeUpdate();
//		    
//		    query = session.createSQLQuery(deleteQuery);
//		    query.executeUpdate();
//		    trans.commit();
//		    return true;
//		}
//		catch(Exception ex)
//		{
//			System.out.println("Error " + ex.getMessage());
//			trans.rollback();
//			return false;
//		}
//	}
    public boolean editProgram(Program prog)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {	
            session.update(prog);
//            String editQuery = "update fg_programs set prog_name ='"+ prog.getProg_name()+"' where program_id = '" + prog.getProgram_id()+"'";
//            Query query = session.createSQLQuery(editQuery);
//            query.executeUpdate();
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
    
    public List<ProgramRole> getListProgramRolebyRoleId(int roleId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<ProgramRole> programRoles = new ArrayList<>();
        try{
            Criteria criteria = session.createCriteria(ProgramRole.class);	
            criteria.add(Restrictions.eq("role_id", roleId));
            programRoles = (List<ProgramRole>)criteria.list();			
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
       
        return programRoles;	
    }
    
    public boolean SaveProgRole(ProgramRoleSubmit progRole)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {	
            String deleteQuery = "delete from fg_program_role where role_id = '" + progRole.getRole_id()+"'";
            Query query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
            if(progRole.getListProgRole().size()>0)
            {
                for(ProgramRole item:progRole.getListProgRole())
                {
                    String addQuery ="INSERT INTO fg_program_role VALUES ('"+item.getProgram_id()+"','"+item.getApp_id()+"',"+progRole.getRole_id()+")";
                    query = session.createSQLQuery(addQuery);
                    query.executeUpdate();
                }

            }
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
