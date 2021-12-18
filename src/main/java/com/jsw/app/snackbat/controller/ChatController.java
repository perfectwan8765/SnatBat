package com.jsw.app.snackbat.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.jsw.app.snackbat.repository.ChatRoomRepository;
import com.jsw.app.snackbat.vo.ChatRoom;
import com.jsw.app.snackbat.vo.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

    @Autowired 
    private SimpUserRegistry simpUserRegistry;
    
    @Autowired
    private ChatRoomRepository roomRepository;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @MessageMapping("/secured/chat")
    @SendTo("/secured/history")
    public Message send(Message message) throws Exception {
        return new Message(message.getFrom(), message.getText(), simpleDateFormat.format(new Date()), "send", null);
    }

    @MessageMapping("/secured/join")
    @SendTo("/secured/history")
    public Message join(Message message) throws Exception {
        Iterator<SimpUser> itr = simpUserRegistry.getUsers().iterator();
        List<String> userList = new ArrayList<>();

        while (itr.hasNext()) {
            userList.add(itr.next().getName());
        }
        
        log.info("userList: {}", userList);

        return new Message(message.getFrom(), null, simpleDateFormat.format(new Date()), "join", userList);
    }

    @GetMapping("/room")
    @ResponseBody
    public ResponseEntity<Object> createRoom (@RequestParam("roomname") String name) {
        // 같은 채팅방명으로 이미 존재한다면
        if (roomRepository.findRoomByName(name).isPresent()) {
            return ResponseEntity.badRequest().body("Exists Name");
        }

        return ResponseEntity.ok(roomRepository.createChatRoom(name));
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> getRoomList () {
        return roomRepository.findAllRoom();
    }

}