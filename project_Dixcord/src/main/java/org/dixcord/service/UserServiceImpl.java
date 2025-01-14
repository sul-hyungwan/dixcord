package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.UserVO;
import org.dixcord.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper mapper;
	
	@Transactional
	@Override
	public int registerUser(UserVO vo) {
		log.warn("서비스 유저 회원가입 : " + vo);
		// 1. 가입 유저 정보 insert
		int result = mapper.registerUser(vo);
		
		// 가입이 성공 했을 때
		if(result > 0) {
			// 2. 유저 번호 select
			int userCode = mapper.read(vo.getUserEmail()).getUserCode();
			// 선택된 관심사가 있을 때
			if(vo.getInterest().size() > 0) {
				vo.getInterest().forEach(inter -> {
					inter.setUserCode(userCode);
					// 관심사 insert
					int interenst = mapper.registerInterenst(inter);
					log.warn("관심사 추가 성공 유무 : " + interenst);
				}); 
			}
		}
		return result;
	}
	
	@Override
	public UserVO getUserDataByUserCode(int userCode) {
		return mapper.getUserDataByUserCode(userCode);
	}
	
	@Override
	public int emailCheck(String userEmail) {
		return mapper.emailCheck(userEmail);
	}
	
	@Override
	public int nickNameCheck(String userNickName) {
		return mapper.nickNameCheck(userNickName);
	}
	
	@Override
	public UserVO read(String userEmail) {
		return mapper.read(userEmail);
	}
	
	@Override
	public List<UserVO> getUserList() {
		return mapper.getUserList();
	}
	
	@Override
	public int updateLoginStateOnline(int userCode) {
		return mapper.updateLoginStateOnline(userCode);
	}
	
	@Override
	public int updateLoginStateOffline(int userCode) {
		return mapper.updateLoginStateOffline(userCode);
	}
	
	@Override
	public int updateKakaoId(UserVO vo) {
		return mapper.updateKakaoId(vo);
	}
	
	@Override
	public int userUpdateInfo(UserVO vo) {
		return mapper.userUpdateInfo(vo);
	}
	
	@Override
	public int passwordUpdate(UserVO vo) {
		return mapper.passwordUpdate(vo);
	}
	
	@Override
	public int passwordUpdateByEmail(UserVO vo) {
		return mapper.passwordUpdateByEmail(vo);
	}
	
	@Override
	public int setUpdateUserAuth(UserVO vo) {
		return mapper.setUpdateUserAuth(vo);
	}
	
	@Override
	public int deleteUserData(int userCode) {
		return mapper.deleteUserData(userCode);
	}

}
