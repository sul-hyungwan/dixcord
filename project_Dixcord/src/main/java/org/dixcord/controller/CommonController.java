package org.dixcord.controller;

import org.dixcord.domain.LoginRequest;
import org.dixcord.domain.UserVO;
import org.dixcord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/test/*")
public class CommonController {
	
	// 로그인 관련 컨트롤러
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private PasswordEncoder pwencoder;
	
	@PostMapping("/api/login")
	@ResponseBody
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
	    log.warn("로그인 요청: " + loginRequest.getUsername() + ", 비밀번호: " + loginRequest.getPassword());

	    try {
	        UserVO user = service.read(loginRequest.getUsername());
	        log.warn("유저 데이터 : " + user);
	        if (user == null) {
	            return new ResponseEntity<>("Invalid username", HttpStatus.UNAUTHORIZED);
	        }

	        // PasswordEncoder를 사용하여 비밀번호 검증
	        if (!pwencoder.matches(loginRequest.getPassword(), user.getUserPassword())) {
	            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
	        }

	        // 인증 완료 시 SecurityContext에 저장
	        UsernamePasswordAuthenticationToken authenticationToken = 
	                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

	        Authentication authentication = authenticationManager.authenticate(authenticationToken);
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        return new ResponseEntity<>("Login successful", HttpStatus.OK);
	    } catch (AuthenticationException e) {
	        log.error("로그인 실패: " + e.getMessage());
	        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
	    }
	}

	
	@ResponseBody
	@GetMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Authentication getUser() {
		// 현재 인증된 사용자 정보(Authentication 객체)를 반환
	  return SecurityContextHolder.getContext().getAuthentication();
	}
	
	@ResponseBody
	@PostMapping("/api/logout")
	public ResponseEntity<String> logout() {
		
		SecurityContextHolder.clearContext();
		
		return new ResponseEntity<String>("Logout successful" , HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/api/emailCheck")
	public UserVO emailCheck(String email) {
		log.warn("이메일 췌크 : " + email);
		UserVO vo = service.read(email);
		log.warn("결과 : " + vo);
		return vo != null ? vo : null;
	}
	
	@GetMapping("/customLogout")
	public String customLogout() {
		return "/";
	}
	
	@ResponseBody
	@PostMapping("/api/kakao/login")
	public ResponseEntity<String> kakaoLogin(@RequestBody String kakaoId) {
		
		log.warn("카카오 리퀘스트 탔음");
		log.warn("카카오 리퀘스트 : " + kakaoId);
		
	    try {
	        UsernamePasswordAuthenticationToken authenticationToken = 
	                new UsernamePasswordAuthenticationToken(kakaoId, null);

	        Authentication authentication = authenticationManager.authenticate(authenticationToken);

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        return new ResponseEntity<>("Kakao login successful", HttpStatus.OK);
	    } catch (AuthenticationException e) {
	        return new ResponseEntity<>("Kakao login failed", HttpStatus.UNAUTHORIZED);
	    }
	}


	

}
