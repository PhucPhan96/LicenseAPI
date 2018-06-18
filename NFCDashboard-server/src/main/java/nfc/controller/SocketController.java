/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import nfc.messages.BaseResponse;
import nfc.messages.base.CustomerRegisterPacket;
import nfc.messages.base.RegisterPacket;
import nfc.socket.CustomerDataQueue;
import nfc.socket.DataQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class SocketController {
    
    public SocketController(SimpMessagingTemplate template){
        DataQueue.getInstance().setSimpMessagingTemplate(template);
        CustomerDataQueue.getInstance().setSimpMessagingTemplate(template);
    }
    
    @MessageMapping("/store/register")
    public BaseResponse register(RegisterPacket registerPacket, SimpMessageHeaderAccessor headerAccessor) {
        DataQueue.getInstance().addKeyMap(headerAccessor.getSessionId(), registerPacket.getUserId());
        return new BaseResponse();
    }
    
    @MessageMapping("/store/disconnect")
    public BaseResponse disconnect(RegisterPacket registerPacket, SimpMessageHeaderAccessor headerAccessor) {
        DataQueue.getInstance().deleteKeyMap(headerAccessor.getSessionId(), registerPacket.getUserId());
        return new BaseResponse();
    }
    
    
    @MessageMapping("/cutomer/register")
    public BaseResponse customerRegister(CustomerRegisterPacket customerRegisterPacket, SimpMessageHeaderAccessor headerAccessor) {
        CustomerDataQueue.getInstance().addKeyMap(customerRegisterPacket.getUuid(), headerAccessor.getSessionId());
        return new BaseResponse();
    }
    
    @MessageMapping("/cutomer/disconnect")
    public BaseResponse customerDisconnect(CustomerRegisterPacket customerRegisterPacket, SimpMessageHeaderAccessor headerAccessor) {
        CustomerDataQueue.getInstance().deleteKeyMap(customerRegisterPacket.getUuid());
        return new BaseResponse();
    }
    
    
}
