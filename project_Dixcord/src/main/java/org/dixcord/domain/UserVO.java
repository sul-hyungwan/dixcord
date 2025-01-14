package org.dixcord.domain;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
	private int userCode; // 유저 고유 코드
	private String userEmail; // 유저 이메일
	private String userPassword; // 유저 비밀번호
	private String userName; // 유저 이름
	private String userNickName; // 유저 별명
	private String userIcon; // 유저 아이콘
	private String userPhone; // 유저 전화번호
	private Date userBirthday; // 유저 생일
	private Date registerDate; // 가입일
	private String userAuth; // 권한
	private String userState; // 로그인상태
	private String userStateOption; // 유저 상태 설정
	private String userSearchOption; // 유저 검색 설정
	private String userMessageOption; // 유저 메시지 설정
	private String kakaoId; // 카카오 고유 id
	private String socialLogin; // 소셜 로그인 정보
	private String backGroundImg; // 유저 백그라운드 이미지 ( 배경 이미지 )
	
	private List<UserInterestVO> interest;
}
