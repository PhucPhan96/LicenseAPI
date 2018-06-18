package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.AttachFile;
import nfc.model.Board;
import nfc.model.User;
import nfc.model.Thread;
import nfc.model.ThreadImg;
import nfc.service.IBoardService;
import nfc.service.IFileService;
import nfc.model.ViewModel.BoardView;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.ThreadImageView;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.criterion.Order;
import org.hibernate.type.LongType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BoardService implements IBoardService{
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private IFileService fileDAO;
    @Autowired
    private IUserService userDAO;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    //@Override
    public boolean insertBoard(Board board) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            /*if(cate.getCate_img_id() == 0)
                    cate.setCate_img_id(null);*/
            session.save(board);
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
    public boolean insertThread(Thread thread) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            /*if(cate.getCate_img_id() == 0)
            cate.setCate_img_id(null);*/
            session.save(thread);
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
    public boolean insertThreadImageView(ThreadImageView threadImgView) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            ThreadImg threadImg = new ThreadImg();
            session.save(threadImgView.getThread());
            threadImg.setThread_id(threadImgView.getThread().getThread_id());
            threadImg.setImg_id(threadImgView.getThread_img().getFile_id());
            session.save(threadImg);
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
    
    public List<Board> getListBoard(String userName)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Board> list = new ArrayList<>();
        try{
            String userId = getUserIdfromUserName(userName, session);
            Criteria criteria = session.createCriteria(Board.class);
            criteria.add(Restrictions.eq("owner_id",userId));
            list = (List<Board>) criteria.list();
            for(Board board:list)
            {
                board.setOwner_name(userName);
            }
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return list;
    }
    public String getUserIdfromUserName(String userName, Session session)
    {
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("user_name",userName));
        List<User> list = (List<User>) criteria.list();		
        String userId = list.get(0).getUser_id();
        System.out.println(userId);
        return userId; 
    }
    public List<Thread> getListThread(int board_id)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Thread> list = new ArrayList<>();
        try{
            Criteria criteria = session.createCriteria(Thread.class);
            criteria.add(Restrictions.eq("board_id", board_id));
            criteria.add(Restrictions.eq("parent_thread_id","0"));
            criteria.addOrder(Order.asc("write_date"));
            list = (List<Thread>) criteria.list();
            for(Thread thread: list){	                
               thread.setAttachFile(getListAttachFileOfThread(session, thread.getThread_id()));   
               thread.setChildThreads(getListChildThread(session, thread.getThread_id()));
               thread.setName(getNameOfUserFromUserid(session, thread.getWriter_id()));
            }
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage() +  ex.getLocalizedMessage());
            trans.rollback();
        }
        return list;
    }
    
    private String getNameOfUserFromUserid(Session session, String userId){
        String name = "";
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("user_id", userId));
        User user =  (User) criteria.uniqueResult(); 
        if(user != null){
            name = user.getFirst_name() + " " + user.getMiddle_name() + " " + user.getLast_name();
        } 
        return name;
    }
    
    private List<AttachFile> getListAttachFileOfThread(Session session, String threadId){
        List<AttachFile> attachFiles = new ArrayList<>();
        Criteria criteria = session.createCriteria(ThreadImg.class);
        criteria.add(Restrictions.eq("thread_id",threadId));
        List<ThreadImg> listThImg = (List<ThreadImg>)criteria.list();
        for(ThreadImg item : listThImg)
        {
            attachFiles.add(fileDAO.getAttachFileWithSession(item.getImg_id(),session));				
        }
        return attachFiles;
    }
    
    private List<Thread> getListChildThread(Session session, String threadId){
        List<Thread> listChildThreads = new ArrayList<>();
        Criteria criteria = session.createCriteria(Thread.class);
        criteria.add(Restrictions.eq("parent_thread_id", threadId));
        listChildThreads = (List<Thread>) criteria.list();
        for(Thread thread: listChildThreads){
            thread.setAttachFile(getListAttachFileOfThread(session, thread.getThread_id().trim()));
            thread.setName(getNameOfUserFromUserid(session, thread.getWriter_id().trim()));
        }
        return listChildThreads;
    }

    public boolean deleteThread(String threadId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {	
            String deleteQuery = "delete from fg_thread_imgs where thread_id = " + threadId;
            Query query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
            String deleteThread = "delete from fg_threads where thread_id = " + threadId;
            Query queryThread = session.createSQLQuery(deleteThread);
            queryThread.executeUpdate();
            trans.commit();
            return true;
        }
        catch(Exception ex)
        {
            trans.rollback();
            return false;
        } 
    }
    public boolean deleteBoard(int boardId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {	
            Criteria criteria = session.createCriteria(Thread.class);
            criteria.add(Restrictions.eq("board_id",boardId));
            List<Thread> list = (List<Thread>) criteria.list();
            for(Thread thread:list)
            {
                    String deleteQuery = "delete from fg_thread_imgs where thread_id = '" + thread.getThread_id()+"'";
                    Query query = session.createSQLQuery(deleteQuery);
                query.executeUpdate();

                String deleteThread = "delete from fg_threads where thread_id = '" + thread.getThread_id()+"'";
                    Query queryThread = session.createSQLQuery(deleteThread);
                    queryThread.executeUpdate();
            }
            String deleteBoard = "delete from fg_boards where board_id = " + boardId;
            Query queryBoard = session.createSQLQuery(deleteBoard);
            queryBoard.executeUpdate();
            trans.commit();
            return true;
        }
        catch(Exception ex)
        {
            trans.rollback();
            return false;
        } 
    }

    public List<Thread> getListThreadSmall(String thread_id)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Thread.class);
        criteria.add(Restrictions.eq("parent_thread_id",thread_id));
        List<Thread> list = (List<Thread>) criteria.list();
        trans.commit();
        return list;
    }
    public List<Thread> getListThreadFromBoardId(int board_id)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();		
        Criteria criteria = session.createCriteria(Thread.class);
        Criterion boardId = Restrictions.eq("board_id", board_id);
        Criterion parentId = Restrictions.eq("parent_thread_id", "0");
        LogicalExpression orExp = Restrictions.and(boardId, parentId);
        criteria.add(orExp);
        List<Thread> list = (List<Thread>) criteria.list();
        trans.commit();
        return list;
    }
    public List<Board> getListBoards(int boardId)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Board.class);
        criteria.add(Restrictions.eq("board_id",boardId));
        List<Board> list = (List<Board>) criteria.list();
        trans.commit();
        return list;
    }
	
    public Board getBoard(int boardId)
    {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Board board = new Board();
        try{
            Criteria criteria = session.createCriteria(Board.class);
            criteria.add(Restrictions.eq("board_id",boardId));
            board = (Board) criteria.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }

        return board;
    }
	
    public List<ThreadImg> getListThreadImg(String threadId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();		
        Criteria criteria = session.createCriteria(ThreadImg.class);
        criteria.add(Restrictions.eq("thread_id",threadId));
        List<ThreadImg> list = (List<ThreadImg>) criteria.list();
        trans.commit();
        return list;		
    }

    public BoardView getBoardView (int boardId){
        BoardView boardView = new BoardView();
        boardView.setBoard(getBoard(boardId));
        List<Thread> listThreads = getListThreadFromBoardId(boardId);
        //List<ThreadView> listThreadView = new ArrayList<ThreadView>();
        for( Thread thread: listThreads){
                System.out.println("thread_id is: " + thread.getThread_id());	
                List<ThreadImg> listThreadImg = getListThreadImg(thread.getThread_id());
                List<AttachFile> supplAttachFiles = new ArrayList<AttachFile>();
                for( ThreadImg threadImg: listThreadImg){
                        System.out.println("img_id is: " + threadImg.getImg_id());
                        supplAttachFiles.add(fileDAO.getAttachFile(threadImg.getImg_id()));
                }
                thread.setAttachFile(supplAttachFiles);
        }
        boardView.setThread(listThreads);
        return boardView;
    }
    
    public boolean updateBoard(Board board){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            session.update(board);
            trans.commit();
            return true;
        }
        catch(Exception ex)
        {
            trans.rollback();
            return false;
        } 
    }
    
    public GridView getListBoardGrid(GridView gridData){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            StringBuilder filterBuild = Utils.generateGridFilterString(gridData);
            String filter = filterBuild.toString();
            List<Board> list = session.createSQLQuery("select b.* from fg_boards b join fg_supplier_work sw on b.board_id =  sw.board_id where find_in_set(sw.suppl_id,'" + gridData.getData() + "')" + (filter.equals("")  ? "" :  (" where " + filter)) + " limit " + gridData.getPageSize() + " offset " + ((gridData.getPageIndex() - 1) * gridData.getPageSize()))
                    .addEntity(Board.class)
                    .list();
            gridData.setResponse(new ArrayList<>(convertBoardToJson(list)));
            
            long count = (long) session.createSQLQuery("select count(*) as count from fg_boards b join fg_supplier_work sw on b.board_id =  sw.board_id where find_in_set(sw.suppl_id,'" + gridData.getData() + "')" + (filter.equals("")  ? "" :  (" where " + filter)))
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
    
    private JSONArray convertBoardToJson(List<Board> boards){
        JSONArray jsonArr = new JSONArray();
        for(Board board: boards){
            JSONObject object = new JSONObject();
            object.put("board_id", board.getBoard_id());
            object.put("created_date",Utils.convertDateToString(board.getCreated_date()));
            object.put("board_name", board.getBoard_name());
            jsonArr.add(object);
        }
        return jsonArr;
    }
}
