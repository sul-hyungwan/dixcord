package org.dixcord.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FriendVO {
	private int userCode; // 유저 고유 코드
	private int friendCode; // 친구 고유 코드
	private String friendAuth; // 권한
	private String friendMemo; // 친구메모
	private String userNickName; // 친구 닉네임
	private Date registerDate; // 가입일
	private String userState; // 유저 로그인 상태	
	private	String userIcon;
	
	// 유저 검색 시에 사용
	private String searchFriend;
	private String searchRequestFriend;
	private String searchRecommendFriend;
	private String searchWaitFriend;
	private String searchBlockFriend;
}
