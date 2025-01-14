package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.UserVO;

public interface UserService {
	// 유저 회원가입
	public int registerUser(UserVO vo);
	// 이메일 중복체크
	public int emailCheck(String userEmail);
	// 닉네임 중복체크
	public int nickNameCheck(String userNickName);
	// 유저 코드로 유저정보 가져오기
	public UserVO getUserDataByUserCode(int userCode);
	// 이메일로 유저정보 가져오기
	public UserVO read (String userEmail);
	// 모든 유저 리스트 가져오기
	public List<UserVO> getUserList();
	// 유저 상태 온라인으로 변경
	public int updateLoginStateOnline(int userCode);
	// 유저 상태 오프라인으로 변경
	public int updateLoginStateOffline(int userCode);
	// 카카오 연동 업데이트하기
	public int updateKakaoId(UserVO vo);
	// 유저 정보 변경
	public int userUpdateInfo(UserVO vo);
	// 비밀번호 변경
	public int passwordUpdate(UserVO vo);
	// 유저 비밀번호 변경 ( 이메일 인증 )
	public int passwordUpdateByEmail(UserVO vo);
	// 유저 권한 변경
	public int setUpdateUserAuth(UserVO vo);
	// 유저 삭제
	public int deleteUserData(int userCode);
}
