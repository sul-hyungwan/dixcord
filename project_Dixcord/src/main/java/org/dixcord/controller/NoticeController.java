package org.dixcord.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dixcord.domain.NoticeAttachVO;
import org.dixcord.domain.NoticeVO;
import org.dixcord.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
public class NoticeController {
	
	@Autowired
	private NoticeService service;
	
	
	@GetMapping("noticeList")
	public List<NoticeVO> noticeList() {
		log.info("list...");
		List<NoticeVO> nvo = service.noticeList();
		return nvo;
	}
	
	@GetMapping(value = "/service/notice/{idx}", produces = MediaType.APPLICATION_JSON_VALUE)
	public NoticeVO get(@PathVariable int idx) {
		log.info("get..." + idx);
		NoticeVO nvo = service.read(idx);
		log.info("게시글 번호 " + nvo);
		return nvo;
	}
	
	@PostMapping(value = "/service/notice/modify/{idx}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String modifyNotice(@RequestBody NoticeVO nvo) {
        int isSuccess = service.modify(nvo);
        log.info("수정 내용 " + nvo);
        if (isSuccess > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
	
	@GetMapping(value = "/noticeDelete/{idx}", produces = MediaType.APPLICATION_JSON_VALUE)
	public int deleteNotice(@PathVariable int idx) {
		log.info("get..." + idx);
		return service.delete(idx);
	}
	
	@PostMapping(value = "/NoticeWrite", produces = MediaType.APPLICATION_JSON_VALUE)
	public String insertNotice(@RequestBody Map<String, Object> data) {

	    Map<String, Object> notice = (Map<String, Object>) data.get("inputs");
	    Map<String, Object> files = (Map<String, Object>) data.get("uploadedFiles");

	    int usercode = (int) notice.get("userCode");
	    String title = (String) notice.get("title");
	    String writer = (String) notice.get("writer");
	    String content = (String) notice.get("content");

	    NoticeVO nvo = new NoticeVO();
	    nvo.setUsercode(usercode);
	    nvo.setTitle(title);
	    nvo.setWriter(writer);
	    nvo.setContent(content);

	    NoticeAttachVO navo = null;
	    if (files != null) { // 파일 정보가 있을 때만 처리
	        navo = new NoticeAttachVO();
	        navo.setUuid((String) files.get("uuid"));
	        navo.setUploadPath((String) files.get("uploadPath"));
	        navo.setUploadName((String) files.get("uploadName"));
	    }

	    log.warn(files);
	    service.insert(nvo, navo);

	    return "test";
	}

	@GetMapping(value = "/getAttachList/{idx}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<NoticeAttachVO>> getAttachList(@PathVariable("idx") int idx) {
		log.info("get 게시판 업로드 파일 리스트... " + idx);

		service.getAttachList(idx).forEach(action -> {
			log.info("업로드" + action);
		});
		log.warn("아타치 등록 결과 : " + idx);
		return new ResponseEntity<List<NoticeAttachVO>>(service.getAttachList(idx), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getNoticeTitle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getNoticeTitle(@RequestParam int idx) {
	    String title = service.readTitle(idx); // 제목 가져오기
	    return ResponseEntity.ok(title);
	}
	
	
}
