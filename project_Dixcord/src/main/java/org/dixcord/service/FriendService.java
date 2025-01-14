package org.dixcord.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.dixcord.domain.FriendVO;
import org.dixcord.domain.FriendWaitVO;
import org.dixcord.domain.RoomMemberVO;
import org.dixcord.domain.UserVO;

public interface FriendService {
	// 사이드바 친구 목록 방 번호 가져오기
	public List<UserVO> getRoomNumber(RoomMemberVO vo);
	// 친구 페이지 - 모든 친구 출력
	public List<FriendVO> getFriendListByNickName(FriendVO vo);
	// 친구 페이지 - 추천 친구 출력
	public List<FriendVO> getRecommendFriends(FriendVO vo);
	// 친구 페이지 - 요청 친구 출력
	public List<FriendWaitVO> getRequestFriends(FriendVO vo);
	// 친구 페이지 - 대기 중 친구 출력
	public List<FriendWaitVO> getWaitFriends(FriendVO vo);
	// 친구 페이지 - 차단 친구 출력
	public List<FriendVO> getBlockFriends(FriendVO vo);
	// 친구 페이지 - 친구 삭제
	public int deleteFriend(FriendVO vo);
	// 친구 페이지 - 친구 대화방 삭제
	public int deleteTogether(int roomNumber);
	// 친구 페이지 - 친구 추가
	public int addFriend(FriendWaitVO vo);
	// 친구 페이지 - 친구 요청 취소
	public int cancelFriend(FriendWaitVO vo);
	// 친구 페이지 - 대기 중인 친구 요청 수락
	public int acceptFriend(FriendWaitVO vo);
	// 친구 페이지 - 대기 중인 친구 요청 거절
	public int rejectFriend(FriendWaitVO vo);
	// 친구 페이지 - 친구 차단 
	public int lockFriend(FriendVO vo);
	// 친구 페이지 - 친구 차단 해제
	public int unlockFriend(FriendVO vo);
	// 친구 페이지 - 상세 친구 추가
	public int checkFriendListByFriendCode(FriendVO vo);
	// 2. 신청 목록에 있는 지 확인
	public int checkRequestFriendListByFriendCode(FriendVO vo);
	// 3. 친구의 신청 목록에 있는 지 확인
	public int checkRequestFriendListByFriendCode2(FriendVO vo);
	// 4. 친구 추가
	public int addEmailFriend(FriendVO vo);	
	// 모든친구 - 채팅방 이동 - 채팅방 생성
	public int createRoom(int roomNumber);
}
