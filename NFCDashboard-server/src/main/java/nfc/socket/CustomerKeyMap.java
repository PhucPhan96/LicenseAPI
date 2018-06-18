/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.socket;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Admin
 */
public class CustomerKeyMap {
    private ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	
    /**
     * SelectionKey ?? 
     * @param storename	??? ??? 
     * @param key	SelectionKey
     */
//	public void addKey(String storename, SelectionKey key){
//                    
//			map.put(storename, key);
//	}

    public void addKey(String uuid, String sessionId){
        if(map.get(uuid) == null){
            map.put(uuid, sessionId);   
        }           //map.put(storename, key);
    }

    /**
     * SelectionKey? ?? ??? 
     */
    public void clearAll(){
            map.clear();
    }

    /**
     * ???? ??? ??, ???.
     * @param storename
     */
    public void removeKey(String uuid){
        map.remove(uuid);
    }


    public String getKey(String uuid){
        return map.get(uuid);
    }


//	@Override
    public String print(){
        StringBuilder sb = new StringBuilder();

        for(String uuid : map.keySet()){
            sb.append(uuid).append(":").append(map.get(uuid));
        }
        return sb.toString();
    }
}
