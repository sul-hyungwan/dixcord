package org.dixcord.domain;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatHistoryVO {
	private int userCode, textChatNo;
	private String message, userNickName, userIcon;
	private String chatGuid;
	private String textDate;
	private String editText;
	private String chatImg;
	private String gifSrc;
	
}
