package org.dixcord.controller;

import java.util.List;

import org.dixcord.domain.AdminInquiryVO;
import org.dixcord.domain.InquiryVo;
import org.dixcord.service.AdminService;
import org.dixcord.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/admin/*")
public class AdminController {
	
	@Autowired
	private AdminService service;
	
	@Autowired
	private InquiryService inservice;
	
	@GetMapping(value = "/api/getAllInquiry", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<InquiryVo> getAllInquiry(){
		return service.getAllInquiry();
	}
	
	@PostMapping(value = "/api/setInquiryAdminMessage")
	public int setInquiryAdminMessage(@RequestBody AdminInquiryVO vo) {
		return service.setInquiryAdminMessage(vo);
	}
	
	@GetMapping(value = "/api/deleteInquiryByAdmin")
	public int deleteInquiryByAdmin(int inquiryNo) {
		log.warn("문의 삭제하기 위한 inquiryNo" + inquiryNo);
		return service.deleteInquiryByAdmin(inquiryNo);
	}
	
	@GetMapping(value = "/api/getAdminInquiryByInquiryNo")
	public AdminInquiryVO getAdminInquiryByInquiryNo(int inquiryNo) {
		log.warn("관리자 답변 가져오기 위한 inquiryNo" + inquiryNo);
		return service.getAdminInquiryByInquiryNo(inquiryNo);
	}
	
	@GetMapping(value = "/api/deleteRoom")
	public int deleteRoom(int roomNumber) {
		log.warn("방삭제 하기 위한 roomNumber : " + roomNumber);
		return service.deleteRoom(roomNumber);
	}
}
