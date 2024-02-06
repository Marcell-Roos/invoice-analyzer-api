package com.epiuse.invoiceanalyzerapi.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebsocketController {

	@MessageMapping("/progress.status")
	@SendTo("/topic/public")
	public Double sendProgress(@Payload Double progress) {
		System.out.println("Percentage: " +progress);
		return progress;
	}
}
