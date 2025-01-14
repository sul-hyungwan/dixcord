package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.RoomMemberVO;
import org.dixcord.domain.RoomVO;
import org.dixcord.domain.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

public interface RoomService {

	// 모든 방 리스트 가져오기
	public List<RoomVO> getRoomListAll();

	// 방 번호로 방 정보 가져오기
	public RoomVO chatRoomInfo(int roomNumber);

	// 유저가 속해있는 방 리스트 가져오기
	public List<RoomVO> getChatList(int userCode);

	// 유저네임 가져오기? 이거는 없어도 될듯..?
	public String getUserName(int userCode);

	// 방 생성하기
	public int createRoom(RoomVO rvo);

	// 초대코드로 방 번호 조회하기
	public int isInvited(int roomInviteCode);

	// 방 번호로 방 맴버 리스트 가져오기
	public List<RoomMemberVO> getChatMember(int roomNum);

	// 유저코드로 유저 정보 가져오기
	public UserVO getMemberInfo(int userCode);

	// 방 맴버 추가하기 ( dico_together )
	public int addChatMember(RoomMemberVO rmvo);

	// 방 삭제하기
	public int deleteRoom(int roomNumber);

	// 유저가 속한 방 번호 가져오기
	public int[] joinedRoomNum(int userCode);

	// 유저가 참여한 방의 권한 가져오기
	public String getRoomAuth(int userCode, int roomNumber);
	
	//방장이 나가면 방장 다음으로 들어온 사람에게 방장 권한 이전
	public int updateHost(int roomNumber);
	
	
	//방 나간 후 참여 목록에서 유저 삭제(일반 유저일 때  방 나가기)
	public int exitRoom2(RoomMemberVO rmvo);
	
	//이미 방에 참가하고 있는지 조회
	public int alreadyJoin(RoomMemberVO rmvo);
	
	//방 정보 가져오기
	public RoomVO getRoomInfo(int roomNumber);
	
	//방 설정 업데이트
	public int settingUpdate(RoomVO rvo);
	
	//유저 닉네임들 불러오기
	public List<UserVO> getUserNickNames(List<Integer> userCodes);

	
	public int togetherCheck(RoomMemberVO rmvo);
	
	//초대코드 가져오기
	public int getInviteCode(int roomNumber);

}
