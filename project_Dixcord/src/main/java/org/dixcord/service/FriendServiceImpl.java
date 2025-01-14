package org.dixcord.service;

import java.util.ArrayList;
import java.util.List;

import org.dixcord.domain.FriendVO;
import org.dixcord.domain.FriendWaitVO;
import org.dixcord.domain.RoomMemberVO;
import org.dixcord.domain.UserVO;
import org.dixcord.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class FriendServiceImpl implements FriendService {
	
	@Autowired
	private FriendMapper mapper;

	// 사이드바 친구 목록 출력
	@Override
	public List<UserVO> getRoomNumber(RoomMemberVO vo) {
		int[] roomNumbers = mapper.getRoomNumber(vo);
		List<UserVO> list = new ArrayList<>();
		for (int i : roomNumbers) {
			if(i > 9999) {
				vo.setRoomNumber(i);
				list.add(mapper.getFriendList(vo));
			}
		}
//		log.warn("대화목록 총 길이 : " + list.size());
		return list;
	}

	// 친구 페이지 - 모든 친구 출력
	@Override
	public List<FriendVO> getFriendListByNickName(FriendVO vo) {
		return mapper.getFriendListByNickName(vo);
	}

	// 친구 페이지 - 추천 친구 출력
	@Override
	public List<FriendVO> getRecommendFriends(FriendVO vo) {
		return mapper.getRecommendFriends(vo);
	}

	// 친구 페이지 - 요청 친구 출력
	@Override
	public List<FriendWaitVO> getRequestFriends(FriendVO vo) {
		return mapper.getRequestFriends(vo);
	}

	// 친구 페이지 - 대기 중 친구 출력
	@Override
	public List<FriendWaitVO> getWaitFriends(FriendVO vo) {
		return mapper.getWaitFriends(vo);
	}
	
	// 친구 페이지 - 차단 친구 출력
	@Override
	public List<FriendVO> getBlockFriends(FriendVO vo) {
		return mapper.getBlockFriends(vo);
	}

	// 친구 페이지 - 친구 삭제
	@Override
	public int deleteFriend(FriendVO vo) {
		try {
            // 1. dico_friend에 친구 삭제 (친구의 친구목록의 데이터 삭제 )
            int deleteResult = mapper.deleteFriend(vo);
            if (deleteResult == 0) {
                throw new RuntimeException("친구 삭제 실패");
            }
            
            // 2. dico_friend에 친구 삭제 ( 나의 친구목록의 데이터 삭제 )
            int userCode = vo.getFriendCode();	// 임시로 userCode에다가 내 코드 저장
            vo.setFriendCode(vo.getUserCode()); // 친구 코드에 친구 코드 저장
            vo.setUserCode(userCode);			// 임시로 저장한 내 코드를 내 코드에 저장
            deleteResult += mapper.deleteFriend(vo);
            if (deleteResult == 0) {
            	throw new RuntimeException("친구 삭제 실패");
            }
            
            // 성공 시 1 반환
            return 1;
        } catch (Exception e) {
            // 예외 발생 시 롤백
            throw new RuntimeException("친구 수락 처리 중 오류 발생", e);
        }
	}	
	
	// 친구 대화방 삭제
	@Override
	public int deleteTogether(int roomNumber) {
		return mapper.deleteTogether(roomNumber);
	}
	
	// 친구 페이지 - 친구 추가
	@Override
    public int addFriend(FriendWaitVO vo) {
        int result = mapper.addFriend(vo);
        return result;
    }

	// 친구 페이지 - 친구 요청 취소
	@Override
	public int cancelFriend(FriendWaitVO vo) {
		
		log.warn("친구 요청 취소 : " + vo.getFriendCode());
		log.warn("친구 요청 취소 : " + vo.getUserCode());
		
	    int result = mapper.cancelFriend(vo);
	    log.info("친구 요청 취소 결과: " + result);
	    if (result > 0) {
	        log.info("친구 요청 취소 성공");
	    } else {
	        log.info("친구 요청 취소 실패");
	    }
	    return result;
	}
	
	// 친구 페이지 - 대기 중인 친구 요청 수락
	@Transactional
	@Override
	public int acceptFriend(FriendWaitVO vo) {		
		try {
            // 1. dico_friend에 친구 관계 추가 ( 친구의 친구목록에 데이터 저장 )
            int insertResult = mapper.insertFriend(vo);
            if (insertResult == 0) {
                throw new RuntimeException("친구 추가 실패");
            }
            
            // 2. dico_friend에 친구 관계 추가 ( 나의 친구목록에 데이터 저장 )
            int userCode = vo.getFriendCode();	// 임시로 userCode 에다가 내 코드 저장
            vo.setFriendCode(vo.getUserCode()); // 친구 코드에 친구 코드 저장
            vo.setUserCode(userCode);			// 임시로 저장한 내 코드를 내 코드에 저장
            insertResult += mapper.insertFriend(vo);
            if (insertResult == 0) {
            	throw new RuntimeException("친구 추가 실패");
            }
            // 3. 친구 요청 삭제 (dico_request_friend에서)
            int deleteResult = mapper.deleteFriendRequest(vo);
            log.warn("deleteResult 의 값 " + deleteResult);
            if (deleteResult == 0) {
            	throw new RuntimeException("친구 요청 삭제 실패");
            }

            // 성공 시 1 반환
            return 1;
        } catch (Exception e) {
            // 예외 발생 시 롤백
            throw new RuntimeException("친구 수락 처리 중 오류 발생", e);
        }
	}

	// 친구 페이지 - 대기 중인 친구 요청 거절
	@Override
	public int rejectFriend(FriendWaitVO vo) {
		
		log.warn("대기 중 친구 거절 : " + vo.getFriendCode());
		log.warn("대기 중 친구 거절 : " + vo.getUserCode());
		
	    int result = mapper.rejectFriend(vo);
	    
	    log.info("대기 중 친구 거절 결과: " + result);
	    
	    if (result > 0) {
	        log.info("대기 중 친구 거절 성공");
	    } else {
	        log.info("대기 중 친구 거절 실패");
	    }
	    return result;
	}

	
	// 친구 페이지 - 친구 차단
	@Override
	public int lockFriend(FriendVO vo) {
		
		log.warn("친구 차단 : " + vo.getFriendCode());
		log.warn("친구 차단 : " + vo.getUserCode());
		
	    int result = mapper.lockFriend(vo);
	    
	    log.info("친구 차단 결과: " + result);
	    
	    if (result > 0) {
	        log.info("친구 차단 성공");
	    } else {
	        log.info("친구 차단 실패");
	    }
	    return result;
	}
		
	// 친구 페이지 - 친구 차단 해제
	@Override
	public int unlockFriend(FriendVO vo) {
		
		log.warn("친구 차단 해제 : " + vo.getFriendCode());
		log.warn("친구 차단 해제 : " + vo.getUserCode());
		
	    int result = mapper.unlockFriend(vo);
	    
	    log.info("친구 차단  해제 결과: " + result);
	    
	    if (result > 0) {
	        log.info("친구 차단 해제 성공");
	    } else {
	        log.info("친구 차단 해제 실패");
	    }
	    return result;
	}	
	
	// 친구 페이지 - 상세 친구 추가
	// 1. 친구 목록에 있는 지 확인
	@Override
	public int checkFriendListByFriendCode(FriendVO vo) {
		
		log.warn("친구 유무 확인: " + vo.getFriendCode());
		log.warn("친구 유무 확인 : " + vo.getUserCode());
		
		int result = mapper.checkFriendListByFriendCode(vo);
		
		log.info("친구 유무 결과: " + result);
	    
	    return result;
	}

	// 2. 신청 목록에 있는 지 확인
	@Override
	public int checkRequestFriendListByFriendCode(FriendVO vo) {
		log.warn("요청 친구 유무 확인: " + vo.getFriendCode());
		log.warn("요청 친구 유무 확인 : " + vo.getUserCode());
		
		int result = mapper.checkRequestFriendListByFriendCode(vo);
		
		log.info("친구 유무 결과: " + result);
		
		return result;
	}
	
	// 3. 친구의 신청 목록에 있는 지 확인
	@Override
	public int checkRequestFriendListByFriendCode2(FriendVO vo) {
		log.warn("요청 친구 유무 확인: " + vo.getFriendCode());
		log.warn("요청 친구 유무 확인 : " + vo.getUserCode());
		
		int result = mapper.checkRequestFriendListByFriendCode2(vo);
		
		log.info("친구 유무 결과: " + result);
		
		return result;
	}
	
	// 4. 친구 추가
	@Override
    public int addEmailFriend(FriendVO vo) {
        int result = mapper.addEmailFriend(vo);
        return result;
    }
	
	// 모든 친구 - 채팅방 이동 - 채팅방 생성
	@Override
	public int createRoom(int roomNumber) {
		int result = mapper.createRoom(roomNumber);
        return result;
	}
	
}
