package com.jsw.app.snackbat.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import com.jsw.app.snackbat.vo.ChatRoom;

import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomRepository {

    private ConcurrentHashMap<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new ConcurrentHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.sort(chatRooms);

        return chatRooms;
    }

    public ChatRoom findRoomById (String id) {
        return chatRoomMap.get(id);
    }

    public Optional<ChatRoom> findRoomByName (String name) {
        for (String id : chatRoomMap.keySet()) {
            ChatRoom chatRoom = chatRoomMap.get(id);
            if (name.equals(chatRoom.getName())) {
                return Optional.of(chatRoom);
            }
        }

        return Optional.empty();
    }

    public ChatRoom createChatRoom (String name, String admin) {
        ChatRoom chatRoom = ChatRoom.create(name, admin);
        chatRoomMap.put(chatRoom.getRooId(), chatRoom);

        return chatRoom;
    }
    
}