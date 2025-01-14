package org.dixcord.mapper;

import java.util.List;

import org.dixcord.domain.ChatHistoryVO;

public interface ChatMapper {
	public List<ChatHistoryVO> getChatHistory(int textChatNo);
	
	public int[] getTextChatNo(int roomNumber);
	
	public int createTextChat(int roomNumber);
	
	public int saveChatHistory(ChatHistoryVO vo);
	
	public int deleteChatHistory(ChatHistoryVO chat);
	
	public ChatHistoryVO getChatHistoryNow(int textChatNo);

	public ChatHistoryVO getChatHistoryUpdate(String chatGuid);
	
	public int saveEditedMessage(ChatHistoryVO vo);
}
