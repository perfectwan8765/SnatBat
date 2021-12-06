package com.jsw.app.snackbat.interceptor;

import java.util.List;
import java.util.Map;

import com.jsw.app.snackbat.vo.AnonymousPrincipal;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomChannelInterceptor implements ChannelInterceptor {

    @Nullable
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getMessageType() == SimpMessageType.CONNECT) {
            MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);

            for (Map.Entry<String, List<String>> head : multiValueMap.entrySet()) {
                if ("name".equals(head.getKey())) {
                    AnonymousPrincipal principal = new AnonymousPrincipal();
                    principal.setName(head.getValue().get(0));
                    accessor.setUser(principal);
                }

                log.info("key:{}, value:{}", head.getKey(), head.getValue());
            }
        }

		return message;
    }

}