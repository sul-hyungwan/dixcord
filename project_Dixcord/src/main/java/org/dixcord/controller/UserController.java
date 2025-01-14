package org.dixcord.controller;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.dixcord.domain.UserVO;
import org.dixcord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/user/*")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private PasswordEncoder pwencoder;
	
	@Autowired
	private JavaMailSenderImpl sender;
	
	@PostMapping(value = "/api/register", produces = MediaType.TEXT_PLAIN_VALUE)
	public String userDataInsert(@RequestBody UserVO vo) {
		
		log.warn("회원가입컨트롤러 이메일" + vo.getUserEmail());
		log.warn("회원가입컨트롤러 입력한 비밀번호" + vo.getUserPassword());
		log.warn("회원가입컨트롤러 유저이름" + vo.getUserName());
		log.warn("회원가입컨트롤러 유저별명" + vo.getUserNickName());
		log.warn("회원가입컨트롤러 유저아이콘" + vo.getUserIcon());
		log.warn("회원가입컨트롤러 전화번호" + vo.getUserPhone());
		log.warn("회원가입컨트롤러 유저생일" + vo.getUserBirthday());
		if(vo.getInterest().size() > 0) {
			vo.getInterest().forEach(action -> {
				log.warn("회원가입 선택한 관심사 : " + action.getInterest());
			});;
		}
		
		// 이메일 중복 체크
		int emailCheck = service.emailCheck(vo.getUserEmail());
		if(emailCheck > 0) {
			return "emailCheckFail";
		}
		
		// 비밀번호 인코더
		vo.setUserPassword(pwencoder.encode(vo.getUserPassword()));
		log.warn("회원가입컨트롤러 인코더 후 비밀번호" + vo.getUserPassword());
		
		int insert = 0;
		insert = service.registerUser(vo);
		
		return insert > 0 ? "Join Success" : "Join Fail";
	}
	
	// 별명 중복 체크
	@GetMapping(value = "/api/nickNameCheck", produces = MediaType.TEXT_PLAIN_VALUE)
	public String nickNameCheck(@RequestParam String userNickName) {
		log.warn("userNickName : " + userNickName);
		int userNickNameCheck = service.nickNameCheck(userNickName);
		return userNickNameCheck > 0 ? "nickNameCheckFail" : "success nickName";
	}
	
	// 이메일 중복 체크
	@GetMapping(value = "/api/emailCheck", produces = MediaType.TEXT_PLAIN_VALUE)
	public String emailCheck(String userEmail) {
		int userEmailCheck = service.emailCheck(userEmail);
		return userEmailCheck > 0 ? "emailCheckFail" : "success email";
	}
	
	// 유저 접속 정보 업데이트 ( 온라인 )
	@GetMapping(value = "/api/loginState", produces = MediaType.TEXT_PLAIN_VALUE)
	public String loginStateUpdateOnline(@RequestParam int userCode) {
		log.warn("유저코드 : " + userCode);
		int result = service.updateLoginStateOnline(userCode);
		return result > 0 ? "success" : "fail";
	}
	
	// 유저 접속 정보 업데이트 ( 오프라인 )
	@GetMapping(value = "/api/loginStateOff", produces = MediaType.TEXT_PLAIN_VALUE)
	public String loginStateUpdateOffline(@RequestParam int userCode) {
		log.warn("유저코드 : " + userCode);
		int result = service.updateLoginStateOffline(userCode);
		return result > 0 ? "success" : "fail";
	}
	
	// 카카오 로그인 고유 ID 저장
	@PostMapping(value = "/api/updateKakaoId", produces = MediaType.TEXT_PLAIN_VALUE)
	public String updateKakaoId(@RequestBody UserVO vo) {
		log.warn("연동 카카오id : "+ vo.getKakaoId());
		log.warn("연동 유저코드 : "+ vo.getUserCode());
		int result = service.updateKakaoId(vo);
		return result > 0 ? "success" : "fail";
	}
	
	// 유저 정보 가져오기
	@GetMapping(value = "/api/getUserData")
	public UserVO getUserDataByUserCode(int userCode) {
		UserVO vo = service.getUserDataByUserCode(userCode);
		log.warn("결과 : " + vo);
		log.warn("닉네임 : " + vo.getUserNickName());
		return vo;
	}
	
	// 이미지 업로드
	@PostMapping(value = "/api/uploadFile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = ("content-type=multipart/*"))
	public String uploadAsyncPost(@RequestParam("uploadFile") MultipartFile uploadFile) {

		log.warn("이미지 업로드...");
		log.warn("이미지 업로드..." + uploadFile);
		
		String uploadPath = "\\\\192.168.0.140\\uploadImg/userImg";

		UUID uuid = null;

		if (uploadFile != null && !uploadFile.isEmpty()) {
			
			String uploadFileName = uploadFile.getOriginalFilename();
			
			try {

				if (uploadFileName != null) {
					uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);

					uuid = UUID.randomUUID();
					uploadFileName = uuid.toString() + "_" + uploadFileName;

					File saveFile = new File(uploadPath, uploadFileName);
					uploadFile.transferTo(saveFile);

					log.info("Uploaded file successfully: " + uploadFileName);
					log.info("Upload Path: " + uploadPath);
					

				}

			} catch (Exception e) {
				log.error("File upload error: ", e);
				return null;
			}
			
			return uploadFileName;

		} else {
			log.warn("File upload failed: no file provided or file is empty.");
			return null;
		}
	}
	
	
	// 유저 정보 업데이트
	@PostMapping(value = "/api/userUpdateInfo", produces = MediaType.TEXT_PLAIN_VALUE)
	public String userUpdateInfo(@RequestBody UserVO vo) {
		log.warn("유저 데이터 업데이트 : " + vo.getUserCode());
		log.warn("유저 데이터 업데이트 : " + vo.getUserNickName());
		log.warn("유저 데이터 업데이트 : " + vo.getUserEmail());
		log.warn("유저 데이터 업데이트 : " + vo.getUserIcon());
		log.warn("유저 데이터 업데이트 : " + vo.getBackGroundImg());
		int result = service.userUpdateInfo(vo);
		return result > 0 ? "success" : "fail";
	}
	
	// 유저 비밀번호 업데이트
	@PostMapping(value = "api/passwordUpdate", produces = MediaType.TEXT_PLAIN_VALUE)
	public String passwordUpdate(@RequestBody UserVO vo) {
		log.warn("유저 코드 : " + vo.getUserCode());
		log.warn("인코딩 전 변경할 비밀번호 : " + vo.getUserPassword());
		String userPassword = pwencoder.encode(vo.getUserPassword());
		log.warn("인코딩 후 변경할 비밀번호 : " + userPassword);
		vo.setUserPassword(userPassword);
		int result = service.passwordUpdate(vo);
		log.warn("업데이트 결과 : " + result);
		return result > 0 ? "success" : "false";
	}
	
	// 유저 비밀번호 업데이트 ( 이메일 인증 후 )
	@PostMapping(value = "api/passwordUpdateByEmail", produces = MediaType.TEXT_PLAIN_VALUE)
	public String passwordUpdateByEmail(@RequestBody UserVO vo) {
		log.warn("유저 코드 : " + vo.getUserEmail());
		log.warn("인코딩 전 변경할 비밀번호 : " + vo.getUserPassword());
		String userPassword = pwencoder.encode(vo.getUserPassword());
		log.warn("인코딩 후 변경할 비밀번호 : " + userPassword);
		vo.setUserPassword(userPassword);
		int result = service.passwordUpdateByEmail(vo);
		log.warn("업데이트 결과 : " + result);
		return result > 0 ? "success" : "false";
	}
	
	@GetMapping(value = "/api/getUserListAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<UserVO> getUserList(){
		List<UserVO> list = service.getUserList();
		return list;
	}
	
	// 유저 권한 업데이트
	@GetMapping(value = "/api/updateUserAuth")
	public String userUpdateAuth(String userAuth, int userCode) {
		log.warn("변경할 권한" + userAuth);
		log.warn("변경할 유저의 코드" + userCode);
		UserVO vo = new UserVO();
		vo.setUserCode(userCode);
		vo.setUserAuth(userAuth);
		int result = service.setUpdateUserAuth(vo);
		return result > 0 ? "success" : "false";
	}
	
	// 유저 삭제
	@GetMapping(value = "/api/deleteUserData")
	public String deleteUserData(int userCode) {
		log.warn("삭제할 유저의 코드" + userCode);
		return service.deleteUserData(userCode) > 0 ? "success" : "false";
	}
	
	@GetMapping(value = "/api/passwordResult")
	public String passwordResult(String userEmail) {
		log.warn("전달받은 이메일 : " + userEmail);
		UserVO vo = service.read(userEmail);
		
		if(vo != null) {
			SimpleMailMessage message = new SimpleMailMessage();
			Random ran = new Random();
			int num = ran.nextInt(999999);
			
			String sendMessage = vo.getUserName() + " 님, 안녕하세요. \n 비밀번호 재설정 요청을 주셔서 이메일"
					+ " 주소로 메시지를 발송하였습니다. \n"
					+ "요청한 분이 본인이 맞다면, 아래 인증번호를 입력 해 주세요. \n"
					+ "인증번호 : " + num + "\n 본 메일은 중요한 정보를 포함하고 있으므로, 메일 알람 수신에 동의하지 않으신 분들에게도 발송하고 있습니다. \n"
							+ "본 메일은 발신 전용 메일로 회신되지 않습니다.";
			
			message.setFrom("ehddnwn@naver.com");
			message.setTo(userEmail);
			message.setSubject("[Dixcord] 비밀번호 재설정 요청");
			message.setText(sendMessage);
			sender.send(message);
			return "" + num;
		}else {
			return "fail";
		}
	}
}
