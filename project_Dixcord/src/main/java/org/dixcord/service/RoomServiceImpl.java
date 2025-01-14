package org.dixcord.service;

import java.util.ArrayList;
import java.util.List;

import org.dixcord.domain.RoomMemberVO;
import org.dixcord.domain.RoomVO;
import org.dixcord.domain.UserVO;
import org.dixcord.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class RoomServiceImpl implements RoomService{
	
	
	@Autowired 
	private RoomMapper rmapper;
	
	@Override
	public RoomVO chatRoomInfo(int roomNumber) {
		return rmapper.getRoomInfo(roomNumber);
	}
	
	@Override
	public List<RoomVO> getChatList(int userCode){
		return rmapper.getChatList(userCode);
	}
	
	@Override
	public String getUserName(int userCode) {
		return rmapper.getUserName(userCode);
	}
	
	
	@Transactional
	@Override
	public int createRoom(RoomVO rvo) {
		log.info("createRoom...");
		
		//방 생성
		int result = rmapper.createRoom(rvo);
		
		//방 멤버에 방장 추가
		
		if (result > 0) {
			
			int roomNum = rvo.getRoomNumber();
			
			RoomMemberVO rmvo = new RoomMemberVO();
			rmvo.setRoomNumber(roomNum);
			rmvo.setUserCode(rvo.getUserCode());
			rmvo.setAuth("방장");
			
			int memberResult = rmapper.addChatMember(rmvo);
			
			return memberResult;
		}else {
			return 0;
		}
				
	}
	
	@Override
	public int isInvited(int roomInviteCode) {
		return rmapper.isInvited(roomInviteCode);
	}
	
	@Override
	public List<RoomMemberVO> getChatMember(int roomNum) {
		return rmapper.getChatMember(roomNum);
	}
	
	
	@Override
	public UserVO getMemberInfo(int userCode){
		return rmapper.getMemberInfo(userCode);
	}
	
	//채팅 방 생성 외 채팅 참여 유저 추가
	@Override
	public int addChatMember(RoomMemberVO rmvo) {
		return rmapper.addChatMember(rmvo);
	}
	
	
	//방 삭제(방장)
	@Transactional
	@Override
	public int deleteRoom(int roomNumber) {
		
		rmapper.deleteTogether(roomNumber);
		
		return rmapper.deleteRoom(roomNumber);
		
	}

	@Override
	public int[] joinedRoomNum(int userCode) {
		return rmapper.joinedRoomNum(userCode);
	}
	
	@Override
	public String getRoomAuth(int userCode, int roomNumber) {
		
		RoomMemberVO rmvo = new RoomMemberVO();
		rmvo.setUserCode(userCode);
		rmvo.setRoomNumber(roomNumber);
		
		return rmapper.getRoomAuth(rmvo);
		
	}
	
	@Override
	public List<RoomVO> getRoomListAll() {
		return rmapper.getRoomListAll();
	}
	
	
	@Transactional
	@Override
	public int updateHost(@PathVariable int roomNumber) {
		
		int firstCode = 0;
		int secondCode = 0;
		int result = 0;
		int result2 = 0;
		int roomNum = 0;
		//특정 방의 유저들 불러오기
		List<RoomMemberVO> members =  rmapper.getChatMember(roomNumber);
		log.warn("members" + members.get(0).getAuth());
		
		// 방장 혼자 - 방 삭제
		if(members.size() == 1) {
			
			result = 0;
			rmapper.deleteTogether(members.get(0).getRoomNumber());
			rmapper.deleteRoom(members.get(0).getRoomNumber());
			
			log.warn("방장 혼자");
			
			return 1;
		}
		
		// 방에 참가한 유저 수 ( 방장을 제외한 누군가가 있어야 이전 가능 )
		if(members.size() >= 2) {
			//제일 처음에 들어온 방장
			RoomMemberVO first = members.get(0);
			firstCode = first.getUserCode();
			log.warn("first" + first + firstCode);
			//방장 dico_together 에서 삭제
			rmapper.exitRoom(firstCode);
			
			
			//방장 다음으로 들어온 유저
			RoomMemberVO second = members.get(1);
			//secondCode 에 방장 다음으로 들어온 유저 코드 가져오기
			secondCode = second.getUserCode();
			//방장 다음으로 들어온 유저vo에 유저 코드 저장
			second.setUserCode(secondCode);
			second.setRoomNumber(members.get(1).getRoomNumber());
			log.warn("second" + second + secondCode);
			
			//dico_room 테이블 업데이트(userCode:개설자 코드)
			roomNum = second.getRoomNumber();
			result =rmapper.updateCreator(second);
			
			log.warn("result" + result);
			
			//권한 '방장'으로 업데이트
			result2 = rmapper.updateHost(secondCode);	
			log.warn("result2" + result2);
			
		}
		

		return result2;
	
	}
	
	//일반 유저일 때 방 나가기
	@Override
	public int exitRoom2(RoomMemberVO rmvo) {
		return rmapper.exitRoom2(rmvo);
	}
	
	
	//이미 참가하고 있는 방인지 조회하기
	@Override
	public int alreadyJoin(RoomMemberVO rmvo) {
		return rmapper.alreadyJoin(rmvo);
	}
	
	//방 정보 가져오기
	@Override
	public RoomVO getRoomInfo(int roomNumber) {
		return rmapper.getRoomInfo(roomNumber);

	}
	
	//방 설정 업데이트
	@Transactional
	@Override
	public int settingUpdate(RoomVO rvo) {
		
		int userCode = rvo.getUserCode();
		rvo.setUserCode(userCode);
		rmapper.updateHost(userCode);
		return rmapper.settingUpdate(rvo);
		
		
	}
	
	@Override
	public List<UserVO> getUserNickNames(List<Integer> userCodes) {
	    List<UserVO> userNickNames = new ArrayList<>();
	    
	    for (Integer userCode : userCodes) {
	        // 해당 userCode로 닉네임을 가져오는 과정 (DB에서 가져온다고 가정)
	        List<UserVO> nicknames = rmapper.getUserNickName(userCode);
	        
	        if (nicknames != null) {
	            // 모든 닉네임을 userNickNames에 추가
	            userNickNames.addAll(nicknames);
	        }
	    }

	    return userNickNames;
	}

	
	@Override
	public int togetherCheck(RoomMemberVO rmvo) {
		return rmapper.togetherCheck(rmvo);
	}

	
	@Override
	public int getInviteCode(int roomNumber) {
		return rmapper.getInviteCode(roomNumber);

	}

}



