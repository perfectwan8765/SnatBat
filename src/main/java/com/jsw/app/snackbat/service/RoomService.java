package com.jsw.app.snackbat.service;

import java.util.List;

import com.jsw.app.snackbat.vo.ChatRoom;

public interface RoomService {

    public ChatRoom createRoom (String roomName, String userName);

    public List<ChatRoom> getChatRoomList ();
    
}