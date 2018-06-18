/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.socket;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nfc.messages.base.StoreBasePacket;
import nfc.serviceImpl.common.SocketIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

/**
 *
 * @author Admin
 */
public class DataQueue {
    private SimpMessagingTemplate template;
    static final int MAX_SIZE_QUEUE = 100;
    private LinkedList<StoreBasePacket> dataQueue = new LinkedList<StoreBasePacket>();
    private ConcurrentHashMap<String, LinkedList<StoreBasePacket>> pendingDataMap = new 
			ConcurrentHashMap<String, LinkedList<StoreBasePacket>>();
    private KeyMap keymap;
//    @Autowired 
//    private SocketIntegrationService integrationService; 
    
    private static class SingletonHelper{
        private static final DataQueue INSTANCE = new DataQueue(); 
    }
    
    public static DataQueue getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
    public DataQueue(){
        keymap = new KeyMap();
        ExecutorService executorService = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors());
//		ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new PoolWorker());
    }
    
    public void setSimpMessagingTemplate(SimpMessagingTemplate template){
        this.template = template;
    }
    
    public void addDataQueue(StoreBasePacket data){
        synchronized(dataQueue){
            dataQueue.add(data);
            dataQueue.notify();
        }
    }
    
    public void deleteKeyMap(String sessionId, String storeId){
        keymap.removeKey(storeId, sessionId);
//        Iterator<String> it = keymap.getKey(storeId).iterator();
//        while(it.hasNext()){
//            String value = it.next();
//            if(value.equals(sessionId)){
//                    keymap.getKey(storeId).remove(value);
//            }
//        }
    }
    
    public void addKeyMap(String sessionId, String storeId){
        keymap.addKey(storeId, sessionId);
    }
    
    public void sendPendingData(String userId){
		
        LinkedList<StoreBasePacket> list = pendingDataMap.get(userId);
        if (list == null) return;

        while(!list.isEmpty()){
            StoreBasePacket data = list.removeFirst();
            System.out.println("pending : " + data);
            sendDataToClient(userId, data);
        }
    }    
    
    public void addPendingData(StoreBasePacket msg){
		
        LinkedList<StoreBasePacket> list = pendingDataMap.get(msg.getUserId());
        if (list == null){
                list = new LinkedList<StoreBasePacket>();
        }

        if ( list.size() >= MAX_SIZE_QUEUE) {
                list.removeFirst();
                System.err.println("pending size is overflow (" + MAX_SIZE_QUEUE +") so removefirst.");
        }

        list.add(msg);
        pendingDataMap.put(msg.getUserId()+"", list);

        System.err.println(
                        String.format("channel is not connected... pendingDataMap(%d)",
                        list.size())
                        );
    }
    
    private void sendDataToClient(String storeId, StoreBasePacket msg){
        template.convertAndSend("/topic/"+storeId, msg);    
    }
    
    private class PoolWorker extends Thread {
		
        public void run() {
            while(true){
                synchronized(dataQueue){
                    while(dataQueue.isEmpty()){
                        try{
                                dataQueue.wait();
                        }catch(InterruptedException e){}
                    }
                    StoreBasePacket order = (StoreBasePacket)dataQueue.removeFirst();
                    List<String> keys = keymap.getKey(order.getUserId());
                    System.err.println(keys);
                    if(keys!=null && keys.size() > 0){
                        sendPendingData(order.getUserId());
                        sendDataToClient(order.getUserId(), order);
                    }
                    else{
                        addPendingData(order);
                    }

                }
            }
        }
    }
}
