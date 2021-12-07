package com.jsw.app.snackbat.interceptor;

import com.jsw.app.snackbat.util.JwtTokenProviderUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class CustomChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtTokenProviderUtil jwtTokenProviderUtil;

    @Nullable
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            String jwt = accessor.getFirstNativeHeader("token");
            // jwtTokenProviderUtil.validateToken(jwt);
        }

		return message;
    }

}