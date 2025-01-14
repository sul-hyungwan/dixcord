package org.dixcord.controller;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.dixcord.domain.RoomMemberVO;
import org.dixcord.domain.RoomVO;
import org.dixcord.domain.UserVO;
import org.dixcord.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService crservice;

	// Authentication authentication =
	// SecurityContextHolder.getContext().getAuthentication(). getPrincipal();

	// UserDetails userDetails = (UserDetails)Principal;

	@GetMapping("/chatRoomInfo/{roomNumber}")
	public ResponseEntity<?> chatRoomInfo(@PathVariable int roomNumber) {
		RoomVO result = crservice.chatRoomInfo(roomNumber);

		if (result == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("chatInfo not found");
		}
		return ResponseEntity.ok(result);
	}
	

	@GetMapping("/getChatList/{userCode}")
	public List<RoomVO> chatList(@PathVariable int userCode) {
		
		//1번. userCode로 together 테이블 조회
		int[] resultNum = crservice.joinedRoomNum(userCode);
		
		//2. 가져 온 roomNumber 로 roomList 조회
		List<RoomVO> list = new ArrayList<>();
		if(resultNum.length > 0) {
			for (int i = 0; i < resultNum.length; i++) {
				list.add(crservice.chatRoomInfo(resultNum[i]));
			}
		}
		
		
		if(list.size() > 0) {
			list.forEach(action -> {
			});
		}
		

		return list.size() > 0 ? list : null;
	}

	@GetMapping(value = "/getUserName/{userCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getUserName(@PathVariable int userCode) {

		String userName = crservice.getUserName(userCode);

		return userName;

	}

	
	
	@PostMapping(value = "/uploadFile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = ("content-type=multipart/*"))
	public String uploadAsyncPost(@RequestParam("uploadFile") MultipartFile uploadFile) {

		String uploadPath = "\\\\192.168.0.140\\uploadImg/creator/";

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

				}

			} catch (Exception e) {
				return null;
			}
			
			return uploadFileName;

		} else {
			return null;
		}

		

	}
	
	@PostMapping(value = "/uploadFile2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = ("content-type=multipart/*"))
	public String uploadAsyncPost2(@RequestParam("uploadFile2") MultipartFile uploadFile) {

		String uploadPath = "\\\\192.168.0.140\\uploadImg/backgroundImg";

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
				return null;
			}
			
			return uploadFileName;

		} else {
			return null;
		}

		

	}
	

	
	        
	@PostMapping(value = "/createRoom",produces =  MediaType.TEXT_PLAIN_VALUE)
	public String createRoom(@RequestBody RoomVO rvo) {
		
		
		int result = crservice.createRoom(rvo);
		
		if(result > 0) {
			return (String) "success";
		}else {
			return (String) "fail";
		}
	}
	
	
	@GetMapping("/isInvited")
	public int isInvited(int putCode) {
		
		return crservice.isInvited(putCode);
		
	}
	

	@GetMapping("/getChatMember/{roomNum}")
	public List<RoomMemberVO> getChatMember(@PathVariable int roomNum) {
		
		List<RoomMemberVO> memberList = crservice.getChatMember(roomNum);
		
		return memberList;
	}
	
	@GetMapping(value = "/getMemberInfo/{userCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	public UserVO getMemberInfo(@PathVariable int userCode) {
		
		UserVO memberInfoList = crservice.getMemberInfo(userCode);
		
		return memberInfoList;
		
	}
	
	//채팅 방 생성 외 채팅 참여 유저 추가
	@PostMapping("/addChatMember")
	public int addChatMember(@RequestBody RoomMemberVO rmvo) {
		
		//이미 방에 참가하고 있는지 조회
		 int alreadyJoin = crservice.alreadyJoin(rmvo);
		 
		 if (alreadyJoin>0) {
			return 999;
		}

		
		
		return crservice.addChatMember(rmvo);
	}
	
	
	
	@DeleteMapping("/deleteRoom/{roomNumber}")
	public int deleteRoom(@PathVariable int roomNumber) {
		
		return crservice.deleteRoom(roomNumber);
	}
	
	
	@GetMapping(value = "/getRoomAuth/{userCode}/{roomNumber}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getRoomAuth(@PathVariable int userCode, @PathVariable int roomNumber) {
		String auth = crservice.getRoomAuth(userCode, roomNumber);
		return auth;
	}
	
    @GetMapping("/allList")
    public List<RoomVO> getRoomListAll(){
       return crservice.getRoomListAll();
    }
    
    @GetMapping(value = "/updateHost/{roomNumber}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int updateHost(@PathVariable int roomNumber) {
    	
    	
    	int result = crservice.updateHost(roomNumber);
    	
    	return result;
    	
    	
    }
    
    
    @PostMapping(value = "/exitRoom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int exitRoom(@RequestBody RoomMemberVO rmvo) {
    	
    	RoomMemberVO vo = new RoomMemberVO();
    	vo.setUserCode(rmvo.getUserCode());
    	vo.setRoomNumber(rmvo.getRoomNumber());
    	
    	int result = crservice.exitRoom2(rmvo);
    	
    	return result;
    }
    
    
    @GetMapping(value = "/getSettingData/{roomNumber}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RoomVO getSettingData(@PathVariable int roomNumber) {
	    
	    RoomVO getSettingData = crservice.getRoomInfo(roomNumber);

	    return getSettingData;
    }
    
    
    
    @GetMapping(value = "/getSettingMember/{roomNum}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RoomMemberVO> getSettingMember(@PathVariable int roomNum) {
    	
    	List<RoomMemberVO> getSettingMember = crservice.getChatMember(roomNum);
    	
    	
    	return getSettingMember;
    }
    
    @PostMapping(value = "/settingUpdate",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String settingUpdate(@RequestBody RoomVO rvo) {
    	  
    	int settingUpdate = crservice.settingUpdate(rvo);
    	
    	if (settingUpdate > 0) {
			return "success";
		}else {
			return "false";
		}

    }
    
    @PostMapping(value = "/getUserNickNames", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public List<UserVO> getUserNickNames(@RequestBody List<Integer> userCodes) {
    	
    	List<UserVO> userNickNames  = crservice.getUserNickNames(userCodes);
    	
    	return userNickNames;
    }
    
    
    @GetMapping("/getInviteCode/{roomNumber}")
    public int getInviteCode(@PathVariable int roomNumber) {
    
    	int inviteCode = crservice.getInviteCode(roomNumber);
    	
    	return inviteCode;
    }
    
}
