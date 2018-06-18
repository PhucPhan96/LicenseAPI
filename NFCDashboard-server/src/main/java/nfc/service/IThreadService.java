/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.ThreadModel;
import nfc.model.ViewModel.ThreadSupplierUser;
import nfc.model.ViewModel.ThreadView;

/**
 *
 * @author Admin
 */
public interface IThreadService {
    List<ThreadView> getListThreadView(String username);
    List<ThreadModel> getListThread(String username);
    nfc.model.Thread insertThread(nfc.model.Thread thread);
    boolean updateThread(nfc.model.Thread thread);
    boolean deleteThread(String threadId);
    List<ThreadSupplierUser> getListThreadStorebyID(int supplID);
    List<ThreadSupplierUser> getListThreadStorebyIDNotOwner(int supplID);
    List<ThreadSupplierUser> getListThreadStorebyWriteID(int supplID);
    List<ThreadSupplierUser> getListThreadStoreSmall(int supplID, String thread_id);
    boolean updateThreadStoreSmall(nfc.model.Thread thread ); 
    nfc.model.Thread getThreadByID(String thread_id);
    boolean deleteThreadSmall(String thread_id) ;      
    nfc.model.Thread insertThreadStore(nfc.model.Thread thread);
    int getBoardIDbySupllierID(int suppl_id);
    List<ThreadSupplierUser>  getListThreadNoReview(int suppl_id);
    public Object fGetReviewCount(int board_id);
    List<ThreadView> getListReview(String username);
    List<ThreadModel> getListThreadApp(int suppl_id);
    boolean deleteReview(String thread_id);
    boolean deleteAllReview(String thread_id);
}
