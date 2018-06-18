package nfc.service;

import java.util.List;

import nfc.model.Board;
import nfc.model.Thread;
import nfc.model.ViewModel.BoardView;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.ThreadImageView;

public interface IBoardService {
	boolean insertBoard(Board board);
        boolean updateBoard(Board board);
	List<Board> getListBoard(String userName);
	List<Thread> getListThread(int board_id);
	boolean deleteThread(String threadId);
	boolean deleteBoard(int boardId);
	List<Board> getListBoards(int boardId);
	List<Thread> getListThreadFromBoardId(int board_id);
	List<Thread> getListThreadSmall(String thread_id);
	boolean insertThread(Thread thread);
	BoardView getBoardView (int boardId);
        Board getBoard(int boardId);
        
        GridView getListBoardGrid(GridView gridData);
        public boolean insertThreadImageView(ThreadImageView threadImgView);
}
