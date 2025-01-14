package org.dixcord.controller;

import java.util.List;

import org.dixcord.domain.InquiryVo;
import org.dixcord.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
public class InquiryController {

    @Autowired
    private InquiryService service;

    @PostMapping("/inquiry")
    public ResponseEntity<String> createInquiry(@RequestBody InquiryVo inquiry) {
        log.info("1대1 문의 데이터 요청: " + inquiry);

        try {
            int result = service.inquiryInsert(inquiry);
            if (result > 0) {
                log.info("1대1 문의가 성공적으로 저장되었습니다.");
                return ResponseEntity.ok("문의가 성공적으로 등록되었습니다.");
            } else {
                log.warn("1대1 문의 저장 실패.");
                return ResponseEntity.status(500).body("문의 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("1대1 문의 등록 중 오류 발생: ", e);
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }
    
    @GetMapping("/mypage/myquestions/questionlist")
    public List<InquiryVo> getUserInquiries(int userCode) {
    	log.info("유저코드 : " + userCode);
    	log.warn("유저코드 : " + userCode);
        List<InquiryVo> ivo = service.userInquirieList(userCode);
        return ivo;
    }	
}
