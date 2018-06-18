package nfc.model.ViewModel;
import java.util.ArrayList;
import java.util.List;

import nfc.model.Board;
import nfc.model.Thread;
import nfc.model.ThreadImg;
import nfc.model.AttachFile;

public class BoardView {
	private Board board = new Board();
	private List<Thread> thread = new ArrayList<Thread>();

	public List<Thread> getThread() {
		return thread;
	}
	public void setThread(List<Thread> thread) {
		this.thread = thread;
	}
	//	private List<AttachFile> file = new ArrayList<AttachFile>();
//	private List<ThreadImg> thread_img = new ArrayList<ThreadImg>();
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
}
