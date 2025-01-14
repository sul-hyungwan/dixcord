package org.dixcord.controller;

import java.util.List;

import org.dixcord.domain.BanWordVO;
import org.dixcord.domain.BotStatusD;
import org.dixcord.domain.BotStatusVO;
import org.dixcord.domain.BotWarnVO;
import org.dixcord.domain.BotWorkVO;
import org.dixcord.domain.RoomVO;
import org.dixcord.service.BotService;
import org.dixcord.service.ChatService;
import org.dixcord.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/api/bot")
public class BotController {
	
	@Autowired
	BotService botservice;
	
	@Autowired
	ChatService cservice;
	
	@Autowired
	RoomService rservice;
	

	
	@PostMapping("/banword")
	public ResponseEntity<String> banword(@RequestBody BanWordVO banwordvo ) {
		log.warn("밴 단어 :  " + banwordvo.getBanword());
		log.warn("밴 단어 :  " + banwordvo.getRoomnumber());
		BotWorkVO botVO = new BotWorkVO();
		botVO.setBanword(banwordvo.getBanword());
		botVO.setRoomnumber(banwordvo.getRoomnumber());
		int result = botservice.banword(botVO);
		//밴+방번호 테이블 ★
		// 봇 서비스 만들어서 집어 넣기★
		return ResponseEntity.ok("Banword received");
	}
	@PostMapping("/deleteBanword")
	public ResponseEntity<String> deleteBanword(@RequestBody BanWordVO banwordvo ) {
		log.warn("밴 단어 :  " + banwordvo.getBanword());
		log.warn("밴 단어 :  " + banwordvo.getRoomnumber());
		BotWorkVO botVO = new BotWorkVO();
		botVO.setBanword(banwordvo.getBanword());
		botVO.setRoomnumber(banwordvo.getRoomnumber());
		int result = botservice.deleteBanword(botVO);
		//밴+방번호 테이블 ★
		// 봇 서비스 만들어서 집어 넣기★
		return ResponseEntity.ok("Banword deleted");
	}
	
	@GetMapping(value = "/banWordList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> banwordList(int roomnumber) {
		log.warn("밴리스트 api 탔어요 !!!  ");
		log.warn("roomnumber roomnumber roomnumber !roomnumber!  " + roomnumber);
		List<String> wordList = botservice.banWordList(roomnumber);
		log.warn("wordList wordList wordList  " + wordList);
		//밴+방번호 테이블 ★
		// 봇 서비스 만들어서 집어 넣기★
	    return wordList;
	}
	@GetMapping(value = "/roomStatus",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BotStatusVO roomStatus(int roomnumber) {
		BotStatusVO botstatusVO = botservice.roomStatus(roomnumber);
		log.warn(botstatusVO);	
		
		return botstatusVO;
	}
	
	
	@PostMapping("/bigbrother")
	public String bigbrother( @RequestBody BotWorkVO botVO ) {
		/*
		log.warn("빅브라더 :  " + chat);
		String zip = chat.replaceAll("\\s", "");
		//띄어쓰기 제거 작업 ★
		log.warn("줄어든 문장 :  " + zip);
		List<String> banWord = botservice.banWordList(roomnumber);
		log.warn("금지어 목록 :  " + banWord);
		//금지어 목록 가져오기
		boolean containsBanword = banWord.stream().anyMatch(zip::contains);
		//금지어 있는지 확인해서 1,2 반환
		*/ //react에서 처리함
		BotWarnVO botWarnVO = new BotWarnVO();
		botWarnVO.setRoomnumber(botVO.getRoomnumber());
		botWarnVO.setUserCode(botVO.getUserCode());
		
		log.warn("빅브라더가 눈을 뜹니다 :  " + botVO);
		
		RoomVO rvo = rservice.chatRoomInfo(botVO.getRoomnumber());
		if(rvo.getUserCode() == botVO.getUserCode()) {
			return "creater not";
		}
		
		int result = botservice.bigbrother(botVO);
		log.warn("경고 성공 :  " + result);
		//int successWarnMessage = botservice.warning(botVO.getRoomnumber());
		//update =기존 채팅 가리기 ? 호연이 꺼로 삭제 진행 가능? => 그냥 서버 가기전에 실행으로 조아쓰
		//insert = roomnumber로 채팅방에 경고★
		int warnCount = botservice.deathcount(botWarnVO);
		log.warn("경고 횟수 : " + warnCount);
		if(warnCount >= 4) {
			int kick = botservice.kick(botVO);
			log.warn("강퇴 성공 :  " + kick);		
			int indulgence = botservice.indulgence(botVO);
		}
		//채팅 창에서 변화가 일어 날때 마다 작동.★
		//채팅 내용을 단어 별로 잘라서검사 진행.★
		//경고횟수가 3이면 강제 퇴장.★
		//db에일치하는 게 있으면 경고 후 삭제★
		//구분자 0일시 킥 기능 막고, 구분자 1이면 킥활성화 => 나중에 만들기.
		
		//모든 작업이 성공 했을때 final 값이 1 이상이면 success return, 실패시 return fail.
		return "Bigbrother watch you";
	}
	
	@PostMapping("/updateStatus")
	public String updateStatus(@RequestBody BotStatusD botstatusVO) {
		log.warn(botstatusVO.getRoomnumber());
		log.warn(botstatusVO.getStatusType());
		log.warn(botstatusVO.getValue());		
		int checkresult = botservice.checkStatus(botstatusVO.getRoomnumber());
		if(checkresult>0) {
			int updateResult = botservice.updateStatus(botstatusVO);			
		}else {
			int initResult = botservice.initStatus(botstatusVO.getRoomnumber());
			int updateResult = botservice.updateStatus(botstatusVO);			
		}		
		return "update complete";
	}
	
	@PostMapping("/helpMe")
	   public String helpMe(@RequestBody BotWarnVO warnvo) {
	      log.warn("help!!  " +  warnvo.getUserCode());
	      log.warn("help!!  " +  warnvo.getRoomnumber());
	      int friendResult = botservice.botFriendAdd(warnvo.getUserCode());
	      int friendResult2 = botservice.botFriendAdd2(warnvo.getUserCode());
	      //textchatnumber 찾기
	      int[] textChatNo = cservice.getTextChatNo(warnvo.getRoomnumber());
	      //기존 방이 있다면
	      if(textChatNo.length > 0) {
	         //roomnumber에 저장
	         warnvo.setRoomnumber(textChatNo[0]);
	         //설명서 전달
	         int helpResult = botservice.help(warnvo);
	         //기존 방이 없다면
	      }else {
	         //방 생성
	         int textChatNoInsert = cservice.createTextChat(warnvo.getRoomnumber());
	         //방생성이 성공하면?
	         if(textChatNoInsert > 0) {
	            //방 번호 다시 찾아서
	            textChatNo = cservice.getTextChatNo(warnvo.getRoomnumber());
	            //방번호 지정해주고
	            warnvo.setRoomnumber(textChatNo[0]);            
	            log.warn("저장된 RoomNuber 값 : " + warnvo.getRoomnumber());
	            //설명서 전달
	            int helpResult = botservice.help(warnvo);
	         }else {
	            return "fail";
	         }
	      }      
	      return "helpMe message you";
	   }




}
