package com.narangnorang.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

	@Autowired
	private MessageService messageService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/api/receive")
	@SendTo("/api/send")
	public void WebSocketHandler(Map<String, Object> map) throws Exception {
		System.out.println("before map : " + map);
		if ("message".equals(map.get("type"))) {
			messageService.sendMessage(map);
			simpMessagingTemplate.convertAndSend("/api/send/" + map.get("recieverId"), map);
		}
		System.out.println("after map : " + map);
	}

}
