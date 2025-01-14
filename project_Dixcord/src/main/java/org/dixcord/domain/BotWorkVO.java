package org.dixcord.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BotWorkVO {
	private int userCode; // 유저 고유 코드
	private String userAuth; // 권한
	private int roomnumber; //방번호
	private String banword; //금지어
	private String chat; //채팅내역
	private String youtube; //유투브
	private int textChatNO; //채팅방 번호
	
	private List<String> banwordList; //밴단어
	
	
}
