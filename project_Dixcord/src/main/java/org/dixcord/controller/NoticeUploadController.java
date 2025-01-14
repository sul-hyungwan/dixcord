package org.dixcord.controller;

import org.dixcord.domain.NoticeAttachVO;
import org.dixcord.mapper.NoticeAttachMapper;
import org.dixcord.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

//NoticeUploadController 클래스: 공지사항 파일 업로드를 처리하는 컨트롤러
@Log4j // Lombok의 @Log4j 어노테이션: 로그를 기록할 수 있도록 지원
@Controller // Spring MVC의 컨트롤러로 등록
public class NoticeUploadController {

	// 파일 업로드를 처리하는 메서드
	@ResponseBody // 이 메서드의 반환값을 JSON 형태로 응답하도록 설정
	@PostMapping(value = "/noticeUploadFiles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) // HTTP POST 요청을 처리하며, URL과 응답 형식 지정.
	public ResponseEntity<List<NoticeAttachVO>> uploadFiles(MultipartFile[] uploadFile) throws IOException {
		// 허용된 MIME 유형 정의
	    List<String> allowedMimeTypes = List.of("image/png", "image/jpeg", "image/jfif", "image/jpeg");

	    if (uploadFile == null || uploadFile.length == 0) {
	        log.warn("업로드된 파일이 없습니다.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    List<NoticeAttachVO> list = new ArrayList<>();
	    String uploadFolder = "\\\\DESKTOP-8UUQVVD\\uploadImg";
	    File uploadPath = new File(uploadFolder, getFolder());

	    if (!uploadPath.exists()) {
	        uploadPath.mkdirs();
	    }

	    for (MultipartFile multipartFile : uploadFile) {
	        String mimeType = multipartFile.getContentType();
	        log.info("파일 MIME 유형 확인: " + mimeType);

	        // MIME 유형 검사
	        if (!allowedMimeTypes.contains(mimeType)) {
	            log.warn("허용되지 않은 파일 형식: " + mimeType);
	            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE); // 지원하지 않는 미디어 유형 상태 반환
	        }

	        String uploadFileName = multipartFile.getOriginalFilename();
	        uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
	        UUID uuid = UUID.randomUUID();
	        uploadFileName = uuid.toString() + "_" + uploadFileName;

	        try {
	            File saveFile = new File(uploadPath, uploadFileName);
	            multipartFile.transferTo(saveFile);

	            NoticeAttachVO noticeAttachVO = new NoticeAttachVO();
	            noticeAttachVO.setUuid(uuid.toString());
	            noticeAttachVO.setUploadPath(getFolder());
	            noticeAttachVO.setUploadName(uploadFileName);
	            list.add(noticeAttachVO);
	        } catch (Exception e) {
	            log.error("파일 저장 실패: " + e.getMessage());
	        }
	    }

	    return new ResponseEntity<>(list, HttpStatus.OK);
	}
	

	// 오늘 날짜를 기준으로 디렉터리 경로 문자열 생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷 지정
		Date date = new Date(); // 현재 날짜 가져오기
		String str = sdf.format(date); // 포맷에 맞게 문자열로 변환
		return str.replace("-", File.separator); // '-'를 OS의 디렉터리 구분자로 변환
	}
}