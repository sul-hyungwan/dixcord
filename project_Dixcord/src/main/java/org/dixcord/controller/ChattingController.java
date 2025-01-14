package org.dixcord.controller;

import org.dixcord.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/chatting/*")
public class ChattingController {
	
	@Autowired
	ChatService service;
	
	@GetMapping("/api/getTextChatNo")
	public int getTextChatNo(int roomNumber) {
		log.warn("roomNumber : " + roomNumber);
		int[] textChatNo = service.getTextChatNo(roomNumber);
		log.warn("채팅방 검색 결과 크기 : " + textChatNo.length);
		if(textChatNo.length == 0) {
			int result = service.createTextChat(roomNumber);
			if(result > 0) {
				textChatNo = service.getTextChatNo(roomNumber);
			}else {
				return 0;
			}
		}
		log.warn("채팅방 검색 결과 인덱스 0번 : " + textChatNo[0]);
		return textChatNo[0];
	}
}
