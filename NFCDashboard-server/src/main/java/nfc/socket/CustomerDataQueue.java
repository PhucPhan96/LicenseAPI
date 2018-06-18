/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.socket;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nfc.messages.base.CustomerBasePacket;
import nfc.messages.base.StoreBasePacket;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 *
 * @author Admin
 */
public class CustomerDataQueue {
    private SimpMessagingTemplate template;
    static final int MAX_SIZE_QUEUE = 100;
    private LinkedList<CustomerBasePacket> dataQueue = new LinkedList<CustomerBasePacket>();
    private ConcurrentHashMap<String, LinkedList<CustomerBasePacket>> pendingDataMap = new 
			ConcurrentHashMap<String, LinkedList<CustomerBasePacket>>();
    private CustomerKeyMap keymap;
//    @Autowired 
//    private SocketIntegrationService integrationService; 
    
    private static class SingletonHelper{
        private static final CustomerDataQueue INSTANCE = new CustomerDataQueue(); 
    }
    
    public static CustomerDataQueue getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
    public CustomerDataQueue(){
        keymap = new CustomerKeyMap();
        ExecutorService executorService = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors());
//		ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new PoolWorker());
    }
    
    public void setSimpMessagingTemplate(SimpMessagingTemplate template){
        this.template = template;
    }
    
    public void addDataQueue(CustomerBasePacket data){
        synchronized(dataQueue){
            System.err.println("Add data queue");
            dataQueue.add(data);
            dataQueue.notify();
        }
    }
    
    public void deleteKeyMap(String uuid){
        keymap.removeKey(uuid);
    }
    
    public void addKeyMap(String uuid, String sessionId){
        keymap.addKey(uuid, sessionId);
    }
    
    public void sendPendingData(String uuid){
		
        LinkedList<CustomerBasePacket> list = pendingDataMap.get(uuid);
        if (list == null) return;

        while(!list.isEmpty()){
            CustomerBasePacket data = list.removeFirst();
            System.out.println("pending : " + data);
            sendDataToClient(uuid, data);
        }
    }    
    
    public void addPendingData(CustomerBasePacket msg){
		
        LinkedList<CustomerBasePacket> list = pendingDataMap.get(msg.getUUID());
        if (list == null){
                list = new LinkedList<CustomerBasePacket>();
        }

        if ( list.size() >= MAX_SIZE_QUEUE) {
                list.removeFirst();
                System.err.println("pending size is overflow (" + MAX_SIZE_QUEUE +") so removefirst.");
        }

        list.add(msg);
        pendingDataMap.put(msg.getUUID()+"", list);

        System.err.println(
                        String.format("channel is not connected... pendingDataMap(%d)",
                        list.size())
                        );
    }
    
    private void sendDataToClient(String uuid, CustomerBasePacket msg){
        System.err.println("template " + template);
        template.convertAndSend("/topic/"+uuid, msg);    
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
                    CustomerBasePacket data = (CustomerBasePacket)dataQueue.removeFirst();
                    System.err.println(data.toString());
                    String key = keymap.getKey(data.getUUID());
                    System.err.println(key);
                    if(key!=null && key != ""){
                        System.err.println("Vao send pending");
                        sendPendingData(data.getUUID()+ "");
                        sendDataToClient(data.getUUID()+ "", data);
                    }
                    else{
                        System.err.println("Vao add pending");
                        addPendingData(data);
                    }

                }
            }
        }
    }
}
