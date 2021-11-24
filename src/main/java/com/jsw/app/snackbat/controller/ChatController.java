package com.jsw.app.snackbat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jsw.app.snackbat.vo.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

   @MessageMapping("/chat")
   @SendTo("/topic/messages")
   public Message send(Message message) throws Exception {
       String time = new SimpleDateFormat("HH:mm").format(new Date());
       return new Message(message.getFrom(), message.getText(), time);
   }

}