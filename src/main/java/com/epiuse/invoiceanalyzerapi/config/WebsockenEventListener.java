package com.epiuse.invoiceanalyzerapi.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebsockenEventListener {
	
	
	 @EventListener
	    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
	        System.out.println("Received a new web socket connection");
	    }
	 
//	@EventListener
//	public void handleWebsocketDisconnectListener(
//			SessionDisconnectEvent event
//			) {
//		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wr
//	}

}
