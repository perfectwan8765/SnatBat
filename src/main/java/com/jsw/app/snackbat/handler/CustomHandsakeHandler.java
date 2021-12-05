package com.jsw.app.snackbat.handler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import com.jsw.app.snackbat.vo.AnonymousPrincipal;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomHandsakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		Principal principal = request.getPrincipal();

		log.info("handler map:{}", attributes);	

		if (principal == null) {
			principal = new AnonymousPrincipal();

			((AnonymousPrincipal)principal).setName(UUID.randomUUID().toString());
		}

		return principal;
	}

}