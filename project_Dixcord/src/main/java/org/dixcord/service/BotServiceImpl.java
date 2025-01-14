package org.dixcord.service;

import java.util.List;

import org.dixcord.domain.BotStatusD;
import org.dixcord.domain.BotStatusVO;
import org.dixcord.domain.BotWarnVO;
import org.dixcord.domain.BotWorkVO;
import org.dixcord.mapper.BotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;



@Log4j
@Service
public class BotServiceImpl implements BotService{
	
	@Autowired
	private BotMapper botmapper;
	
	
	
	@Override
	public int banword(BotWorkVO botVO) {
		log.warn("금지어 : " + botVO);		
		return botmapper.banword(botVO);
	}
	@Override
	public int bigbrother(BotWorkVO botVO) {
		log.warn("채팅 내역 : " + botVO);		
		return botmapper.bigbrother(botVO);
	}
	@Override
	public int help(BotWarnVO warnVO) {
		log.warn("유저 코드 : " + warnVO);		
		return botmapper.help(warnVO);
	}
	@Override
	public int kick(BotWorkVO botVO) {
		log.warn("유저 코드 : " + botVO);		
		return botmapper.kick(botVO);
	}
	@Override
	public int deathcount(BotWarnVO botWarnVO) {
		log.warn("유저 코드 : " + botWarnVO);		
		return botmapper.deathcount(botWarnVO);
	}
	@Override
	public List<String> banWordList(int roomnumber) {
		log.warn("유저 코드 : " + roomnumber);		
		return botmapper.banWordList(roomnumber);
	}
	@Override
	public int warning(int roomnumber) {
		log.warn("경고문 발싸 : " + roomnumber);		
		return botmapper.warning(roomnumber);
	}
	@Override
	public int deleteBanword(BotWorkVO botVO) {
		log.warn("금지어 삭제 : " + botVO);		
		return botmapper.deleteBanword(botVO);
	}
	@Override
	public int updateStatus(BotStatusD botstatusVO) {
		log.warn("체크박스 눌린듯 : " + botstatusVO);		
		return botmapper.updateStatus(botstatusVO);
	}
	@Override
	public int checkStatus(int roomnumber) {
		log.warn("체크박스 눌려서 확인 하러감 : " + roomnumber);		
		return botmapper.checkStatus(roomnumber);
	}
	@Override
	public int initStatus(int roomnumber) {
		log.warn("이 방에 처음 왔나벼 " + roomnumber);		
		return botmapper.initStatus(roomnumber);
	}
	@Override
	public BotStatusVO roomStatus(int roomnumber) {
		log.warn("방 소속 봇의 기능 체크 " + roomnumber);		
		return botmapper.roomStatus(roomnumber);
	}
	@Override
	public int indulgence(BotWorkVO botVO) {
		log.warn("당신의 죄를 사하겠어요 " + botVO);		
		return botmapper.indulgence(botVO);
	}
	@Override
	public int botFriendAdd(int userCode) {
		return botmapper.botFriendAdd(userCode);
	}
	@Override
	public int botFriendAdd2(int userCode) {
		return botmapper.botFriendAdd2(userCode);
	}

}
