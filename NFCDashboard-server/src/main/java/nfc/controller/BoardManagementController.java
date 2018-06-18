package nfc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.AttachFile;
import nfc.model.Board;
import nfc.service.IBoardService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;
import nfc.model.Thread;
import nfc.model.ViewModel.BoardView;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.ProductView;
import nfc.model.ViewModel.ThreadImageView;

@RestController
public class BoardManagementController {
    @Autowired
    private IBoardService boardDAO;
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @RequestMapping(value="board",method=RequestMethod.GET)
    public List<Board> getBoards(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<Board> boards = boardDAO.getListBoard(username);
        return boards;
    }
    
    
    @RequestMapping(value = "board/grid", method = RequestMethod.POST)
    public GridView getListTextGrid(@RequestBody GridView gridData) {
        return boardDAO.getListBoardGrid(gridData);
    }

    @RequestMapping(value="board/edit/{boardId}",method=RequestMethod.GET)
    public Board getBoard(@PathVariable("boardId") int board_id){
        return boardDAO.getBoard(board_id);
    }

    @RequestMapping(value="board/edit/update", method=RequestMethod.PUT)
    public @ResponseBody String editRole(@RequestBody Board board){
        String data = boardDAO.updateBoard(board) + "";
        return "{\"result\":\"" + data + "\"}";
    }

    @RequestMapping(value="board/thread/{id}", method=RequestMethod.GET)
    public List<Thread> getCategory(@PathVariable("id") int board_id){
        System.out.println("boardId is: " + board_id);	
        List<Thread> threads = boardDAO.getListThread(board_id);
        System.out.println("count is: " + threads.size());
        return threads;
    } 
    
    @RequestMapping(value="board/thread/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteThread(@PathVariable("id") String threadId){
        System.out.println("Vao delete " + threadId);
        String data =boardDAO.deleteThread(threadId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="board/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteBoard(@PathVariable("id") int boardId){
        System.out.println("Vao delete " + boardId);
        String data =boardDAO.deleteBoard(boardId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="app/board/thread/{id}", method=RequestMethod.GET)
    public List<Thread> getThread(@PathVariable("id") int board_id){
        System.out.println("boardId is: " + board_id);	
        List<Thread> threads = boardDAO.getListThreadFromBoardId(board_id);
        System.out.println("threads is: " + threads);	
        return threads;
    }
    
    @RequestMapping(value="app/board/threadsmall/{id}", method=RequestMethod.GET)
    public List<Thread> getThreadSmall(@PathVariable("id") String thread_id){
        System.out.println("thread_id is: " + thread_id);	
        List<Thread> threads = boardDAO.getListThreadSmall(thread_id);
        return threads;
    }
    
    @RequestMapping(value="app/board/{id}", method=RequestMethod.GET)
    public List<Board> getListBoardForApp(@PathVariable("id") int boardId){
        System.out.println("boardId is: " + boardId);	
        List<Board> threads = boardDAO.getListBoards(boardId);
        return threads;
    }
    
    @RequestMapping(value="app/boardview/{id}", method=RequestMethod.GET)
    public BoardView getBoardView(@PathVariable("id") int boardId){
        System.out.println("boardId is: " + boardId);	
        BoardView threads = boardDAO.getBoardView(boardId);// .getListBoards(boardId);
        return threads;
    }
    
//    @RequestMapping(value="app/thread/add", method=RequestMethod.POST)
//    public @ResponseBody String insertThread(@RequestBody Thread thread){
//        String data = boardDAO.insertThread(thread) + "";// .insertProductView(productView) + "";
//        return "{\"result\":\"" + data + "\"}";
//    }
    
    @RequestMapping(value="app/thread/add", method=RequestMethod.POST)
    public @ResponseBody String insertThread(@RequestBody ThreadImageView thread){
            String data = boardDAO.insertThreadImageView(thread) + "";// .insertProductView(productView) + "";
            return "{\"result\":\"" + data + "\"}";
    }
	
}
