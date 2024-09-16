package com.meetsipdrink.chat.config;

import com.meetsipdrink.chat.interceptor.ChattingHandShakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결을 위한 엔드포인트 설정 (SockJS 사용)
        registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:8080").withSockJS();
    }       //여기 setAllowedOrigins 어떤 도메인 쓸 건지

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 구독할 주제를 설정
        // /topic/room/{roomId}와 같은 다대다 채팅방 경로를 사용하도록 설정
        registry.enableSimpleBroker("/topic", "/queue");
        //메시지 브로커 활성화하고 클라이언트 메시지를 받을 수 있는 경로 지정
       //topic 은 다대다
        //방에 참여한 여러 사용자가 동일한 메시지를 받는 경우엔 /topic 사용
        //stompClient.subscribe('/topic/room/1', function (message) {
        //    // 방 번호 1에 있는 모든 사용자가 메시지를 받음
        //    console.log("Messag`e received in room 1: " + message.body);
        //});`
        //queue d은 일대일


        // 클라이언트에서 메시지를 보낼 때 사용하는 접두사
        // 예: /app/message 로 메시지를 보내고, 서버는 이를 처리한 후 /topic/room/{roomId}로 브로드캐스트
        registry.setApplicationDestinationPrefixes("/app");
        // "/app"으로 시작하는 메시지들은 메시지 매핑 핸들러로 라우팅됨.


    }


}
    //즉 다수에게 메세지를 보낼때는 '/topic/주소', 특정대상에게 메세지를 보낼 때는 '/queue/주소'의 방식을 택

//   Websocket handler 사용 위에는 stomp 사용
//    @Configuration
//    @EnableWebSocket
//    @RequiredArgsConstructor
//    public class WebSocketConfig implements WebSocketConfigurer {
//        private final ChatWebSocketHandler webSocketHandler;
//
//        @Override
//        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//            registry.addHandler(webSocketHandler, "/chat/rooms/*")
//                    .addInterceptors(handshakeInterceptor())
//                    .setAllowedOrigins("*");
//        }
//
//        @Bean
//        public HandshakeInterceptor handshakeInterceptor() {
//            return new ChattingHandShakeInterceptor();
//        }





