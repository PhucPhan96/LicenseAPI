package nfc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import nfc.model.ThreadModel;
import nfc.model.ViewModel.ThreadSupplierUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.ViewModel.ThreadView;
import nfc.service.IThreadService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ThreadManagementController {
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IThreadService threadDAO;

    @RequestMapping(value="threads", method=RequestMethod.GET)
    public List<ThreadView> getListThread(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<ThreadView> threads = threadDAO.getListThreadView(username);
        return threads;
    }
    
    @RequestMapping(value="thread/add", method=RequestMethod.POST)
    public @ResponseBody nfc.model.Thread insertThread(@RequestBody nfc.model.Thread thread){
        return threadDAO.insertThread(thread);
    }
    
    @RequestMapping(value="thread/update", method=RequestMethod.PUT)
    public @ResponseBody String editThread(@RequestBody nfc.model.Thread thread){
        String data = threadDAO.updateThread(thread) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="thread/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deletePaymentView(@PathVariable("id") String threadId){
        String data = threadDAO.deleteThread(threadId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="thread/getListThreadStorebyID/{suppl_id}", method=RequestMethod.GET)
    public  List<ThreadSupplierUser> getListThreadStorebyID(@PathVariable("suppl_id") int suppl_id){               
        return threadDAO.getListThreadStorebyID(suppl_id);
    }
    @RequestMapping(value="thread/getListThreadStorebyIDNotOwner/{suppl_id}", method=RequestMethod.GET)
    public  List<ThreadSupplierUser> getListThreadStorebyIDNotOwner(@PathVariable("suppl_id") int suppl_id){               
        return threadDAO.getListThreadStorebyIDNotOwner(suppl_id);
    }
    @RequestMapping(value="thread/getListThreadStorebyWriteID/{suppl_id}", method=RequestMethod.GET)
    public  List<ThreadSupplierUser> getListThreadStorebyWriteID(@PathVariable("suppl_id") int suppl_id){
        System.out.print("ahihi la" + suppl_id);   
        return threadDAO.getListThreadStorebyWriteID(suppl_id);
    }
    @RequestMapping(value="thread/getListThreadStoreSmall/{suppl_id}/{thread_id}", method=RequestMethod.GET)
    public  List<ThreadSupplierUser> getListThreadStoreSmall(@PathVariable("suppl_id") int suppl_id,
            @PathVariable("thread_id") String thread_id){
        //System.out.print("list Smallthread" + threadDAO.getListThreadStoreSmall(suppl_id,thread_id));      
        return threadDAO.getListThreadStoreSmall(suppl_id,thread_id);
    }
    @RequestMapping(value="thread/updateThreadSmall", method=RequestMethod.PUT)
    public @ResponseBody String updateThreadSmall(@RequestBody nfc.model.Thread thread){
        System.out.print("Vao Update Thread" +thread);
        String data = threadDAO.updateThreadStoreSmall(thread) + "";          
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="thread/deleteThreadSmall/{threadId}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteThreadSmall(@PathVariable("threadId") String threadId){
         System.out.print("Vao Delete Thread" +threadId);
        String data = threadDAO.deleteThreadSmall(threadId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="thread/insertThreadStore", method=RequestMethod.POST)
    public @ResponseBody nfc.model.Thread insertThreadStore(@RequestBody nfc.model.Thread thread){
        return threadDAO.insertThreadStore(thread);
    }
    @RequestMapping(value="thread/getBoardIDbySupllierID/{suppl_id}", method=RequestMethod.GET)
    public  int getBoardIDbySupllierID(@PathVariable("suppl_id") int suppl_id){               
        return threadDAO.getBoardIDbySupllierID(suppl_id);
    }
    @RequestMapping(value="thread/getListThreadNoReview/{suppl_id}", method=RequestMethod.GET)
    public  List<ThreadSupplierUser> getListThreadNoReview(@PathVariable("suppl_id") int suppl_id){ 
        System.out.print("Vao No Answer Thread"+ threadDAO.getListThreadNoReview(suppl_id));
        return threadDAO.getListThreadNoReview(suppl_id);
    }
    @RequestMapping(value="app/thread/getreviewcount/{board_id}", method=RequestMethod.GET)
    public Object getReviewCount(@PathVariable("board_id") int board_id){
        Object reviewCount = threadDAO.fGetReviewCount(board_id);
        return reviewCount;
    }
    @RequestMapping(value="app/thread/getListReview/{username}", method=RequestMethod.GET)
    public  List<ThreadView> getListReview(@PathVariable("username") String username){ 
        System.out.print("Vao getListReview"+ threadDAO.getListReview(username));
        return threadDAO.getListReview(username);
    }
    @RequestMapping(value="app/thread/getListThreadApp/{suppl_id}", method=RequestMethod.GET)
    public  List<ThreadModel> getListThreadApp(@PathVariable("suppl_id") int suppl_id){ 
        System.out.print(" Con ca getListThreadApp ahihi la: ");
        return threadDAO.getListThreadApp(suppl_id);
    }
    
    @RequestMapping(value="thread/deleteReview/{thread_id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteReview(@PathVariable("thread_id") String thread_id){
        System.out.print("Vao Delete Review" +thread_id);
        String data = threadDAO.deleteReview(thread_id) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="thread/deleteAllReview/{thread_id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteAllReview(@PathVariable("thread_id") String thread_id){
        System.out.print("Vao Delete All Review" +thread_id);
        String data = threadDAO.deleteAllReview(thread_id) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
}
