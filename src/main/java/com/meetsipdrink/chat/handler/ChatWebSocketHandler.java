//package com.meetsipdrink.chat.handler;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//    private final Set<WebSocketSession> sessions = new HashSet<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.add(session);
//
//        System.out.println("새 클라이언트와 연결되었습니다.");
//    }
//    //연결 됐을 때
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session,
//                                     TextMessage message) throws IOException {
//        String payload = message.getPayload();
//        System.out.println(message.getPayload());
//
//        for (WebSocketSession connectedSession : sessions) {
//            connectedSession.sendMessage(message);
//        }
//    }
////메시지 보낼 때
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session,
//                                      CloseStatus status)  {
//        sessions.remove(session);
//
//        System.out.println("특정 클라이언트와의 연결이 해제되었습니다.");
//    }
////연결 끊겼을 때
//
//
//
//
//}
