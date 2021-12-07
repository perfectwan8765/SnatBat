package com.jsw.app.snackbat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jsw.app.snackbat.vo.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

    @Autowired 
    private SimpUserRegistry simpUserRegistry;

    @MessageMapping("/secured/chat")
    @SendTo("/secured/history")
    public Message send(Message message) throws Exception {

        simpUserRegistry.getUsers().stream()
                    .map(u -> u.getName())
                    .forEach(log::info);
    

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new Message(message.getFrom(), message.getText(), time);
    }

}