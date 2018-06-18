/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import nfc.model.AttachFile;
import nfc.model.Thread;
/**
 *
 * @author Admin
 */
public class ThreadImageView {
    Thread thread;
    AttachFile thread_img;

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public AttachFile getThread_img() {
        return thread_img;
    }

    public void setThread_img(AttachFile thread_img) {
        this.thread_img = thread_img;
    }
    
    
}
