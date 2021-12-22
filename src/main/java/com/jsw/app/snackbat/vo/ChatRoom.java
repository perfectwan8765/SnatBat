package com.jsw.app.snackbat.vo;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom implements Comparable<ChatRoom> {

    private String rooId;
    private String name;
    private String admin;
    private Date createdDate;

    public static ChatRoom create (String name, String admin) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.rooId = UUID.randomUUID().toString();
        chatRoom.createdDate = new Date(System.currentTimeMillis());
        chatRoom.name = name;
        chatRoom.admin = name;

        return chatRoom;
    }

    @Override
    public int compareTo(ChatRoom chatRoom) {
        int compareDate = this.createdDate.compareTo(chatRoom.createdDate);

        if (compareDate == 0) {
            return this.name.compareTo(chatRoom.name);
        }

        return compareDate;
    }
    
}