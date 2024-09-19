package com.meetsipdrink.chat.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        System.out.println("새 클라이언트와 연결되었습니다.");
    }
    //연결 됐을 때

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws IOException {
        String chatMessage = message.getPayload();
        System.out.println(message.getPayload());

        for (WebSocketSession connectedSession : sessions) {
            connectedSession.sendMessage(message);
            saveChatToFile(chatMessage);
        }

    }

    //saveChatToFile  수신한 채팅 메시지 파일에 기록
    //파일이 존재하지 않으면 새로 생성 존재하면 기존 내용 뒤에 추가
    private void saveChatToFile(String chatMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("chat-log.txt", true))) {
            writer.write(chatMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }




//메시지 보낼 때

//    @Override
//    public void afterConnectionClosed(WebSocketSession session,
//                                      CloseStatus status)  {
//        sessions.remove(session);
//
//        System.out.println("특정 클라이언트와의 연결이 해제되었습니다.");
//    }
//연결 끊겼을 때




//}
