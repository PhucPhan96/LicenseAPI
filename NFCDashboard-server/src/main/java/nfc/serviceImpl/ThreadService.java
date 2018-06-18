package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.transform.Transform;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.AttachFile;
import nfc.model.Supplier;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.ThreadImg;
import nfc.model.ThreadModel;
import nfc.model.User;

import nfc.model.ViewModel.ThreadSupplierUser;
import nfc.model.ViewModel.ThreadView;
import nfc.service.IThreadService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

public class ThreadService implements IThreadService{
    @Autowired
    private IUserService userDAO;
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }
    public List<ThreadView> getListThreadView(String username){
            List<ThreadView> listThreadView = new ArrayList<ThreadView>();
            setupListThreadView(listThreadView, username);
            System.out.println("Size of list thread " + listThreadView.size());
            return listThreadView;
    }
    private void setupListThreadView(List<ThreadView> listThreadView, String username){
            List<ThreadModel> lstThread = getListThread(username);
            for(ThreadModel thread: lstThread){
                    listThreadView.add(getThreadView(thread));
            }
    }
    private ThreadView getThreadView(ThreadModel thread){
            return setupThreadView(thread);
    }
    private ThreadView setupThreadView(ThreadModel thread){
            ThreadView threadView = new ThreadView();
            threadView.setThread(thread);
            threadView.setUsername(getUserNameOfThread(thread.getWriter_id()));
            threadView.setLstAttachFile(getListImageOfThread(thread.getThread_id()));
            return threadView;
    }

    public List<AttachFile> getListImageOfThread(String threadId){
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            Query query = session.createSQLQuery(
                            "select f.* from fg_files f inner join fg_thread_imgs ti on f.file_id = ti.img_id where ti.thread_id = '"+threadId+"'")
                            .addEntity(AttachFile.class);
            
            List<AttachFile> listAttachFile = query.list();
            trans.commit();
            System.out.print("listAttachFile la "+listAttachFile);
            return listAttachFile;
    }
    public List<ThreadModel> getListThread(String username){
            User user = userDAO.findUserByUserName(username);
            Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            Query query = session.createSQLQuery(
                            "CALL GetListThreadParentOfStore(:userId)")
                            .addEntity(ThreadModel.class)
                            .setParameter("userId", user.getUser_id());
            List<ThreadModel> listThread = query.list();
            trans.commit();
            return listThread;
    }
    private String getUserNameOfThread(String userId){
        User user = userDAO.getUser(userId);
        return user.getFirst_name() + " " + user.getMiddle_name() + " " + user.getLast_name();
    }

    public nfc.model.Thread insertThread(nfc.model.Thread thread){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            thread.setApp_id(Utils.appId);
            session.save(thread);
            insertThreadImage(thread.getAttachFile(), session, thread.getThread_id());
            trans.commit();
            ;
        }
        catch(Exception ex){
            trans.rollback();
        }
        return thread;
    }
    
    private void insertThreadImage(List<AttachFile> attachFiles, Session session, String threadId){
        for(AttachFile attachFile : attachFiles){
            ThreadImg threadImage = new ThreadImg();
            threadImage.setImg_id(attachFile.getFile_id());
            threadImage.setThread_id(threadId);
            session.save(threadImage);
        }
    }
    
    public boolean updateThread(nfc.model.Thread thread){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            session.update(thread);
            deleteReferenceOfThread(session, thread.getThread_id(), "fg_thread_imgs");
            insertThreadImage(thread.getAttachFile(), session, thread.getThread_id());
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    private void deleteReferenceOfThread(Session session, String threadId, String table)
    {
        String deleteQuery = "delete from "+table+" where thread_id = '" + threadId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }
    
    private void deleteReferenceOfChildThread(Session session, String threadId, String table)
    {
        String deleteQuery = "delete from "+table+" where parent_thread_id = '" + threadId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }
    
    public boolean deleteThread(String threadId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            deleteReferenceOfThread(session, threadId, "fg_thread_imgs");
            deleteReferenceOfChildThread(session, threadId, "fg_threads");
            deleteReferenceOfThread(session, threadId, "fg_threads");
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
 
    public List<ThreadSupplierUser> getListThreadStorebyIDNotOwner(int supplID){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();       
         List<ThreadSupplierUser> listThreadSupplierUser = new ArrayList<ThreadSupplierUser>();
            //Get List userID of store
            try{             
                 
                List<SupplierUser> listSupplierUser = new ArrayList<SupplierUser>();            
                listSupplierUser = session.createSQLQuery("SELECT s.* FROM fg_supplier_users s WHERE s.suppl_id="+supplID+"").addEntity(SupplierUser.class).list();               
                //Get list Thread
                for(SupplierUser supplierUser : listSupplierUser){                            
                    listThreadSupplierUser = session.createSQLQuery("SELECT s.supplier_name,u.user_name, t.* from fg_threads t inner join  fg_boards b on t.board_id = b.board_id inner join fg_supplier_work sw on sw.board_id = b.board_id inner join fg_suppliers s on sw.suppl_id = s.suppl_id  inner join fg_users u on t.writer_id = u.user_id  where t.parent_thread_id = 0 and  sw.suppl_id="+supplID+" and t.writer_id <> '"+supplierUser.getUser_id()+"'order by t.write_date DESC").setResultTransformer(Transformers.aliasToBean(ThreadSupplierUser.class)).list();                                     
                } 
                trans.commit(); 
            }
           catch (Exception ex) {
                System.err.println("error" + ex.getMessage());
                trans.rollback();
            }
          
            return listThreadSupplierUser;
    }
    public List<ThreadSupplierUser> getListThreadStorebyID(int supplID){
         Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
             List<ThreadSupplierUser> listThreadSupplierUser = new ArrayList<ThreadSupplierUser>();
            try{
                listThreadSupplierUser = session.createSQLQuery("SELECT s.supplier_name,u.user_name, t.* from fg_threads t inner join  fg_boards b on t.board_id = b.board_id inner join fg_supplier_work sw on sw.board_id = b.board_id inner join fg_suppliers s on sw.suppl_id = s.suppl_id  inner join fg_users u on t.writer_id = u.user_id  where t.parent_thread_id = '0' and  sw.suppl_id="+supplID+" order by t.write_date DESC").setResultTransformer(Transformers.aliasToBean(ThreadSupplierUser.class)).list();
                trans.commit();            
            }
            catch(Exception ex){
                trans.rollback();
            }
            return listThreadSupplierUser;
    }
      public List<ThreadSupplierUser> getListThreadStorebyWriteID(int supplID){
         Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
             List<ThreadSupplierUser> listThreadSupplierUser = new ArrayList<ThreadSupplierUser>();
            try{
                List<SupplierUser> listSupplierUser = new ArrayList<SupplierUser>();            
                listSupplierUser = session.createSQLQuery("SELECT s.* FROM fg_supplier_users s WHERE s.suppl_id="+supplID+"").addEntity(SupplierUser.class).list(); 
                for ( SupplierUser supplierUser :listSupplierUser )
                {
                 listThreadSupplierUser = session.createSQLQuery("SELECT s.supplier_name,u.user_name, t.* from fg_threads t inner join  fg_boards b on t.board_id = b.board_id inner join fg_supplier_work sw on sw.board_id = b.board_id inner join fg_suppliers s on sw.suppl_id = s.suppl_id  inner join fg_users u on t.writer_id = u.user_id  where t.parent_thread_id = '0' and  sw.suppl_id="+supplID+" and t.writer_id='"+supplierUser.getUser_id()+"' order by t.write_date DESC").setResultTransformer(Transformers.aliasToBean(ThreadSupplierUser.class)).list();
                }
                
                trans.commit();            
            }
            catch(Exception ex){
                System.out.println("Loi " + ex.getMessage());
                trans.rollback();
            }
            return listThreadSupplierUser;
    }
     public List<ThreadSupplierUser> getListThreadStoreSmall(int suppl_id, String thread_id){
         Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
             List<ThreadSupplierUser> listThreadSupplierUser = new ArrayList<ThreadSupplierUser>();
            try{
                listThreadSupplierUser = session.createSQLQuery("SELECT s.supplier_name,u.user_name, t.* from fg_threads t inner join  fg_boards b on t.board_id = b.board_id inner join fg_supplier_work sw on sw.board_id = b.board_id inner join fg_suppliers s on sw.suppl_id = s.suppl_id  inner join fg_users u on t.writer_id = u.user_id  where sw.suppl_id="+ suppl_id+" and t.parent_thread_id = " + "'" + thread_id + "'"
                        + " order by t.write_date DESC").setResultTransformer(Transformers.aliasToBean(ThreadSupplierUser.class)).list();
                trans.commit();            
            }
            catch(Exception ex){
                trans.rollback();
            }
            return listThreadSupplierUser;
    }
      public boolean updateThreadStoreSmall(nfc.model.Thread thread ) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            
            session.update(thread);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
      public nfc.model.Thread getThreadByID(String thread_id)
    {
        Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"SELECT t.* FROM fg_threads t  WHERE t.thread_id =" + "'" + thread_id + "'")
				.addEntity(nfc.model.Thread.class);
		nfc.model.Thread result = (nfc.model.Thread) query.uniqueResult();
		trans.commit();
		return result;
    }
      
    public int getBoardIDbySupllierID(int suppl_id)
    { 
        Session session = this.sessionFactory.getCurrentSession();
        int boardID =0;
        Transaction trans = session.beginTransaction();
        List<SupplierWork> listSupplierWork = new ArrayList<SupplierWork>() ;
        try{
            listSupplierWork = session.createSQLQuery("SELECT sw.* FROM fg_supplier_work sw  WHERE sw.suppl_id =" + suppl_id).addEntity(SupplierWork.class).list();
            trans.commit(); 
            boardID = listSupplierWork.get(0).getBoard_id();
        }
        catch(Exception ex){
            trans.rollback();
        }        
        return boardID; 
    }
     public boolean deleteThreadSmall(String thread_id) {
        nfc.model.Thread thread = getThreadByID(thread_id);
        System.out.print("Vao getThreadByID" +thread_id);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.delete(thread);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }
     public nfc.model.Thread insertThreadStore(nfc.model.Thread thread){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            thread.setApp_id(Utils.appId);
            session.save(thread);           
            trans.commit();
            ;
        }
        catch(Exception ex){
            trans.rollback();
        }
        return thread;
    }
    public List<ThreadSupplierUser> getListThreadNoReview(int suppl_id){
         Session session = this.sessionFactory.getCurrentSession();
            Transaction trans = session.beginTransaction();
            List<ThreadSupplierUser> listThreadSupplierUser = new ArrayList<ThreadSupplierUser>();
            try{
             
                List<SupplierUser> listSupplierUser = new ArrayList<SupplierUser>();            
                listSupplierUser = session.createSQLQuery("SELECT s.* FROM fg_supplier_users s WHERE s.suppl_id="+suppl_id+"").addEntity(SupplierUser.class).list();
                for(SupplierUser supplierUser : listSupplierUser){ 
                    listThreadSupplierUser = session.createSQLQuery(" SELECT s.supplier_name,u.user_name, t.* from fg_threads t  inner join  fg_boards b on t.board_id = b.board_id  inner join fg_supplier_work sw on sw.board_id = b.board_id  inner join fg_suppliers s on sw.suppl_id = s.suppl_id   inner join fg_users u on t.writer_id = u.user_id  where sw.suppl_id="+ suppl_id+" and t.writer_id <> '"+supplierUser.getUser_id()+"' and t.thread_id IN  ( select thread_id from fg_threads where parent_thread_id = '0' AND  thread_id NOT IN (select parent_thread_id from fg_threads where parent_thread_id != '0' ) )").setResultTransformer(Transformers.aliasToBean(ThreadSupplierUser.class)).list();
                }                          
                trans.commit();            
            }
            catch(Exception ex){
                trans.rollback();
            }
            return listThreadSupplierUser;
    } 
     
    public Object fGetReviewCount(int board_id){
        Object reviewCount = new Object();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Query query = session.createSQLQuery(
                "SELECT count(thread_id) as review_count FROM 82wafoodgo.fg_threads where board_id = " + board_id);
        reviewCount = (Object) query.uniqueResult();
        trans.commit();
        return reviewCount;
    }
     public List<ThreadView> getListReview(String username)
     {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<ThreadModel> listThreadModel = new ArrayList<ThreadModel>();
        List<ThreadView> listThreadView = new ArrayList<ThreadView>();
        
        try{
            listThreadModel = session.createSQLQuery("SELECT  t.* from fg_threads t where t.writer_id='"+ username+"'AND t.parent_thread_id = '0' order by t.write_date DESC").addEntity(ThreadModel.class).list();
            trans.commit();             
            for ( ThreadModel threadModel:listThreadModel){
                ThreadView threadView = new ThreadView();
                threadView.setThread(threadModel);
                //Get supplier name
                String supplierName =""; 
                System.out.println("list la"+getSupplierByThreadId(threadModel.getThread_id()).size());
                if(getSupplierByThreadId(threadModel.getThread_id()).size()>0){
                 supplierName= getSupplierByThreadId(threadModel.getThread_id()).get(0).getSupplier_name();
                }                
                threadView.setSupplierName(supplierName);                
               
                //threadView.setUsername(username);
                threadView.setLstAttachFile(getListImageOfThread(threadModel.getThread_id()));
                listThreadView.add(threadView);
            }            
        }
        catch(Exception ex){
            trans.rollback();
        }       
        return listThreadView;
     }
    public List<Supplier> getSupplierByThreadId (String thread_id){
    
         Session session = this.sessionFactory.getCurrentSession();
         Transaction trans = session.beginTransaction();
         List<Supplier> listSupplier = new ArrayList<Supplier>();
         listSupplier = session.createSQLQuery("SELECT s.* FROM fg_suppliers s INNER JOIN fg_supplier_work sw ON s.suppl_id = sw.suppl_id INNER JOIN fg_boards b ON sw.board_id = b.board_id INNER JOIN fg_threads t ON t.board_id = b.board_id WHERE thread_id='"+thread_id+"'").addEntity(Supplier.class).list();
         trans.commit(); 
         //supplierName = listSupplier.get(0).getSupplier_name();
        
        return listSupplier;
    }
    
    
    public List<ThreadModel> getListThreadApp(int suppl_id){
    Session session = this.sessionFactory.getCurrentSession();
       Transaction trans = session.beginTransaction();
       List<ThreadModel> listThreadSupplierUser = new ArrayList<ThreadModel>();
       try{
           List<SupplierUser> listSupplierUser = new ArrayList<SupplierUser>();            
           listSupplierUser = session.createSQLQuery("SELECT s.* FROM fg_supplier_users s WHERE s.suppl_id="+suppl_id+"").addEntity(SupplierUser.class).list();
           for(SupplierUser supplierUser : listSupplierUser){ 
               listThreadSupplierUser = session.createSQLQuery("SELECT t.* from fg_threads t inner join  fg_boards b on t.board_id = b.board_id inner join fg_supplier_work sw on sw.board_id = b.board_id inner join fg_suppliers s on sw.suppl_id = s.suppl_id  inner join fg_users u on t.writer_id = u.user_id  where t.parent_thread_id = '0' and  sw.suppl_id="+suppl_id+" and t.writer_id='"+supplierUser.getUser_id()+"' order by t.write_date DESC limit 0,1").setResultTransformer(Transformers.aliasToBean(ThreadModel.class)).list();
           }                          
           trans.commit();            
       }
       catch(Exception ex){
           System.out.println("BUG LA LALALA: "+ex.getMessage());
           trans.rollback();
       }
       return listThreadSupplierUser;
    } 

    public boolean deleteReview(String thread_id){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();        
        try{
            Query query = session.createSQLQuery(
				"SELECT t.* FROM fg_threads t  WHERE t.thread_id =" + "'" + thread_id + "'")
				.addEntity(nfc.model.Thread.class);
            nfc.model.Thread thread = (nfc.model.Thread) query.uniqueResult();
            session.delete(thread);
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    public boolean deleteAllReview(String thread_id){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();  
        try{
            deleteReferenceOfChildThread(session, thread_id, "fg_threads");
            deleteReferenceOfThread(session, thread_id, "fg_threads");
            trans.commit();
            return true;
            
        }catch(Exception ex){
            trans.rollback();
            return false;
        }
        
    }
    
}
