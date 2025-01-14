package org.dixcord.controller;

import java.util.List;

import org.dixcord.domain.FriendVO;
import org.dixcord.domain.FriendWaitVO;
import org.dixcord.domain.RoomMemberVO;
import org.dixcord.domain.RoomVO;
import org.dixcord.domain.UserVO;
import org.dixcord.service.ChatRoomService;
import org.dixcord.service.ChatService;
import org.dixcord.service.FriendService;
import org.dixcord.service.RoomService;
import org.dixcord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
public class FriendController {
	
	@Autowired
	private FriendService service;
	
	@Autowired
	private UserService uservice;

	@Autowired
	private ChatService cservice;
	
	@Autowired
	private RoomService rservice;
	
	// 사이드 바 친구 목록 출력 
	// 방 번호 가져오기
	@GetMapping(value="/api/sideBarFriend", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<UserVO> getFriendList(int userCode) {
		log.warn("사이드바 친구 목록 출력");
		RoomMemberVO vo = new RoomMemberVO();
		vo.setUserCode(userCode);
		List<UserVO> list = service.getRoomNumber(vo);
		list.forEach(li -> {
			log.warn("대화목록 : " + li.getUserCode());
		});		
		return list;
	}
	
	// 친구 페이지 모든 친구 출력 (검색 기능 포함)
	@GetMapping("/api/friendAreaList")
	public List<FriendVO> getFriendList(@RequestParam String searchFriend, @RequestParam int userCode) {
	    FriendVO vo = new FriendVO();
	    vo.setUserCode(userCode);
	    vo.setSearchFriend(searchFriend);
	    List<FriendVO> friendList = service.getFriendListByNickName(vo);
	    return friendList;
	}
	
	// 친구 페이지 추천 친구 출력 (검색 기능 포함)
	@GetMapping("/api/recommendFriends")
	public List<FriendVO> getRecommendFriendList(@RequestParam String searchRecommendFriend, @RequestParam int userCode) {
		FriendVO vo = new FriendVO();
	    vo.setUserCode(userCode);
	    vo.setSearchRecommendFriend(searchRecommendFriend);
		List<FriendVO> friendRecomList = service.getRecommendFriends(vo);
		return friendRecomList;
	}
	
	// 친구 페이지 요청 친구 출력 (검색 기능 포함)
	@GetMapping("/api/requestFriends")
	public List<FriendWaitVO> getRequestFriendList(@RequestParam String searchRequestFriend, @RequestParam int friendCode) {
		FriendVO vo = new FriendVO();
	    vo.setFriendCode(friendCode);
	    vo.setSearchRequestFriend(searchRequestFriend);
		List<FriendWaitVO> friendRequestList = service.getRequestFriends(vo);
		return friendRequestList;
	}
	
	// 친구 페이지 대기 중 친구 출력 (검색 기능 포함)
	@GetMapping("/api/waitFriends")
	public List<FriendWaitVO> getWaitFriendList(@RequestParam String searchWaitFriend, @RequestParam int friendCode) {
		FriendVO vo = new FriendVO();
		vo.setFriendCode(friendCode);
	    vo.setSearchWaitFriend(searchWaitFriend);
		List<FriendWaitVO> friendWaitList = service.getWaitFriends(vo);
		return friendWaitList;
	}
	
	// 친구 페이지 - 차단 친구 출력
	@GetMapping("/api/blockFriends")
	public List<FriendVO> getblockFriendList(@RequestParam String searchBlockFriend, @RequestParam int friendCode) {
		FriendVO vo = new FriendVO();
		vo.setFriendCode(friendCode);
	    vo.setSearchBlockFriend(searchBlockFriend);
		List<FriendVO> blockFriendList = service.getBlockFriends(vo);
		return blockFriendList;
	}
	
	// 친구 삭제
	@Transactional
	@PostMapping("/api/deleteFriends")
	public String deleteFriend(@RequestBody FriendVO vo) {
		RoomMemberVO rmvo = new RoomMemberVO();
		rmvo.setRoomNumber(vo.getFriendCode() * vo.getUserCode());
		
		// 친구 삭제 처리
		int result = service.deleteFriend(vo);
		int roomresult = 0;

		// 방 번호로 방 멤버 리스트 가져오기
		List<RoomMemberVO> list = rservice.getChatMember(rmvo.getRoomNumber());
		
		if(list.size() > 0) {
			roomresult = service.deleteTogether(rmvo.getRoomNumber());
		}
				
		return result > 0 ? "success" : "fail";
	}	
	
	// 친구 추가
	@PostMapping("/api/addFriends")
	public String addFriend(@RequestBody FriendWaitVO vo) {
        // 친구 추가 처리
        int result = service.addFriend(vo);
        
        return result > 0 ? "success" : "fail";
	}
	
	// 친구 요청 취소
	@PostMapping("/api/cancelFriends")
	public String cancelFriend(@RequestBody FriendWaitVO vo) {		
		// 친구 요청 취소 처리
		int result = service.cancelFriend(vo);
		
		return result > 0 ? "success" : "fail";
	}
	
	// 대기 중인 친구 요청 수락
	@PostMapping("/api/acceptFriends")
	public String acceptFriend(@RequestBody FriendWaitVO vo) {		
		// 친구 추가 처리
		int result = service.acceptFriend(vo);
		
		return result > 0 ? "success" : "fail";
	}
	
	// 대기 중인 친구 요청 거절
	@PostMapping("/api/rejectFriends")
	public String rejectFriend(@RequestBody FriendWaitVO vo) {		
		// 친구 추가 처리
		int result = service.rejectFriend(vo);
				
		return result > 0 ? "success" : "fail";
	}
	
	// 친구 차단
	@PostMapping("/api/lockFriends")
	public String lockFriend(@RequestBody FriendVO vo) {				
		// 친구 차단 처리
		int result = service.lockFriend(vo);
		
		return result > 0 ? "success" : "fail";
	}
	
	// 친구 차단 해제
	@PostMapping("/api/unlockFriends")
	public String unlockFriend(@RequestBody FriendVO vo) {
		// 친구 추가 처리
		int result = service.unlockFriend(vo);
		
		return result > 0 ? "success" : "fail";
	}
	
	// 상세 친구 추가
	@PostMapping(value = "/api/addAllFriends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addAllFriend(@RequestBody FriendVO vo) {	    
	    // 1. 친구 목록에 있는 지 확인
	    int checkCount = service.checkFriendListByFriendCode(vo);
	    if(checkCount > 0) {
	        return "이미 친구에요";
	    }
	    // 2. 신청 목록에 있는 지 확인
	    int checkCount2 = service.checkRequestFriendListByFriendCode(vo);
	    if(checkCount2 > 0) {
	        return "이미 신청했어요";
	    }
	    // 3. 친구의 신청 목록에 있는 지 확인
	    int checkCount3 = service.checkRequestFriendListByFriendCode2(vo);
	    if(checkCount3 > 0) {
	        return "친구가 친구 요청 보냈어요";
	    }
	    // 4. 친구 추가 처리
	    int insertResult = service.addEmailFriend(vo);
	    if (insertResult > 0) {
	        return "친구 추가 완료";
	    } else {
	        return "친구 추가 중 오류 발생";
	    }
	}
	
	// 채팅방 이동
	@Transactional
	@GetMapping("/api/chatFriends")
	public int chatFriends(@RequestParam("roomNumber") int roomNumber, int userCode, int friendCode) {	
		// roomNumber로 친구 채팅방을 생성
		RoomVO room = rservice.chatRoomInfo(roomNumber);
		
		// 채팅방이 있으면
		if(room != null) {
			log.warn("roomNumber가 존재합니다");
			
			// dico_together 
			RoomMemberVO rmvo = new RoomMemberVO();
			rmvo.setRoomNumber(roomNumber);
			rmvo.setUserCode(userCode);
			rmvo.setAuth("친구");
			int togetherCheck = rservice.togetherCheck(rmvo);
			if(togetherCheck == 0) {
				int togetherResult = rservice.addChatMember(rmvo);
				rmvo.setUserCode(friendCode);
				togetherResult = rservice.addChatMember(rmvo);
			}
			// textChatNo 받아오기
			int[] textChatNo = cservice.getTextChatNo(roomNumber);
			// textChatNo 가 없을 경우 (채팅을 한 번도 안한 경우)
			if(textChatNo.length == 0) {
				// textChat 생성
				int createTextChat = cservice.createTextChat(roomNumber);
				// textChat 생성 성공 시 새로 생성된 textChat 번호 반환
	            if (createTextChat > 0) {
	            	textChatNo = cservice.getTextChatNo(roomNumber);
	                return textChatNo[0];  // 내가 생성한 방
	            } else {
	            	// textChat 생성 실패
	                log.error("채팅 생성에 실패했습니다.");
	                return -1; 
	            }
			}else {
				return textChatNo[0];				
			}
		}else {
			// 채팅방이 없으면 방 생성
			int createRoom = service.createRoom(roomNumber);
			
			if (createRoom > 0) {
	            // 방 생성 성공 시 새로운 채팅 생성
				// 이유 : 채팅방 자체가 없으면 애초에 textChat도 없을 테니까
	            int createTextChat = cservice.createTextChat(roomNumber);
	            
	            // dico_together 
				RoomMemberVO rmvo = new RoomMemberVO();
				rmvo.setRoomNumber(roomNumber);
				rmvo.setUserCode(userCode);
				rmvo.setAuth("친구");
				int togetherCheck = rservice.togetherCheck(rmvo);
				if(togetherCheck == 0) {
					int togetherResult = rservice.addChatMember(rmvo);
					rmvo.setUserCode(friendCode);
					togetherResult = rservice.addChatMember(rmvo);
				}

	            // textChat 생성 성공 시 
	            if (createTextChat > 0) {
	            	int[] textChatNo = cservice.getTextChatNo(roomNumber);
	                return textChatNo[0];  // 내가 생성한 방
	            } else {
	            	// textChat 생성 실패 시 
	                log.error("채팅 생성에 실패했습니다.");
	                return -1; 
	            }
	        } else {
	            log.error("방 생성에 실패했습니다.");
	            return -1; 
	        }			
		}		
	} 
	
}
