package org.dixcord.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FriendWaitVO {
	private int userCode; // 유저 고유 코드
	private int friendCode; // 친구 고유 코드
	private String userNickName; // 친구 닉네임
	private String userState; // 유저 로그인 상태
	private	String userIcon;
}
