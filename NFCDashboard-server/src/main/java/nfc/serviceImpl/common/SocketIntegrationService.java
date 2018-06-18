/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.common;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

/**
 *
 * @author Admin
 */

//@Configuration
//@ComponentScan
//@EnableIntegration
public class SocketIntegrationService {  
    
//    @Bean
//    public ServerWebSocketContainer serverWebSocketContainer() {
//            return new ServerWebSocketContainer("/notify").setAllowedOrigins("*").withSockJs();
//            //return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
//    }
//    
//    @Bean(name = "webSocketFlow.input")
//    public MessageChannel sendToStore() {
//        return new DirectChannel();
//    }
//    
//    @Bean
//    public ServerWebSocketContainer serverWebSocketContainerCustomer() {
//            return new ServerWebSocketContainer("/customer").setAllowedOrigins("*").withSockJs();
//            //return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
//    }
//        
//    @Bean(name = "webSocketFlowCustomer.input")
//    public MessageChannel sendToCustomer() {
//        return new DirectChannel();
//    }
//    
//    @Bean
//    MessageHandler webSocketOutboundAdapter() {
//        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
//    }
//    
//    @Bean
//    MessageHandler webSocketOutboundAdapterCustomer() {
//        return new WebSocketOutboundMessageHandler(serverWebSocketContainerCustomer());
//    }
//    
//    @Bean
//    IntegrationFlow webSocketFlow() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                    .map(s -> MessageBuilder.fromMessage(m)
//                            .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
//                            .build())
//                    .collect(Collectors.toList());
//            f.split( Message.class, splitter)
//                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
//                    .handle(webSocketOutboundAdapter());
//        };
//    }
//    
//    @Bean
//    IntegrationFlow webSocketFlowCustomer() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainerCustomer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                   .map(s -> MessageBuilder.fromMessage(m)
//                             .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
//                            .build())
//                    .collect(Collectors.toList());
//            f.split( Message.class, splitter)
//                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
//                    .handle(webSocketOutboundAdapterCustomer());
//        };
//    }
//    
//    
//    @Bean
//    public String setHandelSocket(){
//        
//        //serverWebSocketContainer()
//        return "OKIE";
//    }
//    @Bean
//    public String getListenMessage(){
//        new WebSocketInboundChannelAdapter(serverWebSocketContainer()).onMessage(session, webSocketMessage);
//        serverWebSocketContainer().setMessageListener(new WebSocketListener() {
//            @Override
//            public void onMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void afterSessionStarted(WebSocketSession wss) throws Exception {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void afterSessionEnded(WebSocketSession wss, CloseStatus cs) throws Exception {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public List<String> getSubProtocols() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        return "";
//    }
    
    
}
