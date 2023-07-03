package com.harshit.redisspring.chat.service;

import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ChatRoomService implements WebSocketHandler {

    @Autowired
    private RedissonReactiveClient client;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String room = getChatRoom(session);
        RTopicReactive topic = client.getTopic(room, StringCodec.INSTANCE);
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(topic::publish)
                .doOnError(System.out::println)
                .doFinally(s-> System.out.println("subscriber finally "+s))
                .subscribe();
        Flux<WebSocketMessage> sendMessage = topic.getMessages(String.class)
                .map(session::textMessage)
                .doOnError(System.out::println)
                .doFinally(s-> System.out.println("publisher finally "+s));

        return session.send(sendMessage);
    }

    private String getChatRoom(WebSocketSession session){
        URI uri = session.getHandshakeInfo().getUri();
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room","default");
    }
}
