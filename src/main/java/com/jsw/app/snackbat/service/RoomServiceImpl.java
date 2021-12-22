package com.jsw.app.snackbat.service;

import java.util.List;

import com.jsw.app.snackbat.repository.ChatRoomRepository;
import com.jsw.app.snackbat.vo.ChatRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {


    @Autowired
    private ChatRoomRepository roomRepository;

    @Override
    public ChatRoom createRoom (String roomName, String userName) {
        if (userName == null) {
            return null;
        }

        log.info("userName:{}", userName);

        // Duplicate Room Name Check
        if (roomRepository.findRoomByName(roomName).isPresent()) {
            return null;
        }

        return roomRepository.createChatRoom(roomName, userName);
    }

    @Override
    public List<ChatRoom> getChatRoomList () {
        return roomRepository.findAllRoom();
    }

    
}