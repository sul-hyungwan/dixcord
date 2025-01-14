package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.ChatHistoryVO;
import org.dixcord.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	private ChatMapper mapper;
	
	@Override
	public List<ChatHistoryVO> getChatHistory(int textChatNo) {
		return mapper.getChatHistory(textChatNo);
	}
	
	@Override
	public int[] getTextChatNo(int roomNumber) {
		return mapper.getTextChatNo(roomNumber);
	}
	
	@Override
	public int createTextChat(int roomNumber) {
		return mapper.createTextChat(roomNumber);
	}
	
	@Override
	public int saveChatHistory(ChatHistoryVO vo) {
		return mapper.saveChatHistory(vo);
	}
	
	@Transactional
	@Override
	public int deleteChatHistory(ChatHistoryVO chat) {
		return mapper.deleteChatHistory(chat);
	}
	
	@Override
	public ChatHistoryVO getChatHistoryNow(int textChatNo) {
		return mapper.getChatHistoryNow(textChatNo);
	}
	
	@Override
	public ChatHistoryVO getChatHistoryUpdate(String chatGuid) {
		return mapper.getChatHistoryUpdate(chatGuid);
	}
	
	@Override
	public int saveEditedMessage(ChatHistoryVO vo) {
		return mapper.saveEditedMessage(vo);
	}
}
