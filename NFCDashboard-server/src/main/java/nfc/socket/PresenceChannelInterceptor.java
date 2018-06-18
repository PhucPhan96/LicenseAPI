/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.socket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 *
 * @author Admin
 */
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter{

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
// 
//        // ignore non-STOMP messages like heartbeat messages
//        if(sha.getCommand() == null) {
//            return;
//        }
// 
//        String sessionId = sha.getSessionId();
// 
//        switch(sha.getCommand()) {
//            case DISCONNECT:
//                Iterator<String> it = keymap.getKey(order.getStoreId()).iterator();
////                                                                    while(it.hasNext()){
////                                                                            SelectionKey value = it.next();
////                                                                            if(value.equals(key)){
////                                                                                    keymap.getKey(order.getStoreId()).remove(value);
////                                                                            }
////                                                                    }
//                System.err.println("Vao disconnect " + sessionId);
//                //logger.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
//                break;
//            default:
//                break;
// 
//        }
        super.postSend(message, channel, sent); //To change body of generated methods, choose Tools | Templates.
    }
    
}
